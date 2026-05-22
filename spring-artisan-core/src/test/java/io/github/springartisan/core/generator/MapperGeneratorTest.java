package io.github.springartisan.core.generator;

import io.github.springartisan.core.GeneratorTestBase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MapperGeneratorTest extends GeneratorTestBase {

    private final MapperGenerator generator = new MapperGenerator(config, templateEngine);

    @Test
    void generatesMapperAnnotation() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("@Mapper(componentModel = \"spring\"");
    }

    @Test
    void generatesInterfaceDeclaration() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("public interface UserMapper");
    }

    @Test
    void generatesCorrectPackage() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("package com.test.app.mapper;");
    }

    @Test
    void generatesToDtoMethod() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("UserDTO toDTO(User entity)");
    }

    @Test
    void generatestoEntityMethod() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("User toEntity(UserDTO dto)");
    }

    @Test
    void generatesUpdateEntityMethod() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("void updateEntity(UserDTO dto, @MappingTarget User entity)");
    }

    @Test
    void generatesImports() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("import org.mapstruct.Mapper")
                .contains("import org.mapstruct.MappingTarget");
    }

    @Test
    void generatesAuthorHeader() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("Author: Test Author");
    }

    @Test
    void generatesOutputPath() {
        String path = generator.getOutputPath(entity("User"));
        assertThat(path).isEqualTo("com/test/app/mapper/UserMapper.java");
    }
}
