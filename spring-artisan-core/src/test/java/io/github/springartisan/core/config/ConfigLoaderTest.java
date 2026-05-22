package io.github.springartisan.core.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigLoaderTest {

    @TempDir
    Path tempDir;

    @Test
    void loadsDefaultsWhenNoConfigFile() {
        GeneratorConfig config = ConfigLoader.load(tempDir.toString());
        assertThat(config).isNotNull();
        assertThat(config.getPackageBase()).isNotNull();
    }

    @Test
    void loadsConfigFromFile() throws Exception {
        String yaml = """
                spring-artisan:
                  package-base: com.example.myapp
                  author: John Doe
                  api-prefix: /api/v2
                  language: java
                  generators:
                    include-lombok: true
                    include-validation: true
                """;
        Files.writeString(tempDir.resolve("spring-artisan.yml"), yaml);

        GeneratorConfig config = ConfigLoader.load(tempDir.toString());

        assertThat(config.getPackageBase()).isEqualTo("com.example.myapp");
        assertThat(config.getAuthor()).isEqualTo("John Doe");
        assertThat(config.getApiPrefix()).isEqualTo("/api/v2");
    }

    @Test
    void loadsLanguageFromConfig() throws Exception {
        String yaml = """
                spring-artisan:
                  package-base: com.example.app
                  language: kotlin
                """;
        Files.writeString(tempDir.resolve("spring-artisan.yml"), yaml);

        GeneratorConfig config = ConfigLoader.load(tempDir.toString());

        assertThat(config.getLanguage()).isEqualTo("kotlin");
        assertThat(config.isKotlin()).isTrue();
    }

    @Test
    void loadsLombokSettingFromConfig() throws Exception {
        String yaml = """
                spring-artisan:
                  package-base: com.example.app
                  generators:
                    include-lombok: false
                    include-validation: false
                """;
        Files.writeString(tempDir.resolve("spring-artisan.yml"), yaml);

        GeneratorConfig config = ConfigLoader.load(tempDir.toString());

        assertThat(config.isIncludeLombok()).isFalse();
        assertThat(config.isIncludeValidation()).isFalse();
    }

    @Test
    void loadsOutputDirFromConfig() throws Exception {
        String yaml = """
                spring-artisan:
                  package-base: com.example.app
                  output-dir: src/generated/java
                  test-output-dir: src/generated/test
                """;
        Files.writeString(tempDir.resolve("spring-artisan.yml"), yaml);

        GeneratorConfig config = ConfigLoader.load(tempDir.toString());

        assertThat(config.getOutputDir()).isEqualTo("src/generated/java");
        assertThat(config.getTestOutputDir()).isEqualTo("src/generated/test");
    }

    @Test
    void returnsDefaultsForMissingKeys() throws Exception {
        String yaml = """
                spring-artisan:
                  package-base: com.example.minimal
                """;
        Files.writeString(tempDir.resolve("spring-artisan.yml"), yaml);

        GeneratorConfig config = ConfigLoader.load(tempDir.toString());

        assertThat(config.getPackageBase()).isEqualTo("com.example.minimal");
        assertThat(config.isIncludeLombok()).isTrue();
        assertThat(config.getApiPrefix()).isEqualTo("/api/v1");
    }
}
