package io.github.springartisan.core.generator;

import io.github.springartisan.core.GeneratorTestBase;
import io.github.springartisan.core.model.EntityDefinition;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ModelGeneratorTest extends GeneratorTestBase {

    private final ModelGenerator generator = new ModelGenerator(config, templateEngine);

    @Test
    void generatesEntityAnnotation() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("@Entity");
    }

    @Test
    void generatesTableAnnotation() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("@Table(name = \"users\")");
    }

    @Test
    void generatesCorrectPackage() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("package com.test.app.model;");
    }

    @Test
    void generatesClassName() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("public class User");
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
        assertThat(output).contains("@Id")
                .contains("@GeneratedValue");
    }

    @Test
    void generatesAuthorHeader() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("Author: Test Author");
    }

    @Test
    void generatesStringFields() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("name").contains("email");
    }

    @Test
    void generatesBelongsToRelationship() throws Exception {
        EntityDefinition e = entity("Post");
        e.getBelongsTo().add("User");
        String output = generator.generate(e);
        assertThat(output).contains("@ManyToOne")
                .contains("@JoinColumn(name = \"user_id\")")
                .contains("private User user");
    }

    @Test
    void generatesHasManyRelationship() throws Exception {
        EntityDefinition e = entity("User");
        e.getHasMany().add("Post");
        String output = generator.generate(e);
        assertThat(output).contains("@OneToMany")
                .contains("mappedBy = \"user\"")
                .contains("List<Post>");
    }

    @Test
    void generatesOutputPath() {
        String path = generator.getOutputPath(entity("User"));
        assertThat(path).isEqualTo("com/test/app/model/User.java");
    }

    @Test
    void generatesTimestampFields() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("createdAt").contains("updatedAt");
    }

    @Test
    void generatesLifecycleCallbacks() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("@PrePersist").contains("@PreUpdate");
    }
}
