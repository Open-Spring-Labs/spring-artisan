package io.github.springartisan.core.generator;

import io.github.springartisan.core.GeneratorTestBase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResolverGeneratorTest extends GeneratorTestBase {

    private final ResolverGenerator generator = new ResolverGenerator(config, templateEngine);

    @Test
    void generatesControllerAnnotation() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("@Controller");
    }

    @Test
    void generatesCorrectPackage() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("package com.test.app.resolver;");
    }

    @Test
    void generatesClassName() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("public class UserResolver");
    }

    @Test
    void generatesQueryMappings() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("@QueryMapping");
    }

    @Test
    void generatesMutationMappings() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("@MutationMapping");
    }

    @Test
    void generatesGraphQlImports() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("import org.springframework.graphql.data.method.annotation");
    }

    @Test
    void generatesServiceInjection() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("private final UserService service");
    }

    @Test
    void generatesFindAllQuery() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("users()");
    }

    @Test
    void generatesFindByIdQuery() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("user(@Argument UUID id)");
    }

    @Test
    void generatesCreateMutation() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("createUser");
    }

    @Test
    void generatesDeleteMutation() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("deleteUser");
    }

    @Test
    void generatesAuthorHeader() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("Author: Test Author");
    }

    @Test
    void generatesOutputPath() {
        String path = generator.getOutputPath(entity("User"));
        assertThat(path).isEqualTo("com/test/app/resolver/UserResolver.java");
    }
}
