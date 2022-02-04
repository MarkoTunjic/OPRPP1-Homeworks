package hr.fer.oprpp1.hw05.shell.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * A {@link ShellCommand} that is used for creating directories and it's parents
 * if neccessarry
 *
 * @author Marko Tunjić
 */
public class MkdirShellCommand implements ShellCommand {

    /**
     * A method that takes one argument from the arguments parameter that should be
     * a path to a directory and creates that directory and it's parents if they
     * don't exist. Quoting is allowed.
     *
     * @param env       the {@link Environment} in which this command is being
     *                  executed
     * @param arguments the provided arguments (should be only one the path to a
     *                  dir)
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

        // check if correct number of arguments was provideed
        if (splittedArguments.size() != 1) {
            env.writeln(String.format("1 argument expected but %d were given", splittedArguments.size()));
            return ShellStatus.CONTINUE;
        }

        // create file and create directories MUST HANDLE EXCEPTIONS
        File f = new File(splittedArguments.get(0));
        try {
            f.mkdirs();
        } catch (SecurityException e) {
            env.writeln("No permissions to create directory!");
        }

        // exit from command
        return ShellStatus.CONTINUE;
    }

    /**
     * A method that returns this {@link ShellCommand} name aka mkdir
     *
     * @return {@link ShellCommand} name
     */
    @Override
    public String getCommandName() {
        return "mkdir";
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
        description.add("If path is wrong a message is displayed");
        description.add("Otherwise the command creates the given directory structure");
        return Collections.unmodifiableList(description);
    }
}
