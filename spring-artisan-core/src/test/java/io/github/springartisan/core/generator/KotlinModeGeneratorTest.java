package io.github.springartisan.core.generator;

import io.github.springartisan.core.config.GeneratorConfig;
import io.github.springartisan.core.model.EntityDefinition;
import io.github.springartisan.core.model.EntityField;
import io.github.springartisan.core.template.TemplateEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KotlinModeGeneratorTest {

    private GeneratorConfig kotlinConfig;
    private TemplateEngine kotlinEngine;

    @BeforeEach
    void setUp() {
        kotlinConfig = GeneratorConfig.builder()
                .packageBase("com.test.app")
                .author("Test Author")
                .apiPrefix("/api/v1")
                .includeLombok(true)
                .includeValidation(true)
                .language("kotlin")
                .build();
        kotlinEngine = new TemplateEngine(kotlinConfig);
    }

    private EntityDefinition entity(String name) {
        EntityDefinition e = new EntityDefinition(name, kotlinConfig.getModelPackage());
        e.setAuthor(kotlinConfig.getAuthor());
        e.setUseLombok(kotlinConfig.isIncludeLombok());
        e.setAddValidation(kotlinConfig.isIncludeValidation());
        e.addField(EntityField.fromString("id:uuid"));
        e.addField(EntityField.fromString("name:string"));
        e.addField(EntityField.fromString("email:string"));
        return e;
    }

    @Test
    void modelGeneratorUsesKotlinTemplate() throws Exception {
        ModelGenerator generator = new ModelGenerator(kotlinConfig, kotlinEngine);
        String output = generator.generate(entity("User"));
        assertThat(output).contains("class User(");
    }

    @Test
    void modelGeneratorOutputPathHasKtExtension() {
        ModelGenerator generator = new ModelGenerator(kotlinConfig, kotlinEngine);
        String path = generator.getOutputPath(entity("User"));
        assertThat(path).endsWith("User.kt");
    }

    @Test
    void serviceGeneratorUsesKotlinSyntax() throws Exception {
        ServiceGenerator generator = new ServiceGenerator(kotlinConfig, kotlinEngine);
        String output = generator.generate(entity("User"));
        assertThat(output).contains("class UserService");
        assertThat(output).doesNotContain("public class");
    }

    @Test
    void serviceGeneratorOutputPathHasKtExtension() {
        ServiceGenerator generator = new ServiceGenerator(kotlinConfig, kotlinEngine);
        String path = generator.getOutputPath(entity("User"));
        assertThat(path).endsWith("UserService.kt");
    }

    @Test
    void controllerGeneratorUsesKotlinSyntax() throws Exception {
        ControllerGenerator generator = new ControllerGenerator(kotlinConfig, kotlinEngine);
        String output = generator.generate(entity("User"));
        assertThat(output).contains("class UserController");
        assertThat(output).doesNotContain("public class");
    }

    @Test
    void controllerGeneratorOutputPathHasKtExtension() {
        ControllerGenerator generator = new ControllerGenerator(kotlinConfig, kotlinEngine);
        String path = generator.getOutputPath(entity("User"));
        assertThat(path).endsWith("UserController.kt");
    }

    @Test
    void repositoryGeneratorUsesKotlinSyntax() throws Exception {
        RepositoryGenerator generator = new RepositoryGenerator(kotlinConfig, kotlinEngine);
        String output = generator.generate(entity("User"));
        assertThat(output).contains("interface UserRepository");
        assertThat(output).contains("JpaRepository<User, UUID>");
    }

    @Test
    void repositoryGeneratorOutputPathHasKtExtension() {
        RepositoryGenerator generator = new RepositoryGenerator(kotlinConfig, kotlinEngine);
        String path = generator.getOutputPath(entity("User"));
        assertThat(path).endsWith("UserRepository.kt");
    }

    @Test
    void kotlinConfigReturnsKotlinOutputDirs() {
        assertThat(kotlinConfig.getEffectiveOutputDir()).isEqualTo("src/main/kotlin");
        assertThat(kotlinConfig.getEffectiveTestOutputDir()).isEqualTo("src/test/kotlin");
    }
}
