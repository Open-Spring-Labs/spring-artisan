package io.github.springartisan.core.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GeneratorConfigTest {

    @Test
    void defaultConfigUsesJava() {
        GeneratorConfig config = GeneratorConfig.builder().build();
        assertThat(config.isKotlin()).isFalse();
        assertThat(config.getLanguage()).isEqualTo("java");
    }

    @Test
    void kotlinConfigDetectedCorrectly() {
        GeneratorConfig config = GeneratorConfig.builder().language("kotlin").build();
        assertThat(config.isKotlin()).isTrue();
    }

    @Test
    void kotlinDetectionIsCaseInsensitive() {
        assertThat(GeneratorConfig.builder().language("KOTLIN").build().isKotlin()).isTrue();
        assertThat(GeneratorConfig.builder().language("Kotlin").build().isKotlin()).isTrue();
    }

    @Test
    void javaOutputDirIsDefault() {
        GeneratorConfig config = GeneratorConfig.builder().build();
        assertThat(config.getEffectiveOutputDir()).isEqualTo("src/main/java");
    }

    @Test
    void kotlinOutputDirIsKotlinPath() {
        GeneratorConfig config = GeneratorConfig.builder().language("kotlin").build();
        assertThat(config.getEffectiveOutputDir()).isEqualTo("src/main/kotlin");
    }

    @Test
    void javaTestOutputDirIsDefault() {
        GeneratorConfig config = GeneratorConfig.builder().build();
        assertThat(config.getEffectiveTestOutputDir()).isEqualTo("src/test/java");
    }

    @Test
    void kotlinTestOutputDirIsKotlinPath() {
        GeneratorConfig config = GeneratorConfig.builder().language("kotlin").build();
        assertThat(config.getEffectiveTestOutputDir()).isEqualTo("src/test/kotlin");
    }

    @Test
    void packageDerivationWorksCorrectly() {
        GeneratorConfig config = GeneratorConfig.builder().packageBase("com.myapp").build();
        assertThat(config.getModelPackage()).isEqualTo("com.myapp.model");
        assertThat(config.getServicePackage()).isEqualTo("com.myapp.service");
        assertThat(config.getControllerPackage()).isEqualTo("com.myapp.controller");
        assertThat(config.getRepositoryPackage()).isEqualTo("com.myapp.repository");
        assertThat(config.getDtoPackage()).isEqualTo("com.myapp.dto");
    }

    @Test
    void defaultAuthorIsSet() {
        GeneratorConfig config = GeneratorConfig.builder().build();
        assertThat(config.getAuthor()).isEqualTo("Spring Artisan");
    }

    @Test
    void defaultApiPrefixIsSet() {
        GeneratorConfig config = GeneratorConfig.builder().build();
        assertThat(config.getApiPrefix()).isEqualTo("/api/v1");
    }

    @Test
    void defaultLombokIsEnabled() {
        GeneratorConfig config = GeneratorConfig.builder().build();
        assertThat(config.isIncludeLombok()).isTrue();
    }

    @Test
    void defaultValidationIsEnabled() {
        GeneratorConfig config = GeneratorConfig.builder().build();
        assertThat(config.isIncludeValidation()).isTrue();
    }
}
