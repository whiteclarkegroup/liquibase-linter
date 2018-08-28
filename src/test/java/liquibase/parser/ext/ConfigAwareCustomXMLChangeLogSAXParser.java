package liquibase.parser.ext;

import liquibase.resource.ResourceAccessor;

public class ConfigAwareCustomXMLChangeLogSAXParser extends CustomXMLChangeLogSAXParser {

    @Override
    protected void loadConfig(ResourceAccessor resourceAccessor) {
        // Need to be change this if we are running tests in parallel...
        config = configLoader.load(resourceAccessor);
    }

    @Override
    public int getPriority() {
        return super.getPriority() + 1;
    }

}
