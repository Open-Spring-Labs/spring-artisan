package io.github.springartisan.core.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneratorConfig {
    @Builder.Default
    private String packageBase = "com.example.app";
    
    @Builder.Default
    private String author = "Spring Artisan";
    
    @Builder.Default
    private String apiPrefix = "/api/v1";
    
    @Builder.Default
    private boolean includeLombok = true;
    
    @Builder.Default
    private boolean includeValidation = true;
    
    @Builder.Default
    private String testFramework = "junit5";
    
    @Builder.Default
    private String outputDir = "src/main/java";
    
    @Builder.Default
    private String testOutputDir = "src/test/java";
    
    @Builder.Default
    private String templateDir = "";
    
    @Builder.Default
    private String resourcesDir = "src/main/resources";

    @Builder.Default
    private String language = "java";

    public boolean isKotlin() {
        return "kotlin".equalsIgnoreCase(language);
    }

    public String getEffectiveOutputDir() {
        return isKotlin() ? "src/main/kotlin" : outputDir;
    }

    public String getEffectiveTestOutputDir() {
        return isKotlin() ? "src/test/kotlin" : testOutputDir;
    }

    public String getModelPackage() {
        return packageBase + ".model";
    }

    public String getServicePackage() {
        return packageBase + ".service";
    }

    public String getControllerPackage() {
        return packageBase + ".controller";
    }

    public String getRepositoryPackage() {
        return packageBase + ".repository";
    }

    public String getDtoPackage() {
        return packageBase + ".dto";
    }

    public String getTestPackage() {
        return packageBase + ".service";
    }
}
