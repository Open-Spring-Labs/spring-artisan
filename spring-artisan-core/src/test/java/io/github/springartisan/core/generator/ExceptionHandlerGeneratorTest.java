package io.github.springartisan.core.generator;

import io.github.springartisan.core.GeneratorTestBase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionHandlerGeneratorTest extends GeneratorTestBase {

    private final ExceptionHandlerGenerator generator = new ExceptionHandlerGenerator(config, templateEngine);

    @Test
    void generatesRestControllerAdviceAnnotation() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("@RestControllerAdvice");
    }

    @Test
    void generatesCorrectPackage() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("package com.test.app.exception;");
    }

    @Test
    void generatesClassName() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("public class GlobalExceptionHandler");
    }

    @Test
    void generatesNotFoundHandler() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("handleNotFound")
                .contains("ResourceNotFoundException");
    }

    @Test
    void generatesValidationHandler() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("handleValidation")
                .contains("MethodArgumentNotValidException");
    }

    @Test
    void generatesGeneralExceptionHandler() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("handleGeneral");
    }

    @Test
    void generatesErrorResponseClass() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("class ErrorResponse");
    }

    @Test
    void generatesAuthorHeader() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("Author: Test Author");
    }

    @Test
    void generatesOutputPath() {
        String path = generator.getOutputPath(entity("User"));
        assertThat(path).isEqualTo("com/test/app/exception/GlobalExceptionHandler.java");
    }

    @Test
    void returnsCorrectHttpStatuses() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("HttpStatus.NOT_FOUND")
                .contains("HttpStatus.BAD_REQUEST")
                .contains("HttpStatus.INTERNAL_SERVER_ERROR");
    }
}
