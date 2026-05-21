package io.github.springartisan.core.model;

public enum FieldType {
    STRING("String", ""),
    INTEGER("Integer", ""),
    LONG("Long", ""),
    DOUBLE("Double", ""),
    BOOLEAN("Boolean", ""),
    DATE("java.time.LocalDate", "import java.time.LocalDate;"),
    TIMESTAMP("java.time.LocalDateTime", "import java.time.LocalDateTime;"),
    UUID("UUID", "import java.util.UUID;");

    private final String javaType;
    private final String importStatement;

    FieldType(String javaType, String importStatement) {
        this.javaType = javaType;
        this.importStatement = importStatement;
    }

    public String getJavaType() {
        return javaType;
    }

    public String getImportStatement() {
        return importStatement;
    }

    public static FieldType fromString(String type) {
        try {
            return FieldType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return STRING;
        }
    }
}
