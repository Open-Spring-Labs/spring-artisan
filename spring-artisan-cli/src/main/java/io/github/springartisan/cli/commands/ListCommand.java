package io.github.springartisan.cli.commands;

import io.github.springartisan.core.config.ConfigLoader;
import io.github.springartisan.core.config.GeneratorConfig;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;

@CommandLine.Command(
    name = "list",
    description = "List all entities and their generated layers in this project",
    mixinStandardHelpOptions = true
)
public class ListCommand implements Callable<Integer> {

    @Override
    public Integer call() {
        GeneratorConfig config = ConfigLoader.load();

        String srcDir = config.getEffectiveOutputDir();
        String base = config.getPackageBase().replace(".", File.separator);

        Map<String, Set<String>> entities = new TreeMap<>();

        String[] layers = {"model", "service", "repository", "controller", "dto", "mapper", "resolver"};

        for (String layer : layers) {
            File dir = Paths.get(srcDir, base, layer).toFile();
            if (!dir.exists()) continue;
            File[] files = dir.listFiles((d, name) -> name.endsWith(".java") || name.endsWith(".kt"));
            if (files == null) continue;
            for (File f : files) {
                String name = f.getName().replaceAll("\\.(java|kt)$", "")
                        .replaceAll("(Service|Controller|Repository|DTO|Mapper|Resolver)$", "");
                entities.computeIfAbsent(name, k -> new TreeSet<>()).add(layer);
            }
        }

        if (entities.isEmpty()) {
            System.out.println("No generated files found under " + srcDir + "/" + base);
            return 0;
        }

        String[] allLayers = {"model", "service", "repository", "controller", "dto", "mapper", "resolver"};
        System.out.printf("%-20s  %s%n", "Entity", String.join("  ", allLayers));
        System.out.println("-".repeat(80));

        for (Map.Entry<String, Set<String>> entry : entities.entrySet()) {
            StringBuilder row = new StringBuilder(String.format("%-20s  ", entry.getKey()));
            for (String layer : allLayers) {
                String mark = entry.getValue().contains(layer) ? "✓" : "-";
                row.append(String.format("%-" + (layer.length() + 2) + "s", mark));
            }
            System.out.println(row);
        }

        return 0;
    }
}
