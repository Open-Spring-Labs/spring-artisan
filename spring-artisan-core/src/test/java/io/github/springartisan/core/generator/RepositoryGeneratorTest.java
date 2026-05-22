package io.github.springartisan.core.generator;

import io.github.springartisan.core.GeneratorTestBase;
import io.github.springartisan.core.model.EntityDefinition;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RepositoryGeneratorTest extends GeneratorTestBase {

    private final RepositoryGenerator generator = new RepositoryGenerator(config, templateEngine);

    @Test
    void generatesRepositoryAnnotation() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("@Repository");
    }

    @Test
    void generatesJpaRepositoryExtension() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("extends JpaRepository<User, UUID>");
    }

    @Test
    void generatesCorrectPackage() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("package com.test.app.repository;");
    }

    @Test
    void generatesInterfaceDeclaration() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("public interface UserRepository");
    }

    @Test
    void generatesAuthorHeader() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("Author: Test Author");
    }

    @Test
    void basicRepositoryHasNoFindByMethods() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).doesNotContain("findBy");
    }

    @Test
    void repositoryWithFindByFieldsGeneratesMethods() throws Exception {
        EntityDefinition e = entity("User");
        e.getFindByFields().add("email");
        e.getFindByFields().add("name");
        String output = generator.generate(e);
        assertThat(output).contains("Optional<User> findByEmail(String email)")
                .contains("Optional<User> findByName(String name)");
    }

    @Test
    void paginatedRepositoryHasFindAllWithPageable() throws Exception {
        EntityDefinition e = entity("User");
        e.setPaginated(true);
        String output = generator.generate(e);
        assertThat(output).contains("Page<User> findAll(Pageable pageable)");
    }

    @Test
    void paginatedRepositoryImportsPagination() throws Exception {
        EntityDefinition e = entity("User");
        e.setPaginated(true);
        String output = generator.generate(e);
        assertThat(output).contains("import org.springframework.data.domain.Page")
                .contains("import org.springframework.data.domain.Pageable");
    }

    @Test
    void generatesOutputPath() {
        String path = generator.getOutputPath(entity("User"));
        assertThat(path).isEqualTo("com/test/app/repository/UserRepository.java");
    }
}
