package com.whiteclarkegroup.liquibaselinter.config;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import com.whiteclarkegroup.liquibaselinter.report.AbstractReporter;
import com.whiteclarkegroup.liquibaselinter.report.ReportItem;
import liquibase.exception.UnexpectedLiquibaseException;
import org.assertj.core.api.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static com.whiteclarkegroup.liquibaselinter.report.ReportItem.ReportItemType.*;

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
        RuleConfig ruleConfig = config.getRules().get("schema-name").get(0);
        assertTrue(ruleConfig.isEnabled());
    }

    @DisplayName("Should not support invalid config object")
    @Test
    void shouldNotSupportInValidConfigObject() throws IOException {
        String configJson = "{\n" +
            "  \"rules\": {\n" +
            "    \"no-duplicate-includes\": \"foo\"\n" +
            "  }\n" +
            "}";
        JsonMappingException mappingException =
            assertThrows(JsonMappingException.class, () -> OBJECT_MAPPER.readValue(configJson, Config.class));
        assertTrue(mappingException.getMessage().contains("instance of `com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig$RuleConfigBuilder`"));
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
        assertEquals(1, config.getRules().size());
        RuleConfig ruleConfig = config.getRules().get("file-name-no-spaces").get(0);
        assertTrue(ruleConfig.isEnabled());
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
        assertEquals(2, config.getRules().size());
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
        RuleConfig ruleConfig = config.getRules().get("file-name-no-spaces").get(0);
        assertFalse(ruleConfig.isEnabled());
    }

    @DisplayName("Should indicate whether rule enabled from config map")
    @Test
    void shouldIndicateWhetherRuleEnabledFromConfigMap() {
        ListMultimap<String, RuleConfig> map = ImmutableListMultimap.of(
            "present-but-off", RuleConfig.disabled(),
            "present-and-on", RuleConfig.enabled()
        );
        Config config = new Config.Builder().withRules(map).withFailFast(true).build();

        assertFalse(config.isRuleEnabled("not-even-present"));
        assertFalse(config.isRuleEnabled("present-but-off"));
        assertTrue(config.isRuleEnabled("present-and-on"));
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

    @DisplayName("Should load reporting configuration")
    @Test
    void shouldSupportReporting() throws IOException {
        String configJson = "{\n" +
            "  \"reporting\": {\n" +
            "    \"text\": \"path/to/report.txt\",\n" +
            "    \"console\": {\n" +
            "      \"filter\": \"ERROR\"" +
            "    },\n" +
            "    \"markdown\": [\n" +
            "      {\n" +
            "        \"path\": \"path/to/report.md\"," +
            "        \"filter\": [\n" +
            "          \"ERROR\",\n" +
            "          \"IGNORED\",\n" +
            "          \"PASSED\"\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"path\": \"path/to/report2.md\"," +
            "        \"enabled\": true\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";
        Config config = OBJECT_MAPPER.readValue(configJson, Config.class);
        assertThat(config.getReporting().asMap()).containsOnlyKeys("text", "console", "markdown");

        assertThat(config.getReporting().get("text")).extracting("path").containsExactly("path/to/report.txt");

        assertThat(config.getReporting().get("console")).extracting("enabled").containsExactly(true);
        assertThat(config.getReporting().get("console").get(0)).extracting("filter", as(InstanceOfAssertFactories.ITERABLE)).containsExactly(ERROR);

        assertThat(config.getReporting().get("markdown")).extracting("path").containsExactly("path/to/report.md", "path/to/report2.md");
        assertThat(config.getReporting().get("markdown").get(0)).extracting("filter", as(InstanceOfAssertFactories.ITERABLE)).containsExactly(ERROR, IGNORED, PASSED);
        assertThat(config.getReporting().get("markdown").get(1).isEnabled()).isTrue();
    }

    @DisplayName("Should not load with missing reporters")
    @Test
    void shouldNotLoadWithMissingReporters() throws IOException {
        String configJson = "{\n" +
            "  \"reporting\": {\n" +
            "    \"other\": false\n" +
            "  }\n" +
            "}";
        assertThatExceptionOfType(JsonMappingException.class).isThrownBy(() -> {
            Config config = OBJECT_MAPPER.readValue(configJson, Config.class);
        }).withMessageContaining("No lq lint reporter named 'other'");
    }

}
