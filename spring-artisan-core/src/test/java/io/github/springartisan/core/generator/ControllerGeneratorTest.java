package io.github.springartisan.core.generator;

import io.github.springartisan.core.GeneratorTestBase;
import io.github.springartisan.core.model.EntityDefinition;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerGeneratorTest extends GeneratorTestBase {

    private final ControllerGenerator generator = new ControllerGenerator(config, templateEngine);

    @Test
    void generatesRestControllerAnnotation() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("@RestController");
    }

    @Test
    void generatesRequestMapping() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("@RequestMapping(\"/api/v1/user\")");
    }

    @Test
    void generatesCorrectPackage() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("package com.test.app.controller;");
    }

    @Test
    void generatesClassName() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("public class UserController");
    }

    @Test
    void generatesAuthorHeader() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("Author: Test Author");
    }

    @Test
    void standaloneControllerHasTodoComments() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("TODO: implement");
    }

    @Test
    void standaloneControllerHasNoServiceField() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).doesNotContain("private final UserService service");
    }

    @Test
    void controllerWithServiceInjectsService() throws Exception {
        EntityDefinition e = entity("User");
        e.setWithService(true);
        String output = generator.generate(e);
        assertThat(output).contains("private final UserService service");
    }

    @Test
    void controllerWithServiceImportsService() throws Exception {
        EntityDefinition e = entity("User");
        e.setWithService(true);
        String output = generator.generate(e);
        assertThat(output).contains("import com.test.app.service.UserService");
    }

    @Test
    void paginatedControllerUsesPagination() throws Exception {
        EntityDefinition e = entity("User");
        e.setPaginated(true);
        String output = generator.generate(e);
        assertThat(output).contains("Page<User>")
                .contains("Pageable pageable");
    }

    @Test
    void securedControllerHasPreAuthorize() throws Exception {
        EntityDefinition e = entity("User");
        e.setSecured(true);
        String output = generator.generate(e);
        assertThat(output).contains("@PreAuthorize");
    }

    @Test
    void openApiControllerHasTagAnnotation() throws Exception {
        EntityDefinition e = entity("User");
        e.setWithOpenApi(true);
        String output = generator.generate(e);
        assertThat(output).contains("@Tag(name = \"User\"")
                .contains("@Operation");
    }

    @Test
    void reactiveControllerUsesFlux() throws Exception {
        EntityDefinition e = entity("User");
        e.setReactive(true);
        String output = generator.generate(e);
        assertThat(output).contains("Flux<User>").contains("Mono<");
    }

    @Test
    void generatesGetAllEndpoint() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("@GetMapping");
    }

    @Test
    void generatesGetByIdEndpoint() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("@GetMapping(\"/{id}\")");
    }

    @Test
    void generatesPostEndpoint() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("@PostMapping");
    }

    @Test
    void generatesPutEndpoint() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("@PutMapping(\"/{id}\")");
    }

    @Test
    void generatesDeleteEndpoint() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("@DeleteMapping(\"/{id}\")");
    }

    @Test
    void generatesOutputPath() {
        String path = generator.getOutputPath(entity("User"));
        assertThat(path).isEqualTo("com/test/app/controller/UserController.java");
    }
}
