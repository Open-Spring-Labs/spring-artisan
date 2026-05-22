package io.github.springartisan.core.generator;

import io.github.springartisan.core.GeneratorTestBase;
import io.github.springartisan.core.model.EntityDefinition;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceGeneratorTest extends GeneratorTestBase {

    private final ServiceGenerator generator = new ServiceGenerator(config, templateEngine);

    @Test
    void generatesServiceAnnotation() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("@Service");
    }

    @Test
    void generatesCorrectPackage() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("package com.test.app.service;");
    }

    @Test
    void generatesClassName() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("public class UserService");
    }

    @Test
    void generatesAuthorHeader() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("Author: Test Author");
    }

    @Test
    void standaloneServiceHasTodoMethods() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("TODO: implement");
    }

    @Test
    void standaloneServiceHasNoRepositoryImport() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).doesNotContain("UserRepository");
    }

    @Test
    void serviceWithRepositoryInjectsRepository() throws Exception {
        EntityDefinition e = entity("User");
        e.setWithRepository(true);
        String output = generator.generate(e);
        assertThat(output).contains("private final UserRepository repository");
    }

    @Test
    void serviceWithRepositoryImportsRepository() throws Exception {
        EntityDefinition e = entity("User");
        e.setWithRepository(true);
        String output = generator.generate(e);
        assertThat(output).contains("import com.test.app.repository.UserRepository");
    }

    @Test
    void serviceWithRepositoryHasFullCrudMethods() throws Exception {
        EntityDefinition e = entity("User");
        e.setWithRepository(true);
        String output = generator.generate(e);
        assertThat(output).contains("public List<User> findAll()")
                .contains("public Optional<User> findById(UUID id)")
                .contains("public User save(User entity)")
                .contains("public void deleteById(UUID id)")
                .contains("public boolean existsById(UUID id)");
    }

    @Test
    void serviceWithRepositoryHasRequiredArgsConstructor() throws Exception {
        EntityDefinition e = entity("User");
        e.setWithRepository(true);
        String output = generator.generate(e);
        assertThat(output).contains("@RequiredArgsConstructor");
    }

    @Test
    void generatesOutputPath() {
        String path = generator.getOutputPath(entity("User"));
        assertThat(path).isEqualTo("com/test/app/service/UserService.java");
    }

    @Test
    void generatesTransactionalAnnotation() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("@Transactional");
    }
}
