package liquibase.parser.ext;

import com.whiteclarkegroup.liquibaselinter.config.Config;
import com.whiteclarkegroup.liquibaselinter.resolvers.DefaultConfigParameterResolver;
import com.whiteclarkegroup.liquibaselinter.resolvers.RuleRunnerParameterResolver;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith({DefaultConfigParameterResolver.class, RuleRunnerParameterResolver.class})
class CustomXMLChangeLogSAXParserTest {

    @Test
    void shouldPreventDuplicateIncludes(Config config) throws Exception {
        LintAwareChangeLogSAXParser parser = new LintAwareChangeLogSAXParser();

        // do a couple includes
        parser.checkDuplicateIncludes("foo/bar/baz.xml", config);
        parser.checkDuplicateIncludes("other/thing.xml", config);

        // now do one of them again
        ChangeLogParseException changeLogParseException =
            assertThrows(ChangeLogParseException.class, () -> parser.checkDuplicateIncludes("foo/bar/baz.xml", config));
        assertEquals("Changelog file 'foo/bar/baz.xml' was included more than once", changeLogParseException.getMessage());
    }

}
