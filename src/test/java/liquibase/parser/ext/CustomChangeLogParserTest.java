package liquibase.parser.ext;

import com.wcg.liquibase.config.Config;
import com.wcg.liquibase.resolvers.DefaultConfigParameterResolver;
import liquibase.exception.ChangeLogParseException;
import liquibase.parser.core.ParsedNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(DefaultConfigParameterResolver.class)
public class CustomChangeLogParserTest {

    @Test
    void should_allow_token_schema_name(Config config) throws ChangeLogParseException {
        CustomChangeLogParser customChangeLogParser = new CustomChangeLogParser();
        ParsedNode parsedNode = mockParsedNode("${schema_name}");
        customChangeLogParser.checkSchemaName(parsedNode, config);
    }

    @Test
    void should_not_allow_raw_schema_name(Config config) {
        CustomChangeLogParser customChangeLogParser = new CustomChangeLogParser();
        ParsedNode parsedNode = mockParsedNode("SCHEMA_NAME");
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> customChangeLogParser.checkSchemaName(parsedNode, config));

        assertTrue(changeLogParseException.getMessage().contains("Must use schema name token, not SCHEMA_NAME"));
    }

    private ParsedNode mockParsedNode(String value) {
        ParsedNode parsedNode = mock(ParsedNode.class);
        when(parsedNode.getName()).thenReturn("schemaName");
        when(parsedNode.getValue()).thenReturn(value);
        return parsedNode;
    }
}
