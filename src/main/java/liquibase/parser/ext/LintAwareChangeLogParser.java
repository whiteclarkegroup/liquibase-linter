package liquibase.parser.ext;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.whiteclarkegroup.liquibaselinter.ChangeLogLinter;
import com.whiteclarkegroup.liquibaselinter.config.Config;
import com.whiteclarkegroup.liquibaselinter.config.ConfigLoader;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.report.ConsoleReporter;
import com.whiteclarkegroup.liquibaselinter.report.Report;
import com.whiteclarkegroup.liquibaselinter.report.Reporter;
import liquibase.changelog.ChangeLogParameters;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.ChangeLogParseException;
import liquibase.parser.ChangeLogParser;
import liquibase.parser.core.ParsedNode;
import liquibase.parser.core.json.JsonChangeLogParser;
import liquibase.parser.core.xml.XMLChangeLogSAXParser;
import liquibase.parser.core.yaml.YamlChangeLogParser;
import liquibase.resource.ResourceAccessor;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class LintAwareChangeLogParser implements ChangeLogParser {
    private static final Collection<Reporter> REPORTERS = ImmutableList.of(new ConsoleReporter());
    private static final CustomXMLChangeLogSAXParser XML_PARSER = new CustomXMLChangeLogSAXParser();
    private static final JsonChangeLogParser JSON_PARSER = new JsonChangeLogParser();
    private static final YamlChangeLogParser YAML_PARSER = new YamlChangeLogParser();

    protected final ConfigLoader configLoader = new ConfigLoader();
    private final Set<String> filesParsed = Sets.newConcurrentHashSet();
    private final ChangeLogLinter changeLogLinter = new ChangeLogLinter();
    protected Config config;
    private String rootPhysicalChangeLogLocation;

    @Override
    public DatabaseChangeLog parse(String physicalChangeLogLocation, ChangeLogParameters changeLogParameters, ResourceAccessor resourceAccessor) throws ChangeLogParseException {
        if (rootPhysicalChangeLogLocation == null) {
            rootPhysicalChangeLogLocation = physicalChangeLogLocation;
        }

        loadConfig(resourceAccessor);

        DatabaseChangeLog changeLog = getDatabaseChangeLog(physicalChangeLogLocation, changeLogParameters, resourceAccessor);

        if (!changeLog.getChangeSets().isEmpty()) {
            checkDuplicateIncludes(physicalChangeLogLocation, config);
        }

        RuleRunner ruleRunner = new RuleRunner(config, filesParsed);

        changeLogLinter.lintChangeLog(changeLog, config, ruleRunner);

        if (hasFinishedParsing(physicalChangeLogLocation)) {
            checkForFilesNotIncluded(config, resourceAccessor);
            runReports(ruleRunner.getReport());
        }

        filesParsed.add(physicalChangeLogLocation);

        return changeLog;
    }

    private boolean lintingEnabledFromCurrentChangeLog() {
        return !config.isEnabledFrom() || filesParsed.contains(config.getEnableFrom());
    }

    private DatabaseChangeLog getDatabaseChangeLog(String physicalChangeLogLocation, ChangeLogParameters changeLogParameters, ResourceAccessor resourceAccessor) throws ChangeLogParseException {
        if (XML_PARSER.supports(physicalChangeLogLocation, resourceAccessor)) {
            return parseXmlDatabaseChangeLog(physicalChangeLogLocation, changeLogParameters, resourceAccessor);
        } else if (JSON_PARSER.supports(physicalChangeLogLocation, resourceAccessor)) {
            return JSON_PARSER.parse(physicalChangeLogLocation, changeLogParameters, resourceAccessor);
        } else if (YAML_PARSER.supports(physicalChangeLogLocation, resourceAccessor)) {
            return YAML_PARSER.parse(physicalChangeLogLocation, changeLogParameters, resourceAccessor);
        }
        throw new IllegalArgumentException("Change log file type not supported");
    }

    private DatabaseChangeLog parseXmlDatabaseChangeLog(String physicalChangeLogLocation, ChangeLogParameters changeLogParameters, ResourceAccessor resourceAccessor) throws ChangeLogParseException {
        ParsedNode parsedNode = XML_PARSER.parseToNode(physicalChangeLogLocation, changeLogParameters, resourceAccessor);
        if (parsedNode == null) {
            return null;
        }

        DatabaseChangeLog changeLog = new DatabaseChangeLog(physicalChangeLogLocation);
        changeLog.setChangeLogParameters(changeLogParameters);
        try {
            changeLog.load(parsedNode, resourceAccessor);
        } catch (Exception e) {
            throw new ChangeLogParseException(e);
        }
        return changeLog;
    }

    private void runReports(Report report) throws ChangeLogParseException {
        if (report.hasItems()) {
            REPORTERS.forEach(reporter -> reporter.processReport(report));
            if (report.countErrors() > 0) {
                throw new ChangeLogParseException(String.format("Linting failed with %d errors", report.countErrors()));
            }
        }
    }

    private boolean hasFinishedParsing(String physicalChangeLogLocation) {
        //TODO move to DatabaseChangeLog::getRootChangeLog when we support liquibase 3.6 minimum
        return rootPhysicalChangeLogLocation.equals(physicalChangeLogLocation);
    }

    @Override
    public boolean supports(String changeLogFile, ResourceAccessor resourceAccessor) {
        return XML_PARSER.supports(changeLogFile, resourceAccessor)
            || JSON_PARSER.supports(changeLogFile, resourceAccessor)
            || YAML_PARSER.supports(changeLogFile, resourceAccessor);
    }

    @Override
    public int getPriority() {
        return 100;
    }

    protected void loadConfig(ResourceAccessor resourceAccessor) {
        if (config == null) {
            config = configLoader.load(resourceAccessor);
        }
    }

    private void checkForFilesNotIncluded(Config config, ResourceAccessor resourceAccessor) throws ChangeLogParseException {
        RuleConfig ruleConfig = config.getEnabledRuleConfig("file-not-included").stream().findAny().orElse(null);
        if (ruleConfig != null) {
            String fileExtension = rootPhysicalChangeLogLocation.substring(rootPhysicalChangeLogLocation.lastIndexOf("."));
            if (ruleConfig.getValues() == null || ruleConfig.getValues().isEmpty()) {
                throw new IllegalArgumentException("values not configured for rule `file-not-included`");
            }
            for (String path : ruleConfig.getValues()) {
                Collection<String> filesInDirectory = getFilesInDirectory(path, resourceAccessor);
                for (String file : filesInDirectory) {
                    checkFileHasBeenParsed(ruleConfig, file, fileExtension);
                }
            }
        }
    }

    private Collection<String> getFilesInDirectory(String path, ResourceAccessor resourceAccessor) {
        try {
            return resourceAccessor.list(null, path, true, false, true)
                .stream()
                .map(fullPath -> fullPath.substring(fullPath.lastIndexOf(path)))
                .collect(Collectors.toSet());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private void checkFileHasBeenParsed(RuleConfig ruleConfig, String file, String fileExtension) throws ChangeLogParseException {
        String changeLogPath = file.replace('\\', '/');
        if (changeLogPath.endsWith(fileExtension) && !filesParsed.contains(changeLogPath)) {
            final String errorMessage = Optional.ofNullable(ruleConfig.getErrorMessage()).orElse("Changelog file '%s' was not included in deltas change log");
            throw new ChangeLogParseException(String.format(errorMessage, changeLogPath));
        }
    }

    private void checkDuplicateIncludes(String physicalChangeLogLocation, Config config) throws ChangeLogParseException {
        RuleConfig ruleConfig = config.getEnabledRuleConfig("no-duplicate-includes").stream().findAny().orElse(null);
        if (ruleConfig != null) {
            if (filesParsed.contains(physicalChangeLogLocation)) {
                final String errorMessage = Optional.ofNullable(ruleConfig.getErrorMessage()).orElse("Changelog file '%s' was included more than once");
                throw new ChangeLogParseException(String.format(errorMessage, physicalChangeLogLocation));
            }
        }
    }

    public static class CustomXMLChangeLogSAXParser extends XMLChangeLogSAXParser implements ChangeLogParser {

        @Override
        public ParsedNode parseToNode(String physicalChangeLogLocation, ChangeLogParameters changeLogParameters, ResourceAccessor resourceAccessor) throws ChangeLogParseException {
            return super.parseToNode(physicalChangeLogLocation, changeLogParameters, resourceAccessor);
        }

    }

}
