package io.github.springartisan.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntityDefinition {
    private String name;
    private String packageName;
    private List<EntityField> fields;
    private String author;
    private boolean useLombok;
    private boolean addValidation;
    private String tableName;
    private boolean withService;
    private boolean withRepository;
    private boolean paginated;
    private boolean secured;
    private boolean withOpenApi;
    private boolean reactive;
    private boolean withMigration;
    private boolean integrationTest;

    @Builder.Default
    private List<String> findByFields = new ArrayList<>();

    @Builder.Default
    private List<String> belongsTo = new ArrayList<>();

    @Builder.Default
    private List<String> hasMany = new ArrayList<>();

    @Builder.Default
    private List<EntityField> relationships = new ArrayList<>();

    public EntityDefinition(String name, String packageName) {
        this.name = name;
        this.packageName = packageName;
        this.fields = new ArrayList<>();
        this.relationships = new ArrayList<>();
        this.tableName = name.toLowerCase() + "s";
        this.useLombok = true;
        this.addValidation = true;
    }

    public String getEntityNameLower() {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    public String getServiceName() {
        return name + "Service";
    }

    public String getControllerName() {
        return name + "Controller";
    }

    public String getRepositoryName() {
        return name + "Repository";
    }

    public String getDtoName() {
        return name + "DTO";
    }

    public String getTestName() {
        return name + "Test";
    }

    public Set<String> getRequiredImports() {
        Set<String> imports = new HashSet<>();
        
        // Add field imports
        for (EntityField field : fields) {
            if (field.needsImport()) {
                imports.add(field.getImportStatement());
            }
        }
        
        // Add validation imports if needed
        if (addValidation) {
            imports.add("import javax.validation.constraints.NotNull;");
            imports.add("import javax.validation.constraints.NotBlank;");
        }
        
        return imports;
    }

    public void addField(EntityField field) {
        if (fields == null) {
            fields = new ArrayList<>();
        }
        fields.add(field);
    }

    public void addFields(List<EntityField> fieldsToAdd) {
        if (fields == null) {
            fields = new ArrayList<>();
        }
        fields.addAll(fieldsToAdd);
    }
}
