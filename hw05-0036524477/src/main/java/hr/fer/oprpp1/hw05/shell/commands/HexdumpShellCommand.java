package hr.fer.oprpp1.hw05.shell.commands;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * A {@link ShellCommand} that displayes a hexdump for a provided file
 *
 * @author Marko Tunjić
 */
public class HexdumpShellCommand implements ShellCommand {

    /**
     * A method that takes 1 path to a file in the arguments and displays that files
     * hex dump on the provided {@link Environment}. Quotes are allowed
     *
     * @param env       the {@link Environment} used for displaying the hexdump
     * @param arguments the arguments for the command
     *
     * @return {@link ShellStatus} CONTINUE
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

        // try getting the arguments
        List<String> splittedArguments;
        try {
            splittedArguments = CommandUtils.splitWithQuotes(arguments, Set.of(0));
        }
        // if fail exit from command
        catch (IllegalArgumentException e) {
            env.writeln(e.getMessage());
            return ShellStatus.CONTINUE;
        }

        // check if correct number of arguments was provided
        if (splittedArguments.size() != 1) {
            env.writeln(String.format("1 argument expected but %d were given", splittedArguments.size()));
            return ShellStatus.CONTINUE;
        }

        // check if a file is given
        File f = new File(splittedArguments.get(0));
        if (f.isDirectory()) {
            env.writeln("File expected but directory was given.");
            return ShellStatus.CONTINUE;
        }

        // try reading the file and displaying the hexdump
        try (InputStream is = Files.newInputStream(f.toPath())) {

            // number of bytes read from the input stream
            int numOfReadBytes = 0;

            // the counter of read hexes
            int counter = 0;

            // create buffer
            byte[] buff = new byte[16];

            // read file
            numOfReadBytes = is.read(buff);

            // while there is something to read
            while (numOfReadBytes != -1) {
                // create a new array for creating hexdump
                byte[] copy = new byte[numOfReadBytes];

                // string builder for building the hexdump format
                StringBuilder sb = new StringBuilder();

                // for each read byte
                int i;
                for (i = 0; i < numOfReadBytes; i++) {
                    // if byte less then 32 or grater than 127 replace with '.'
                    copy[i] = buff[i] < 32 || buff[i] > 127 ? (byte) '.' : buff[i];

                    // tranlate byte to hex
                    String hex = Integer.toHexString(copy[i]);

                    // create format
                    sb.append(hex.length() == 1 ? "0" : "").append(hex).append(i == 7 || i == 15 ? "|" : " ");
                }

                // fill up the fomrat if needed
                for (int j = i; j < 16; j++)
                    sb.append(j == 7 || j == 15 ? "  |" : "   ");

                // display the hex that has been read
                env.writeln(String.format("%08X: %s %s", counter, sb.toString(), new String(copy)));

                // increase number of hexes
                counter += 16;

                // recreate buffer
                buff = new byte[16];

                // read file
                numOfReadBytes = is.read(buff);
            }

        } catch (IOException e) {
            env.writeln("Could not open file: " + f.getName());
        }

        // exit from command
        return ShellStatus.CONTINUE;
    }

    /**
     * A method that returns this {@link ShellCommand} name aka hexdump
     *
     * @return {@link ShellCommand} name
     */
    @Override
    public String getCommandName() {
        return "hexdump";
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
        description.add("This command takes 1 argument <PATH_TO_FILE>");
        description.add("If path is wrong or not a file a message is displayed");
        description.add("Otherwise the command creates a hexdump for the given file");
        return Collections.unmodifiableList(description);
    }

}
