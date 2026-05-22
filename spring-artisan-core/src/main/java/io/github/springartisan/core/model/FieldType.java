package io.github.springartisan.core.model;

public enum FieldType {
    STRING("String", "", "String"),
    INTEGER("Integer", "", "Int"),
    LONG("Long", "", "Long"),
    DOUBLE("Double", "", "Double"),
    BOOLEAN("Boolean", "", "Boolean"),
    DATE("java.time.LocalDate", "import java.time.LocalDate;", "LocalDate"),
    TIMESTAMP("java.time.LocalDateTime", "import java.time.LocalDateTime;", "LocalDateTime"),
    UUID("UUID", "import java.util.UUID;", "UUID");

    private final String javaType;
    private final String importStatement;
    private final String kotlinType;

    FieldType(String javaType, String importStatement, String kotlinType) {
        this.javaType = javaType;
        this.importStatement = importStatement;
        this.kotlinType = kotlinType;
    }

    public String getJavaType() {
        return javaType;
    }

    public String getImportStatement() {
        return importStatement;
    }

    public String getKotlinType() {
        return kotlinType;
    }

    public static FieldType fromString(String type) {
        try {
            return FieldType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return STRING;
        }
    }
}
