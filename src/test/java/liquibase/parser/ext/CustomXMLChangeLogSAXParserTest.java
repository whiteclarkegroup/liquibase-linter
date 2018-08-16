package liquibase.parser.ext;

import com.wcg.liquibase.config.Config;
import com.wcg.liquibase.resolvers.DefaultConfigParameterResolver;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(DefaultConfigParameterResolver.class)
class CustomXMLChangeLogSAXParserTest {

    @Test
    void should_prevent_duplicate_includes(Config config) throws Exception {
        CustomXMLChangeLogSAXParser parser = new CustomXMLChangeLogSAXParser();

        // do a couple includes
        parser.checkDuplicateIncludes("foo/bar/baz.xml", config);
        parser.checkDuplicateIncludes("other/thing.xml", config);

        // now do one of them again
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> parser.checkDuplicateIncludes("foo/bar/baz.xml", config));
        assertEquals("Changelog file 'foo/bar/baz.xml' was included more than once", changeLogParseException.getMessage());
    }
}