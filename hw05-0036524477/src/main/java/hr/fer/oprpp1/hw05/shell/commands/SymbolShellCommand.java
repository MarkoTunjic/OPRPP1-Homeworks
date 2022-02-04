package hr.fer.oprpp1.hw05.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * A {@link ShellCommand} used for viewing and changing shell symbols
 *
 * @author Marko Tunjić
 */
public class SymbolShellCommand implements ShellCommand {

    /**
     * A method that takes 1 or 2 arguments from the arguments . The first is the
     * symbol name and the second is the new symbol value. If only first is provided
     * the current symbol is displayed and if both are provided the value of the
     * given symbol is set to the new value. If invalid symbol is given a message is
     * displayed.
     *
     * @param env       the {@link Environment} for which the symbols should be
     *                  updated or checked
     * @param arguments the provided arguments for this command
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

        // get arguments
        String[] splittedArguments = arguments.trim().split(" ");

        // check if correct number of arguments was provided
        if (arguments.length() == 0 || splittedArguments.length > 2) {
            env.writeln(
                    String.format("Symbol command accepts 1 or 2 arguments <SYMBOL_NAME><NEW_VALUE> but %d were given",
                            arguments.length() == 0 ? 0 : splittedArguments.length));
        }
        // if 2 arguments change the value
        else if (splittedArguments.length == 2) {

            // check if only one character is given as new Value
            if (splittedArguments[1].length() != 1) {
                env.writeln("The new symbol value must be a single character!");
                return ShellStatus.CONTINUE;
            }

            // determine command and set symbol
            if (splittedArguments[0].equals("PROMPT")) {
                char oldValue = env.getPromptSymbol();
                env.setPromptSymbol(splittedArguments[1].charAt(0));
                env.writeln(String.format("Symbol for %s changed from '%c' to '%c'", splittedArguments[0], oldValue,
                        splittedArguments[1].charAt(0)));
            }
            // determine command and set symbol
            else if (splittedArguments[0].equals("MORELINES")) {
                char oldValue = env.getMorelinesSymbol();
                env.setMorelinesSymbol(splittedArguments[1].charAt(0));
                env.writeln(String.format("Symbol for %s changed from '%c' to '%c'", splittedArguments[0], oldValue,
                        splittedArguments[1].charAt(0)));
            }
            // determine command and set symbol
            else if (splittedArguments[0].equals("MULTILINE")) {
                char oldValue = env.getMultilineSymbol();
                env.setMultilineSymbol(splittedArguments[1].charAt(0));
                env.writeln(String.format("Symbol for %s changed from '%c' to '%c'", splittedArguments[0], oldValue,
                        splittedArguments[1].charAt(0)));
            }
            // If none that display message and exit from command
            else {
                env.writeln(String.format("Unknown symbol %s.", splittedArguments[0]));
            }
        } else {
            // determine command and get symbol
            if (splittedArguments[0].equals("PROMPT"))
                env.writeln(String.format("Symbol for %s is '%c'", splittedArguments[0], env.getPromptSymbol()));

            // determine command and get symbol
            else if (splittedArguments[0].equals("MORELINES"))
                env.writeln(String.format("Symbol for %s is '%c'", splittedArguments[0], env.getMorelinesSymbol()));

            // determine command and get symbol
            else if (splittedArguments[0].equals("MULTILINE"))
                env.writeln(String.format("Symbol for %s is '%c'", splittedArguments[0], env.getMultilineSymbol()));

            // If none that display message and exit from command
            else
                env.writeln(String.format("Unknown symbol %s.", splittedArguments[0]));

        }

        // exit from command
        return ShellStatus.CONTINUE;
    }

    /**
     * A method that returns this {@link ShellCommand} name aka symbol
     *
     * @return {@link ShellCommand} name
     */
    @Override
    public String getCommandName() {
        return "symbol";
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
        description.add("This commands takes 1 or 2 arguments <SYMBOL_NAME><NEW_VALUE>");
        description.add("The first is the symbol name and is mandatory the second is a character and is optional");
        description.add("If only one is given the current symbol for the given symbol name is displayed");
        description.add("If both are given the new value is set to the symbol name");
        return Collections.unmodifiableList(description);
    }

}
