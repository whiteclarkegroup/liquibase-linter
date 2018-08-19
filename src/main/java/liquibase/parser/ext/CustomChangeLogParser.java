package liquibase.parser.ext;

import com.wcg.liquibase.ChangeLogLinter;
import com.wcg.liquibase.config.Config;
import com.wcg.liquibase.config.ConfigLoader;
import com.wcg.liquibase.config.rules.RuleRunner;
import com.wcg.liquibase.config.rules.RuleType;
import liquibase.changelog.ChangeLogParameters;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.ChangeLogParseException;
import liquibase.parser.ChangeLogParser;
import liquibase.parser.core.ParsedNode;
import liquibase.parser.core.xml.XMLChangeLogSAXParser;
import liquibase.resource.ResourceAccessor;

public class CustomChangeLogParser extends XMLChangeLogSAXParser implements ChangeLogParser {

    private ChangeLogLinter changeLogLinter = new ChangeLogLinter();
    private ConfigLoader configLoader = new ConfigLoader();
    private Config config;

    @Override
    public DatabaseChangeLog parse(String physicalChangeLogLocation, ChangeLogParameters changeLogParameters, ResourceAccessor resourceAccessor) throws ChangeLogParseException {
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

        changeLogLinter.lintChangeLog(changeLog, config, ruleRunner);
        return changeLog;
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

    private void loadConfig(ResourceAccessor resourceAccessor) {
        if (config == null) {
            config = configLoader.load(resourceAccessor);
        }
    }

}
