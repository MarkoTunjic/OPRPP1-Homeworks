package hr.fer.oprpp1.hw05.shell;

import java.util.List;

/**
 * An interface that defines all the methods that a ShellCommand must implement
 * for the shell to function properly
 *
 * @author Marko Tunjić
 */
public interface ShellCommand {
    /**
     * A method that executes the command on the given {@link Environment} with the
     * given arguments and returns the next {@link ShellStatus}
     *
     * @param env       the {@link Environment} on which the command should be
     *                  executed
     * @param arguments the arguments that the user provided through the shell
     *
     * @return the Next {@link ShellStatus}
     */
    ShellStatus executeCommand(Environment env, String arguments);

    /**
     * A method that returns this commands name.
     *
     * @return the commands name
     */
    String getCommandName();

    /**
     * A method that returns a List of strings that contains the description of what
     * this {@link ShellCommand} does
     *
     * @return a list of description lines of this {@link ShellCommand}
     */
    List<String> getCommandDescription();
}
