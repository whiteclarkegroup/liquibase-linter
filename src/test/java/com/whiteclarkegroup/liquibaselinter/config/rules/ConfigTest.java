package com.whiteclarkegroup.liquibaselinter.config.rules;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.whiteclarkegroup.liquibaselinter.config.Config;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @DisplayName("Should support valid config object")
    @Test
    void shouldSupportValidConfigObject() throws IOException {
        String configJson = "{\n" +
                "  \"rules\": {\n" +
                "    \"schema-name\": {\n" +
                "      \"enabled\": true,\n" +
                "      \"pattern\": \"^\\\\$\\\\{[a-z_]+\\\\}$\",\n" +
                "      \"errorMessage\": \"Must use schema name token, not %s\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        Config config = OBJECT_MAPPER.readValue(configJson, Config.class);
        assertEquals(1, config.getRules().size());
        RuleConfig ruleConfig = config.getRules().get("schema-name");
        assertTrue(ruleConfig.isEnabled());
    }

    @DisplayName("Should not support invalid config object")
    @Test
    void shouldNotSupportInValidConfigObject() throws IOException {
        String configJson = "{\n" +
                "  \"rules\": {\n" +
                "    \"no-duplicate-includes\": []\n" +
                "  }\n" +
                "}";
        JsonMappingException mappingException =
                assertThrows(JsonMappingException.class, () -> OBJECT_MAPPER.readValue(configJson, Config.class));
        assertTrue(mappingException.getMessage().contains("Cannot deserialize instance of `com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig$RuleConfigBuilder`"));
    }

    @DisplayName("Should support having rule config value as boolean")
    @Test
    void shouldSupportHavingRuleConfigAsBoolean() throws IOException {
        String configJson = "{\n" +
                "  \"rules\": {\n" +
                "    \"file-name-no-spaces\": {}\n" +
                "  }\n" +
                "}";
        Config config = OBJECT_MAPPER.readValue(configJson, Config.class);
        assertEquals(1, config.getRules().size());
        RuleConfig ruleConfig = config.getRules().get("file-name-no-spaces");
        assertTrue(ruleConfig.isEnabled());
    }

    @DisplayName("Should return disabled rule for null config object")
    @Test
    void shouldReturnDisabledRuleForNullConfigObject() throws IOException {
        String configJson = "{\n" +
                "  \"rules\": {\n" +
                "    \"file-name-no-spaces\": null\n" +
                "  }\n" +
                "}";
        Config config = OBJECT_MAPPER.readValue(configJson, Config.class);
        assertEquals(1, config.getRules().size());
        RuleConfig ruleConfig = config.getRules().get("file-name-no-spaces");
        assertFalse(ruleConfig.isEnabled());
    }

    @DisplayName("Should indicate whether rule enabled from config map")
    @Test
    void shouldIndicateWhetherRuleEnabledFromConfigMap() {
        Map<String, RuleConfig> map = ImmutableMap.of(
            "present-but-off", RuleConfig.disabled(),
            "present-and-on", RuleConfig.enabled()
        );
        Config config = new Config(null, map, true);

        assertFalse(config.isRuleEnabled("not-even-present"));
        assertFalse(config.isRuleEnabled("present-but-off"));
        assertTrue(config.isRuleEnabled("present-and-on"));
    }

}
