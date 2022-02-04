package hr.fer.oprpp1.hw05.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * A {@link ShellCommand} that provides help with using shell commands for the
 * provided shell
 *
 * @author Marko Tunjić
 */
public class HelpShellCommand implements ShellCommand {

    /**
     * A command that takes 0 or 1 argument. If none a list of all supported
     * commands is displayed and if one the description of the command given is
     * displayed. If command invalid a message is displayed.
     *
     * @param env       the {@link Environment} for which the help is needed
     * @param arguments the user arguments 0 or 1 command
     *
     * @return {@link ShellStatus} CONTINUE
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        // if no arguments provided list all commands
        if (arguments.length() == 0) {
            env.writeln("Supported commands: ");
            env.commands().keySet().forEach(key -> env.writeln("  " + key));
        }
        // if some arguments are given
        else {
            // get arguments
            String[] splittedArguments = arguments.trim().split(" ");

            // if more then one is given exit from command
            if (splittedArguments.length > 1) {
                env.writeln(String.format("Command expects 0 or 1 argument <comman> but %d were given",
                        splittedArguments.length));
                return ShellStatus.CONTINUE;
            }

            // if command is not supported print message and exit from command
            if (!env.commands().keySet().contains(splittedArguments[0])) {
                env.writeln(String.format("Unknown command %s",
                        splittedArguments[0]));
                return ShellStatus.CONTINUE;
            }

            // print command description
            env.writeln(splittedArguments[0]);
            env.commands().get(splittedArguments[0]).getCommandDescription()
                    .forEach(descriptionPart -> env.writeln("  " + descriptionPart));

        }

        // exit from command
        return ShellStatus.CONTINUE;
    }

    /**
     * A method that returns this {@link ShellCommand} name aka help
     *
     * @return {@link ShellCommand} name
     */
    @Override
    public String getCommandName() {
        return "help";
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
        description.add("This command takes 0 or 1 arguments <command>");
        description.add("If no arguments are given a list of all supported commands are displayed");
        description.add("If one argument is given the description of that command is given");
        description.add("If command is not supported a message is displayed");
        return Collections.unmodifiableList(description);
    }

}
