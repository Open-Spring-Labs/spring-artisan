package io.github.springartisan.cli;

import io.github.springartisan.cli.commands.AuditCommand;
import io.github.springartisan.cli.commands.InitCommand;
import io.github.springartisan.cli.commands.ListCommand;
import io.github.springartisan.cli.commands.MakeCommand;
import picocli.CommandLine;

@CommandLine.Command(
    name = "spring-artisan",
    version = "1.0.0",
    description = "Code generator for Spring Boot projects, like Laravel Artisan",
    subcommands = {InitCommand.class, MakeCommand.class, ListCommand.class, AuditCommand.class},
    mixinStandardHelpOptions = true
)
public class SpringArtisanCli implements Runnable {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new SpringArtisanCli())
                .setExecutionExceptionHandler(new ExecutionExceptionHandler())
                .execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        System.out.println("Spring Artisan v1.0.0 - Spring Boot Code Generator");
        System.out.println("");
        System.out.println("Getting started:");
        System.out.println("  spring-artisan init                       initialize in your project");
        System.out.println("  spring-artisan make model User            generate a model");
        System.out.println("  spring-artisan make resource User         generate all 6 layers");
        System.out.println("  spring-artisan make entity User           generate from entities/User.yml");
        System.out.println("  spring-artisan make all                   generate from all entities/*.yml");
        System.out.println("  spring-artisan make exception-handler     generate global error handler");
        System.out.println("  spring-artisan list                       show generated entities");
        System.out.println("  spring-artisan audit                      check for missing layers");
        System.out.println("");
        System.out.println("Use 'spring-artisan --help' to see all commands");
    }

    public static class ExecutionExceptionHandler implements CommandLine.IExecutionExceptionHandler {
        @Override
        public int handleExecutionException(Exception ex, CommandLine cmd, CommandLine.ParseResult parseResult) {
            System.err.println("✗ Error: " + ex.getMessage());
            return 1;
        }
    }
}
