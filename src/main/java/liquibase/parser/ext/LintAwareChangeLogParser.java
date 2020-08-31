package liquibase.parser.ext;

import com.google.common.collect.Sets;
import com.whiteclarkegroup.liquibaselinter.ChangeLogLinter;
import com.whiteclarkegroup.liquibaselinter.config.Config;
import com.whiteclarkegroup.liquibaselinter.config.ConfigLoader;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.report.Report;
import com.whiteclarkegroup.liquibaselinter.report.ReportItem;
import liquibase.changelog.ChangeLogParameters;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.ChangeLogParseException;
import liquibase.parser.ChangeLogParser;
import liquibase.parser.ChangeLogParserFactory;
import liquibase.resource.ResourceAccessor;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;

@SuppressWarnings("WeakerAccess")
public class LintAwareChangeLogParser implements ChangeLogParser {
    protected final ConfigLoader configLoader = new ConfigLoader();
    private final Set<String> filesParsed = Sets.newConcurrentHashSet();
    private final ChangeLogLinter changeLogLinter = new ChangeLogLinter();
    private final List<ReportItem> reportItems = new ArrayList<>();
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

        RuleRunner ruleRunner = new RuleRunner(config, reportItems, filesParsed);

        changeLogLinter.lintChangeLog(changeLog, config, ruleRunner);

        if (hasFinishedParsing(physicalChangeLogLocation)) {
            checkForFilesNotIncluded(config, resourceAccessor);
            runReports(ruleRunner.buildReport());
            final long errorCount = reportItems.stream().filter(item -> item.getType() == ReportItem.ReportItemType.ERROR).count();
            if (errorCount > 0) {
                throw new ChangeLogParseException(String.format("Linting failed with %d errors", errorCount));
            }

        }

        filesParsed.add(physicalChangeLogLocation);

        return changeLog;
    }

    private DatabaseChangeLog getDatabaseChangeLog(String physicalChangeLogLocation, ChangeLogParameters changeLogParameters, ResourceAccessor resourceAccessor) throws ChangeLogParseException {
        ChangeLogParser supportingParser = getParsers()
            .filter(parser -> parser.supports(physicalChangeLogLocation, resourceAccessor))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Change log file type not supported"));
        return supportingParser.parse(physicalChangeLogLocation, changeLogParameters, resourceAccessor);
    }

    private void runReports(Report report) throws ChangeLogParseException {
        config.getReporting().forEach((reportType, reporter) -> {
            if (reporter.getConfiguration().isEnabled()) {
                reporter.processReport(report);
            }
        });
    }

    private boolean hasFinishedParsing(String physicalChangeLogLocation) {
        //TODO move to DatabaseChangeLog::getRootChangeLog when we support liquibase 3.6 minimum
        return rootPhysicalChangeLogLocation.equals(physicalChangeLogLocation);
    }

    @Override
    public boolean supports(String changeLogFile, ResourceAccessor resourceAccessor) {
        return getParsers().anyMatch(parser -> parser.supports(changeLogFile, resourceAccessor));
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
                .collect(toSet());
        } catch (IOException e) {
            return emptyList();
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
        if (ruleConfig != null && filesParsed.contains(physicalChangeLogLocation)) {
            final String errorMessage = Optional.ofNullable(ruleConfig.getErrorMessage()).orElse("Changelog file '%s' was included more than once");
            throw new ChangeLogParseException(String.format(errorMessage, physicalChangeLogLocation));
        }
    }

    private Stream<ChangeLogParser> getParsers() {
        return ChangeLogParserFactory.getInstance().getParsers()
            .stream()
            .filter(parser -> !(parser instanceof LintAwareChangeLogParser));
    }

}
