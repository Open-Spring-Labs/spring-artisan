package io.github.springartisan.core.config;

import org.yaml.snakeyaml.Yaml;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class ConfigLoader {
    private static final String CONFIG_FILE = "spring-artisan.yml";
    private static final String FALLBACK_CONFIG = "spring-artisan-defaults.yml";

    public static GeneratorConfig load() {
        return load(System.getProperty("user.dir"));
    }

    public static GeneratorConfig load(String basePath) {
        GeneratorConfig config = GeneratorConfig.builder().build();
        
        // Try to load from current directory
        File configFile = new File(basePath, CONFIG_FILE);
        if (configFile.exists()) {
            config = loadFromFile(configFile);
        } else {
            // Load defaults
            config = loadDefaults();
        }
        
        return config;
    }

    private static GeneratorConfig loadFromFile(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            return parseYaml(fis);
        } catch (Exception e) {
            System.err.println("Failed to load config: " + e.getMessage());
            return loadDefaults();
        }
    }

    private static GeneratorConfig loadDefaults() {
        try {
            InputStream is = ConfigLoader.class.getClassLoader()
                    .getResourceAsStream(FALLBACK_CONFIG);
            if (is != null) {
                return parseYaml(is);
            }
        } catch (Exception e) {
            // Ignore
        }
        return GeneratorConfig.builder().build();
    }

    private static GeneratorConfig parseYaml(InputStream is) throws Exception {
        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(is);
        
        if (data == null || !data.containsKey("spring-artisan")) {
            return GeneratorConfig.builder().build();
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> config = (Map<String, Object>) data.get("spring-artisan");
        
        GeneratorConfig.GeneratorConfigBuilder builder = GeneratorConfig.builder();
        
        if (config.containsKey("package-base")) {
            builder.packageBase((String) config.get("package-base"));
        }
        if (config.containsKey("author")) {
            builder.author((String) config.get("author"));
        }
        if (config.containsKey("api-prefix")) {
            builder.apiPrefix((String) config.get("api-prefix"));
        }
        if (config.containsKey("output-dir")) {
            builder.outputDir((String) config.get("output-dir"));
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> generators = (Map<String, Object>) config.get("generators");
        if (generators != null) {
            if (generators.containsKey("include-lombok")) {
                builder.includeLombok((Boolean) generators.get("include-lombok"));
            }
            if (generators.containsKey("include-validation")) {
                builder.includeValidation((Boolean) generators.get("include-validation"));
            }
            if (generators.containsKey("test-framework")) {
                builder.testFramework((String) generators.get("test-framework"));
            }
        }
        
        return builder.build();
    }
}
