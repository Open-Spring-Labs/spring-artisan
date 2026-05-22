package io.github.springartisan.core.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EntityDefinitionTest {

    @Test
    void twoArgConstructorInitializesLists() {
        EntityDefinition e = new EntityDefinition("User", "com.test.model");
        assertThat(e.getFields()).isNotNull().isEmpty();
        assertThat(e.getFindByFields()).isNotNull().isEmpty();
        assertThat(e.getBelongsTo()).isNotNull().isEmpty();
        assertThat(e.getHasMany()).isNotNull().isEmpty();
        assertThat(e.getRelationships()).isNotNull().isEmpty();
    }

    @Test
    void twoArgConstructorSetsTableName() {
        EntityDefinition e = new EntityDefinition("User", "com.test.model");
        assertThat(e.getTableName()).isEqualTo("users");
    }

    @Test
    void twoArgConstructorEnablesLombokAndValidation() {
        EntityDefinition e = new EntityDefinition("User", "com.test.model");
        assertThat(e.isUseLombok()).isTrue();
        assertThat(e.isAddValidation()).isTrue();
    }

    @Test
    void addFieldAppendsToList() {
        EntityDefinition e = new EntityDefinition("User", "com.test.model");
        EntityField field = EntityField.fromString("name:string");
        e.addField(field);
        assertThat(e.getFields()).hasSize(1).contains(field);
    }

    @Test
    void derivedNamesAreCorrect() {
        EntityDefinition e = new EntityDefinition("User", "com.test.model");
        assertThat(e.getServiceName()).isEqualTo("UserService");
        assertThat(e.getControllerName()).isEqualTo("UserController");
        assertThat(e.getRepositoryName()).isEqualTo("UserRepository");
        assertThat(e.getDtoName()).isEqualTo("UserDTO");
        assertThat(e.getTestName()).isEqualTo("UserTest");
        assertThat(e.getEntityNameLower()).isEqualTo("user");
    }

    @Test
    void builderDefaultListsAreNotNull() {
        EntityDefinition e = EntityDefinition.builder()
                .name("Product")
                .packageName("com.test.model")
                .build();
        assertThat(e.getFindByFields()).isNotNull();
        assertThat(e.getBelongsTo()).isNotNull();
        assertThat(e.getHasMany()).isNotNull();
    }

    @Test
    void canAddBelongsToRelationships() {
        EntityDefinition e = new EntityDefinition("Post", "com.test.model");
        e.getBelongsTo().add("User");
        e.getBelongsTo().add("Category");
        assertThat(e.getBelongsTo()).containsExactly("User", "Category");
    }

    @Test
    void canAddHasManyRelationships() {
        EntityDefinition e = new EntityDefinition("User", "com.test.model");
        e.getHasMany().add("Post");
        assertThat(e.getHasMany()).containsExactly("Post");
    }

    @Test
    void canAddFindByFields() {
        EntityDefinition e = new EntityDefinition("User", "com.test.model");
        e.getFindByFields().add("email");
        assertThat(e.getFindByFields()).containsExactly("email");
    }

    @Test
    void getRequiredImportsIncludesValidation() {
        EntityDefinition e = new EntityDefinition("User", "com.test.model");
        e.setAddValidation(true);
        assertThat(e.getRequiredImports())
                .anyMatch(i -> i.contains("NotNull"))
                .anyMatch(i -> i.contains("NotBlank"));
    }
}
