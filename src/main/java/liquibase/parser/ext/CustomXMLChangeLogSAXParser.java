package liquibase.parser.ext;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.whiteclarkegroup.liquibaselinter.ChangeLogLinter;
import com.whiteclarkegroup.liquibaselinter.config.Config;
import com.whiteclarkegroup.liquibaselinter.config.ConfigLoader;
import com.whiteclarkegroup.liquibaselinter.config.rules.Rule;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleType;
import com.whiteclarkegroup.liquibaselinter.report.ConsoleReporter;
import com.whiteclarkegroup.liquibaselinter.report.Report;
import com.whiteclarkegroup.liquibaselinter.report.Reporter;
import liquibase.changelog.ChangeLogParameters;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.ChangeLogParseException;
import liquibase.parser.ChangeLogParser;
import liquibase.parser.core.ParsedNode;
import liquibase.parser.core.xml.XMLChangeLogSAXParser;
import liquibase.resource.ResourceAccessor;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public class CustomXMLChangeLogSAXParser extends XMLChangeLogSAXParser implements ChangeLogParser {
    protected final ConfigLoader configLoader = new ConfigLoader();
    private final Set<String> alreadyParsed = Sets.newConcurrentHashSet();
    private final ChangeLogLinter changeLogLinter = new ChangeLogLinter();
    protected Config config;
    private String rootPhysicalChangeLogLocation;

    private static final Collection<Reporter> REPORTERS = ImmutableList.of(new ConsoleReporter());

    @Override
    public DatabaseChangeLog parse(String physicalChangeLogLocation, ChangeLogParameters changeLogParameters, ResourceAccessor resourceAccessor) throws ChangeLogParseException {
        if (rootPhysicalChangeLogLocation == null) {
            rootPhysicalChangeLogLocation = physicalChangeLogLocation;
        }

        loadConfig(resourceAccessor);

        ParsedNode parsedNode = parseToNode(physicalChangeLogLocation, changeLogParameters, resourceAccessor);
        if (parsedNode == null) {
            return null;
        }

        RuleRunner ruleRunner = config.getRuleRunner();

        checkSchemaName(parsedNode, ruleRunner);

        DatabaseChangeLog changeLog = new DatabaseChangeLog(physicalChangeLogLocation);
        changeLog.setChangeLogParameters(changeLogParameters);
        try {
            changeLog.load(parsedNode, resourceAccessor);
        } catch (Exception e) {
            throw new ChangeLogParseException(e);
        }

        if (!changeLog.getChangeSets().isEmpty()) {
            checkDuplicateIncludes(physicalChangeLogLocation, config);
        }

        changeLogLinter.lintChangeLog(changeLog, config, ruleRunner);

        runReports(physicalChangeLogLocation, ruleRunner.getReport());

        return changeLog;
    }

    private void runReports(String physicalChangeLogLocation, Report report) throws ChangeLogParseException {
        //TODO move to DatabaseChangeLog::getRootChangeLog when we support liquibase 3.6 minimum
        if (rootPhysicalChangeLogLocation.equals(physicalChangeLogLocation) && report.hasItems()) {
            REPORTERS.forEach(reporter -> reporter.processReport(report));
            if (report.countErrors() > 0) {
                throw new ChangeLogParseException(String.format("Linting failed with %d errors", report.countErrors()));
            }
        }
    }

    @Override
    public boolean supports(String changeLogFile, ResourceAccessor resourceAccessor) {
        return changeLogFile.toLowerCase().endsWith("xml");
    }

    @Override
    public int getPriority() {
        return 100;
    }

    void checkSchemaName(ParsedNode parsedNode, RuleRunner ruleRunner) throws ChangeLogParseException {
        if ("schemaName".equals(parsedNode.getName())) {
            ruleRunner.forGeneric().run(RuleType.SCHEMA_NAME, parsedNode.getValue().toString());
        }
        if (parsedNode.getChildren() != null && !parsedNode.getChildren().isEmpty()) {
            for (ParsedNode childNode : parsedNode.getChildren()) {
                checkSchemaName(childNode, ruleRunner);
            }
        }
    }

    protected void loadConfig(ResourceAccessor resourceAccessor) {
        if (config == null) {
            config = configLoader.load(resourceAccessor);
        }
    }

    void checkDuplicateIncludes(String physicalChangeLogLocation, Config config) throws ChangeLogParseException {
        final Optional<Rule> rule = RuleType.NO_DUPLICATE_INCLUDES.create(config.getRules());
        if (rule.isPresent() && rule.get().getRuleConfig().isEnabled()) {
            if (alreadyParsed.contains(physicalChangeLogLocation)) {
                final String errorMessage = Optional.ofNullable(rule.get().getErrorMessage()).orElse(RuleType.NO_DUPLICATE_INCLUDES.getDefaultErrorMessage());
                throw new ChangeLogParseException(String.format(errorMessage, physicalChangeLogLocation));
            }
            alreadyParsed.add(physicalChangeLogLocation);
        }
    }

}
