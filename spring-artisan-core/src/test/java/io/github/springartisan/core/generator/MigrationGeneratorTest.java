package io.github.springartisan.core.generator;

import io.github.springartisan.core.GeneratorTestBase;
import io.github.springartisan.core.model.EntityDefinition;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MigrationGeneratorTest extends GeneratorTestBase {

    private final MigrationGenerator generator = new MigrationGenerator(config, templateEngine);

    @Test
    void generatesCreateTableStatement() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("CREATE TABLE users");
    }

    @Test
    void generatesIdColumn() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("id UUID PRIMARY KEY");
    }

    @Test
    void generatesStringColumnsAsVarchar() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("VARCHAR(255)");
    }

    @Test
    void generatesTimestampColumns() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("created_at TIMESTAMP")
                .contains("updated_at TIMESTAMP");
    }

    @Test
    void generatesAuthorComment() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("Author: Test Author");
    }

    @Test
    void generatesTableComment() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("Migration: Create users table");
    }

    @Test
    void generatesBelongsToForeignKey() throws Exception {
        EntityDefinition e = entity("Post");
        e.getBelongsTo().add("User");
        String output = generator.generate(e);
        assertThat(output).contains("user_id UUID REFERENCES users(id)");
    }

    @Test
    void generatesOutputPathWithFlyWayConvention() {
        String path = generator.getOutputPath(entity("User"));
        assertThat(path).contains("db/migration/V")
                .contains("__create_users_table.sql");
    }

    @Test
    void generatesNotNullConstraintForNonNullableFields() throws Exception {
        String output = generator.generate(entity("User"));
        assertThat(output).contains("NOT NULL");
    }
}
