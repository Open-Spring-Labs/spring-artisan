package io.github.springartisan.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntityField {
    private String name;
    private FieldType type;
    private boolean nullable;
    private boolean unique;
    private String validation;
    private String comment;

    public static EntityField fromString(String fieldDef) {
        // Parse "id:uuid" or "name:string" or "email:string:unique"
        String[] parts = fieldDef.split(":");
        String name = parts[0].trim();
        FieldType type = parts.length > 1 ? FieldType.fromString(parts[1].trim()) : FieldType.STRING;
        
        EntityField field = new EntityField();
        field.setName(name);
        field.setType(type);
        field.setNullable(true);
        field.setUnique(false);
        field.setValidation("");
        
        // Check for unique modifier
        if (parts.length > 2 && "unique".equalsIgnoreCase(parts[2].trim())) {
            field.setUnique(true);
        }
        
        return field;
    }

    public String getJavaType() {
        return type.getJavaType();
    }

    public boolean needsImport() {
        return !type.getImportStatement().isEmpty();
    }

    public String getImportStatement() {
        return type.getImportStatement();
    }
}
