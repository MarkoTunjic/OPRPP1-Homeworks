package hr.fer.oprpp1.hw05.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * A {@link ShellCommand} that simulates exiting from the shell
 *
 * @author Marko Tunjić
 */
public class ExitShellCommand implements ShellCommand {

    /**
     * A method that takes no arguments if some are given a message displayed, and
     * exits from the shell by returning TERMINATE.
     *
     * @param env       the shell from which should be exited
     * @param arguments the arguments from user (should be empty)
     *
     * @return {@link ShellStatus} TERMINATE
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        // check if no arguments were given
        if (arguments.length() > 0) {
            env.writeln("Command takes no arguments but some were given");
            return ShellStatus.CONTINUE;
        }

        // terimnate
        return ShellStatus.TERMINATE;
    }

    /**
     * A method that returns this {@link ShellCommand} name aka exit
     *
     * @return {@link ShellCommand} name
     */
    @Override
    public String getCommandName() {
        return "exit";
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
        description.add("This command takes no arguments and terminates the shell");
        description.add("If a argument is given a message is displayed");
        return Collections.unmodifiableList(description);
    }

}
