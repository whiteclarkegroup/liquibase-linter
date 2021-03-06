package com.whiteclarkegroup.liquibaselinter.config;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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
        assertThat(config.getRules().size()).isEqualTo(1);
        assertThat(config.getRules().get("schema-name")).extracting("enabled").containsExactly(true);
    }

    @DisplayName("Should not support invalid config object")
    @Test
    void shouldNotSupportInValidConfigObject() throws IOException {
        String configJson = "{\n" +
            "  \"rules\": {\n" +
            "    \"no-duplicate-includes\": \"foo\"\n" +
            "  }\n" +
            "}";
        assertThatExceptionOfType(JsonMappingException.class)
            .isThrownBy(() -> OBJECT_MAPPER.readValue(configJson, Config.class))
            .withMessageContaining("instance of `com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig$RuleConfigBuilder`");
    }

    @DisplayName("Should support having rule config value as boolean")
    @Test
    void shouldSupportHavingRuleConfigAsBoolean() throws IOException {
        String configJson = "{\n" +
            "  \"rules\": {\n" +
            "    \"file-name-no-spaces\": true\n" +
            "  }\n" +
            "}";
        Config config = OBJECT_MAPPER.readValue(configJson, Config.class);

        assertThat(config.getRules().size()).isEqualTo(1);
        assertThat(config.getRules().get("file-name-no-spaces")).extracting("enabled").containsExactly(true);
    }

    @DisplayName("Should support having an array of configs for one rule")
    @Test
    void shouldSupportArrayOfRuleConfigs() throws IOException {
        String configJson = "{\n" +
            "    \"rules\": {\n" +
            "        \"object-name\": [\n" +
            "            {\n" +
            "                \"pattern\": \"^(?!_)[A-Z_0-9]+(?<!_)$\",\n" +
            "                \"errorMessage\": \"Object name '%s' name must be uppercase and use '_' separation\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"pattern\": \"^POWER.*$\",\n" +
            "                \"errorMessage\": \"Object name '%s' name must begin with 'POWER'\"\n" +
            "            }\n" +
            "        ]\n" +
            "    }\n" +
            "}\n";
        Config config = OBJECT_MAPPER.readValue(configJson, Config.class);
        assertThat(config.getRules().size()).isEqualTo(2);
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
        assertThat(config.getRules().size()).isEqualTo(1);
        assertThat(config.getRules().get("file-name-no-spaces")).extracting("enabled").containsExactly(false);
    }

    @DisplayName("Should indicate whether rule enabled from config map")
    @Test
    void shouldIndicateWhetherRuleEnabledFromConfigMap() {
        ListMultimap<String, RuleConfig> map = ImmutableListMultimap.of(
            "present-but-off", RuleConfig.disabled(),
            "present-and-on", RuleConfig.enabled()
        );
        Config config = new Config.Builder().withRules(map).withFailFast(true).build();

        assertThat(config.isRuleEnabled("not-even-present")).isFalse();
        assertThat(config.isRuleEnabled("present-but-off")).isFalse();
        assertThat(config.isRuleEnabled("present-and-on")).isTrue();
    }

    @DisplayName("Should support a simple import")
    @Test
    void shouldSupportSimpleImport() throws IOException {
        String configJson = "{\n" +
            "  \"import\": \"imported.json\"\n" +
            "}";
        Config config = OBJECT_MAPPER.readValue(configJson, Config.class);
        assertThat(config.getImports()).containsExactly("imported.json");
    }

    @DisplayName("Should support multiple imports")
    @Test
    void shouldSupportMultipleImports() throws IOException {
        String configJson = "{\n" +
            "  \"import\": [\n" +
            "    \"first.json\",\n" +
            "    \"second.json\"\n" +
            "  ]\n" +
            "}";
        Config config = OBJECT_MAPPER.readValue(configJson, Config.class);
        assertThat(config.getImports()).containsExactly("first.json", "second.json");
    }

    @DisplayName("Should create read-only config with builder")
    @Test
    void shouldCreateReadOnlyConfigWithBuilder() throws IOException {
        Config config = new Config.Builder().withIgnoreContextPattern("abc").withIgnoreFilesPattern("def")
            .withRules(ImmutableListMultimap.of("rule-name", RuleConfig.enabled()))
            .withFailFast(true).withEnableAfter("after").withImports("a", "b").build();

        assertThat(config.getIgnoreContextPattern()).asString().isEqualTo("abc");
        assertThat(config.getIgnoreFilesPattern()).asString().isEqualTo("def");
        assertThat(config.getRules().asMap()).containsOnlyKeys("rule-name");
        assertThat(config.isFailFast()).isTrue();
        assertThat(config.getEnableAfter()).isEqualTo("after");
        assertThat(config.getImports()).containsExactly("a", "b");

        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> config.getRules().put("new-rule", RuleConfig.enabled()));
        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> config.getImports().add("new-import"));
    }

    @DisplayName("Should copy existing config with builder")
    @Test
    void shouldCopyConfigWithBuilder() throws IOException {
        Config config = new Config.Builder().withIgnoreContextPattern("abc").withIgnoreFilesPattern("def")
            .withRules(ImmutableListMultimap.of("rule-name", RuleConfig.enabled()))
            .withFailFast(true).withEnableAfter("after").withImports("a", "b").build();
        Config copy = new Config.Builder(config).build();

        assertThat(config).usingRecursiveComparison().isEqualTo(copy);
    }
}
