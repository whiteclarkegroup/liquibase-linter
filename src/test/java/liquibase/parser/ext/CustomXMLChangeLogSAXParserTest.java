package liquibase.parser.ext;

import com.whiteclarkegroup.liquibaselinter.config.Config;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.resolvers.DefaultConfigParameterResolver;
import com.whiteclarkegroup.liquibaselinter.resolvers.RuleRunnerParameterResolver;
import liquibase.exception.ChangeLogParseException;
import liquibase.parser.core.ParsedNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith({DefaultConfigParameterResolver.class, RuleRunnerParameterResolver.class})
class CustomXMLChangeLogSAXParserTest {

    @Test
    void shouldAllowTokenSchemaName(RuleRunner ruleRunner) {
        CustomXMLChangeLogSAXParser customXMLChangeLogSAXParser = new CustomXMLChangeLogSAXParser();
        ParsedNode parsedNode = mockParsedNode("${schema_name}");
        try {
            customXMLChangeLogSAXParser.checkSchemaName(parsedNode, ruleRunner);
        } catch (ChangeLogParseException e) {
            fail(e);
        }
    }

    @Test
    void shouldNotAllowRawSchemaName(RuleRunner ruleRunner) {
        CustomXMLChangeLogSAXParser customXMLChangeLogSAXParser = new CustomXMLChangeLogSAXParser();
        ParsedNode parsedNode = mockParsedNode("SCHEMA_NAME");
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> customXMLChangeLogSAXParser.checkSchemaName(parsedNode, ruleRunner));

        assertTrue(changeLogParseException.getMessage().contains("Must use schema name token, not SCHEMA_NAME"));
    }

    @Test
    void shouldPreventDuplicateIncludes(Config config) throws Exception {
        CustomXMLChangeLogSAXParser parser = new CustomXMLChangeLogSAXParser();

        // do a couple includes
        parser.checkDuplicateIncludes("foo/bar/baz.xml", config);
        parser.checkDuplicateIncludes("other/thing.xml", config);

        // now do one of them again
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> parser.checkDuplicateIncludes("foo/bar/baz.xml", config));
        assertEquals("Changelog file 'foo/bar/baz.xml' was included more than once", changeLogParseException.getMessage());
    }

    private ParsedNode mockParsedNode(String value) {
        ParsedNode parsedNode = mock(ParsedNode.class);
        when(parsedNode.getName()).thenReturn("schemaName");
        when(parsedNode.getValue()).thenReturn(value);
        return parsedNode;
    }
}
