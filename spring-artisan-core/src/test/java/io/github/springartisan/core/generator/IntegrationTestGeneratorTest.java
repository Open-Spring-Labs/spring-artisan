package io.github.springartisan.core.generator;

import io.github.springartisan.core.GeneratorTestBase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IntegrationTestGeneratorTest extends GeneratorTestBase {

    private final IntegrationTestGenerator generator = new IntegrationTestGenerator(config, templateEngine);

    @Test
    void generatesDataJpaTestAnnotation() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("@DataJpaTest");
    }

    @Test
    void generatesCorrectPackage() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("package com.test.app");
    }

    @Test
    void generatesClassName() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("class UserIntegrationTest");
    }

    @Test
    void generatesEntityManagerField() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("TestEntityManager entityManager");
    }

    @Test
    void generatesRepositoryField() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("UserRepository repository");
    }

    @Test
    void generatesSetUpMethod() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("@BeforeEach");
    }

    @Test
    void generatesPersistAndRetrieveTest() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("shouldPersistAndRetrieve");
    }

    @Test
    void generatesFindAllTest() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("shouldFindAll");
    }

    @Test
    void generatesDeleteTest() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("shouldDelete");
    }

    @Test
    void generatesAuthorHeader() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("Author: Test Author");
    }

    @Test
    void generatesOutputPath() {
        String path = generator.getOutputPath(entity("User"));
        assertThat(path).endsWith("UserIntegrationTest.java");
    }

    @Test
    void generatesAssertJImport() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("import static org.assertj.core.api.Assertions.*");
    }
}
