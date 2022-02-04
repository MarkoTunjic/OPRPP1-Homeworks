package hr.fer.oprpp1.hw05.shell.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * A {@link ShellCommand} that prints the tree for the provided directory
 *
 * @author Marko Tunjić
 */
public class TreeShellCommand implements ShellCommand {

    /**
     * A method that takes 1 aguments: a path to a directory and prints it's tree.
     * Quotes are allowed. If invalid path a message is displayed
     *
     * @param env       the {@link Environment} in which this command should be
     *                  executed
     * @param arguments the arguments for this {@link ShellCommand}
     *
     * @return {@link ShellStatus} CONTINUE
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

        // try getting arguments
        List<String> splittedArguments;
        try {
            splittedArguments = CommandUtils.splitWithQuotes(arguments, Set.of(0));
        }
        // if failed exit from command
        catch (IllegalArgumentException e) {
            env.writeln(e.getMessage());
            return ShellStatus.CONTINUE;
        }

        // check if correct number od arguments was given
        if (splittedArguments.size() != 1) {
            env.writeln(String.format("1 argument expected but %d were given", splittedArguments.size()));
            return ShellStatus.CONTINUE;
        }

        // check if directory
        File f = new File(splittedArguments.get(0));
        if (!f.isDirectory()) {
            env.writeln("The given path does not match a directory");
            return ShellStatus.CONTINUE;
        }

        // create a file visitor class for easier walikng
        class MyFileVisitor extends SimpleFileVisitor<Path> {
            private int depth = 0;

            /**
             * A method that on directory entrance increases depth and displays name of dir
             */
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                env.writeln("  ".repeat(depth) + dir.getFileName());
                depth++;
                return FileVisitResult.CONTINUE;
            }

            /**
             * A method that on directory entrance decreases dept
             */
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                depth--;
                return FileVisitResult.CONTINUE;
            }

            /**
             * A method that on file visit displays name of file
             */
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                env.writeln("  ".repeat(depth) + file.getFileName());
                return FileVisitResult.CONTINUE;
            }
        }

        // walk the tree and HANDLE EXCEPTIONS
        try {
            Files.walkFileTree(f.toPath(), new MyFileVisitor());
        } catch (IOException e) {
            env.writeln(e.getMessage());
        }

        // exit from command
        return ShellStatus.CONTINUE;
    }

    /**
     * A method that returns this {@link ShellCommand} name aka tree
     *
     * @return {@link ShellCommand} name
     */
    @Override
    public String getCommandName() {
        return "tree";
    }

    /**
     * A method that returns a List of strings that contains the description of what
     * this {@link ShellCommand} does
     *
     * @return a list of description lines of this {@link ShellCommand}
     */
    @Override
    public List<String> getCommandDescription() {
        List<String> description = new ArrayList<>();
        description.add("This command takes 1 argument <PATH_TO_DIR>");
        description.add("If path is wrong or not a dir a message is displayed");
        description.add("Otherwise the command displays all the tree of the directory");
        return Collections.unmodifiableList(description);
    }

}
