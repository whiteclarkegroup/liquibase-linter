package liquibase.parser.ext;

import com.wcg.liquibase.config.rules.RuleRunner;
import com.wcg.liquibase.resolvers.RuleRunnerParameterResolver;
import liquibase.exception.ChangeLogParseException;
import liquibase.parser.core.ParsedNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(RuleRunnerParameterResolver.class)
public class CustomChangeLogParserTest {

    @Test
    void should_allow_token_schema_name(RuleRunner ruleRunner) throws ChangeLogParseException {
        CustomChangeLogParser customChangeLogParser = new CustomChangeLogParser();
        ParsedNode parsedNode = mockParsedNode("${schema_name}");
        customChangeLogParser.checkSchemaName(parsedNode, ruleRunner);
    }

    @Test
    void should_not_allow_raw_schema_name(RuleRunner ruleRunner) {
        CustomChangeLogParser customChangeLogParser = new CustomChangeLogParser();
        ParsedNode parsedNode = mockParsedNode("SCHEMA_NAME");
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> customChangeLogParser.checkSchemaName(parsedNode, ruleRunner));

        assertTrue(changeLogParseException.getMessage().contains("Must use schema name token, not SCHEMA_NAME"));
    }

    private ParsedNode mockParsedNode(String value) {
        ParsedNode parsedNode = mock(ParsedNode.class);
        when(parsedNode.getName()).thenReturn("schemaName");
        when(parsedNode.getValue()).thenReturn(value);
        return parsedNode;
    }
}
