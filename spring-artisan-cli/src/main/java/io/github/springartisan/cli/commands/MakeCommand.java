package io.github.springartisan.cli.commands;

import io.github.springartisan.cli.commands.subcommands.*;
import picocli.CommandLine;

@CommandLine.Command(
    name = "make",
    description = "Generate code",
    subcommands = {
        MakeModelCommand.class,
        MakeControllerCommand.class,
        MakeServiceCommand.class,
        MakeRepositoryCommand.class,
        MakeDtoCommand.class,
        MakeTestCommand.class,
        MakeResourceCommand.class
    },
    mixinStandardHelpOptions = true
)
public class MakeCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Specify what you want to make:");
        System.out.println("  spring-artisan make model <name>");
        System.out.println("  spring-artisan make controller <name>");
        System.out.println("  spring-artisan make service <name>");
        System.out.println("  spring-artisan make resource <name>");
        System.out.println("\nUse 'spring-artisan make <command> --help' for more information");
    }
}
