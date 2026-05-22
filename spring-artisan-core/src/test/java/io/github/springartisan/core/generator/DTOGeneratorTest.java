package io.github.springartisan.core.generator;

import io.github.springartisan.core.GeneratorTestBase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DTOGeneratorTest extends GeneratorTestBase {

    private final DTOGenerator generator = new DTOGenerator(config, templateEngine);

    @Test
    void generatesCorrectPackage() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("package com.test.app.dto;");
    }

    @Test
    void generatesClassName() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("public class UserDTO");
    }

    @Test
    void generatesLombokAnnotations() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("@Data")
                .contains("@Builder")
                .contains("@NoArgsConstructor")
                .contains("@AllArgsConstructor");
    }

    @Test
    void generatesIdField() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("private UUID id;");
    }

    @Test
    void generatesEntityFields() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("name").contains("email");
    }

    @Test
    void generatesTimestampFields() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("private LocalDateTime createdAt")
                .contains("private LocalDateTime updatedAt");
    }

    @Test
    void generatesAuthorHeader() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("Author: Test Author");
    }

    @Test
    void generatesOutputPath() {
        String path = generator.getOutputPath(entity("User"));
        assertThat(path).isEqualTo("com/test/app/dto/UserDTO.java");
    }
}
