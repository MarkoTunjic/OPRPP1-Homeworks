package hr.fer.oprpp1.hw05.shell.commands;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * A {@link ShellCommand} that read a file and siplays it content
 *
 * @author Marko Tunjić
 */
public class CatShellCommand implements ShellCommand {

    /**
     * A {@link ShellCommand} that receives a path to a file through and a charset
     * the arguments parameter and writes the content of that file on the provided
     * {@link Environment} parameter. The given path can be in quotes if needed. If
     * the path is not provided or not valid a message will be outputed to the
     * {@link Environment}. If a charset is not provided the system default is
     * taken.
     *
     * @param env       the {@link Environment} on which the command should be
     *                  executed
     * @param arguments the arguments provided from the user through the shell
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        // a varibale that contains all the provided arguments
        List<String> splittedArguments;

        // try getting the path
        try {
            splittedArguments = CommandUtils.splitWithQuotes(arguments, Set.of(0));
        }
        // if provided path was in illegal format print message and exit command
        catch (IllegalArgumentException e) {
            env.writeln(e.getMessage());
            return ShellStatus.CONTINUE;
        }

        // if invalid number of arguments was given print message and exit command
        if (splittedArguments.size() < 1 || splittedArguments.size() > 2) {
            env.writeln(String.format("Expected 1 or 2 arguments but %d were given", splittedArguments.size()));
            return ShellStatus.CONTINUE;
        }

        try {
            // try creating charset
            Charset cs = splittedArguments.size() == 1 ? Charset.defaultCharset()
                    : Charset.forName(splittedArguments.get(1));

            env.writeln(new String(Files.readAllBytes(Path.of(splittedArguments.get(0))), cs));
        }
        // reading lines
        catch (IOException e) {
            env.writeln("Could not open specified file (propably doesn't exist): " + splittedArguments.get(0));
        }
        // creating charset
        catch (IllegalCharsetNameException e) {
            env.writeln("Illegal charset: " + e.getMessage());
        }
        // creating charset
        catch (UnsupportedCharsetException e) {
            env.writeln("Unsupported charset: " + e.getMessage());
        }
        // Reading lines
        catch (SecurityException e) {
            env.writeln("No permission to open file.");
        }

        return ShellStatus.CONTINUE;
    }

    /**
     * A method that returns this {@link ShellCommand} name aka cat
     *
     * @return {@link ShellCommand} name
     */
    @Override
    public String getCommandName() {
        return "cat";
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
        description.add("This command takes 1 or 2 arguments <PATH_TO_FILE><CHARSET>");
        description.add("First is mandatory and second is optional");
        description.add("If only path is given the charset will be the system default charset");
        description.add("If given charset is not supported a message is displayed");
        description.add("The command opens the file and reads it with the given charset and displays it");
        return Collections.unmodifiableList(description);
    }

}
