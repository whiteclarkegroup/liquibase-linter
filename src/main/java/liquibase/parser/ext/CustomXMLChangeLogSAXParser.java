package liquibase.parser.ext;

import com.wcg.liquibase.config.Config;
import com.wcg.liquibase.config.ConfigLoader;
import com.wcg.liquibase.config.rules.Rule;
import com.wcg.liquibase.config.rules.RuleType;
import liquibase.changelog.ChangeLogParameters;
import liquibase.exception.ChangeLogParseException;
import liquibase.parser.core.ParsedNode;
import liquibase.parser.core.xml.XMLChangeLogSAXParser;
import liquibase.resource.ResourceAccessor;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public class CustomXMLChangeLogSAXParser extends XMLChangeLogSAXParser {
    private final Set<String> alreadyParsed = new HashSet<>();
    private final ConfigLoader configLoader = new ConfigLoader();
    private Config config;

    @Override
    public int getPriority() {
        return super.getPriority() + 1;
    }

    @Override
    protected ParsedNode parseToNode(String physicalChangeLogLocation, ChangeLogParameters changeLogParameters, ResourceAccessor resourceAccessor) throws ChangeLogParseException {
        loadConfig(resourceAccessor);
        checkDuplicateIncludes(physicalChangeLogLocation, config);
        return super.parseToNode(physicalChangeLogLocation, changeLogParameters, resourceAccessor);
    }

    private void loadConfig(ResourceAccessor resourceAccessor) {
        if (config == null) {
            config = configLoader.load(resourceAccessor);
        }
    }

    void checkDuplicateIncludes(String physicalChangeLogLocation, Config config) throws ChangeLogParseException {
        final Rule rule = RuleType.NO_DUPLICATE_INCLUDES.create(config.getRules());
        if (rule.getRuleConfig().isEnabled()) {
            if (alreadyParsed.contains(physicalChangeLogLocation)) {
                throw new ChangeLogParseException(String.format(rule.getRuleConfig().getErrorMessage(), physicalChangeLogLocation));
            }
            alreadyParsed.add(physicalChangeLogLocation);
        }
    }
}
