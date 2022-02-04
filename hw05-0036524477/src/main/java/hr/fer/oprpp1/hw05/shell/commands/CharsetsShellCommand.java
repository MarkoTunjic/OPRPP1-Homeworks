package hr.fer.oprpp1.hw05.shell.commands;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * A {@link ShellCommand} that displays all available charsets
 *
 * @author Marko Tunjić
 */
public class CharsetsShellCommand implements ShellCommand {

    /**
     * A method that displays all available charsets on the given
     * {@link Environment} env. Expects 0 arguments if some are given a message is
     * displayed. Returns {@link ShellStatus} Continue
     *
     * @param env       the {@link Environment} used for writing
     * @param arguments the user arguments (should be empty)
     *
     * @return {@link ShellStatus} CONTINUE
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        // check if arguments were given
        if (arguments.trim().length() > 0) {
            env.writeln("Command takes no arguments but some were given");
            return ShellStatus.CONTINUE;
        }

        // print all charsets
        env.writeln("Supported charsets: ");
        for (String c : Charset.availableCharsets().keySet())
            env.writeln("  " + c);

        // continue
        return ShellStatus.CONTINUE;
    }

    /**
     * A method that returns this {@link ShellCommand} name aka charsets
     *
     * @return {@link ShellCommand} name
     */
    @Override
    public String getCommandName() {
        return "charsets";
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
        description.add("This command takes no arguments and returns all supported charsets");
        description.add("If a argument is given a message is displayed");
        return Collections.unmodifiableList(description);
    }

}
