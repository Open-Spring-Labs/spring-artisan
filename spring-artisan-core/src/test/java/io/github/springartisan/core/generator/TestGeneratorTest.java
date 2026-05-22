package io.github.springartisan.core.generator;

import io.github.springartisan.core.GeneratorTestBase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestGeneratorTest extends GeneratorTestBase {

    private final TestGenerator generator = new TestGenerator(config, templateEngine);

    @Test
    void generatesCorrectPackage() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("package com.test.app");
    }

    @Test
    void generatesDisplayNameAnnotation() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("@DisplayName");
    }

    @Test
    void generatesTestMethods() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("@Test");
    }

    @Test
    void generatesFindAllTest() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("testFindAll");
    }

    @Test
    void generatesFindByIdTest() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("testFindById");
    }

    @Test
    void generatesSaveTest() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("testSave");
    }

    @Test
    void generatesDeleteTest() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("testDeleteById");
    }

    @Test
    void generatesMockAnnotations() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("@Mock")
                .contains("MockitoAnnotations");
    }

    @Test
    void generatesAuthorHeader() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("Author: Test Author");
    }

    @Test
    void generatesOutputPath() {
        String path = generator.getOutputPath(entity("User"));
        assertThat(path).endsWith("UserServiceTest.java");
    }
}
