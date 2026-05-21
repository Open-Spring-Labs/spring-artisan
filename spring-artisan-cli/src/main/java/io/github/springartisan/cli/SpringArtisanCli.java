package io.github.springartisan.cli;

import io.github.springartisan.cli.commands.MakeCommand;
import picocli.CommandLine;

@CommandLine.Command(
    name = "spring-artisan",
    version = "1.0.0",
    description = "Code generator for Spring Boot projects, like Laravel Artisan",
    subcommands = {MakeCommand.class},
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
        System.out.println("Use 'spring-artisan make --help' to see available commands");
    }

    public static class ExecutionExceptionHandler implements CommandLine.IExecutionExceptionHandler {
        @Override
        public int handleExecutionException(Exception ex, CommandLine cmd, CommandLine.ParseResult parseResult) {
            ex.printStackTrace();
            return 1;
        }
    }
}
