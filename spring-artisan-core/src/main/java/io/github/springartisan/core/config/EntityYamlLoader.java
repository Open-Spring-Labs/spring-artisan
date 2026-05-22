package io.github.springartisan.core.config;

import io.github.springartisan.core.model.EntityDefinition;
import io.github.springartisan.core.model.EntityField;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

public class EntityYamlLoader {

    private static final String ENTITIES_DIR = "entities";

    public static EntityDefinition load(String entityName, GeneratorConfig config) throws Exception {
        File file = new File(System.getProperty("user.dir"), ENTITIES_DIR + "/" + entityName + ".yml");
        if (!file.exists()) {
            throw new IllegalArgumentException(
                "Entity definition not found: " + file.getAbsolutePath() + "\n" +
                "Create " + ENTITIES_DIR + "/" + entityName + ".yml to define the entity."
            );
        }
        return parseFile(file, config);
    }

    public static List<File> findAll() {
        File dir = new File(System.getProperty("user.dir"), ENTITIES_DIR);
        if (!dir.exists() || !dir.isDirectory()) return List.of();
        File[] files = dir.listFiles((d, name) -> name.endsWith(".yml"));
        return files != null ? List.of(files) : List.of();
    }

    public static EntityDefinition parseFile(File file, GeneratorConfig config) throws Exception {
        try (FileInputStream fis = new FileInputStream(file)) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(fis);

            String name = (String) data.get("name");
            EntityDefinition entity = new EntityDefinition(name, config.getModelPackage());
            entity.setAuthor(config.getAuthor());
            entity.setUseLombok(config.isIncludeLombok());
            entity.setAddValidation(config.isIncludeValidation());

            // Fields
            @SuppressWarnings("unchecked")
            List<Map<String, String>> fields = (List<Map<String, String>>) data.get("fields");
            if (fields != null) {
                for (Map<String, String> fieldMap : fields) {
                    fieldMap.forEach((fieldName, fieldType) ->
                        entity.addField(EntityField.fromString(fieldName + ":" + fieldType))
                    );
                }
            }

            // Relationships
            @SuppressWarnings("unchecked")
            List<Map<String, String>> relationships = (List<Map<String, String>>) data.get("relationships");
            if (relationships != null) {
                for (Map<String, String> rel : relationships) {
                    if (rel.containsKey("belongs-to")) entity.getBelongsTo().add(rel.get("belongs-to"));
                    if (rel.containsKey("has-many")) entity.getHasMany().add(rel.get("has-many"));
                }
            }

            return entity;
        }
    }
}
