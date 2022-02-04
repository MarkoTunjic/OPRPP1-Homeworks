package hr.fer.oprpp1.hw05.shell.commands;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

public class CopyShellCommand implements ShellCommand {

    /**
     * A {@link ShellCommand} that takes 2 arguments aka 2 paths to a
     * file (second can be a directory) and copies the content of the first file to
     * the second, if second already exists user is asked if allowed to overwrite,
     * if second is directory a new file with the name of the first file is created
     * in that directory and the content is copied. Quoting is allowed on both
     * arguments. If illegal paths or files are provided a message is displayed
     *
     * @param the       env on which the command is executed
     * @param arguments the user arguments (should contain two paths)
     *
     * @return {@link ShellStatus} CONTINUE
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {

        // try geting the arguments
        List<String> splittedArguments;
        try {
            splittedArguments = CommandUtils.splitWithQuotes(arguments, Set.of(0, 1));
        } catch (IllegalArgumentException e) {
            env.writeln(e.getMessage());
            return ShellStatus.CONTINUE;
        }

        // if illegal number of arguments was given
        if (splittedArguments.size() != 2) {
            env.writeln(String.format("2 argument expected but %d were given", splittedArguments.size()));
            return ShellStatus.CONTINUE;
        }

        // create first file
        File input = new File(splittedArguments.get(0));

        // check if valid
        if (input.isDirectory()) {
            env.writeln("Expected a file as first argument but directory was given.");
            return ShellStatus.CONTINUE;
        }

        // create second file
        File output = new File(splittedArguments.get(1));

        // check if exists
        if (output.exists() && !output.isDirectory()) {

            // ask to overwrite and repeat if response is not valid
            env.write("Overwrite file? (y/n) ");
            String line = env.readLine();
            while (!line.equals("y") && !line.equals("n")) {
                env.write("Overwrite file? (y/n) ");
                line = env.readLine();
            }

            // if response is negative then exit from command
            if (line.equals("n"))
                return ShellStatus.CONTINUE;
        }

        // if directory
        if (output.isDirectory())
            // create new file
            output = new File(splittedArguments.get(0) + input.getName());

        // try copying files
        try (InputStream is = Files.newInputStream(input.toPath());
                OutputStream os = Files.newOutputStream(output.toPath());) {

            // number of read bytes from the input
            int numOfReadBytes = 0;

            // create buffer
            byte[] buff = new byte[4000];

            // read file
            numOfReadBytes = is.read(buff);

            // while there is something to read
            while (numOfReadBytes != -1) {

                // write result to output stream or nothing if empty
                os.write(buff, 0, numOfReadBytes);

                // recreate buffer
                buff = new byte[4000];

                // read file
                numOfReadBytes = is.read(buff);
            }
        } catch (IOException e) {
            env.writeln("Could not open file/files: " + e.getMessage());
        }

        // exit from command
        return ShellStatus.CONTINUE;
    }

    /**
     * A method that returns this {@link ShellCommand} name aka copy
     *
     * @return {@link ShellCommand} name
     */
    @Override
    public String getCommandName() {
        return "copy";
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
        description.add("This command takes 2 arguments <PATH_TO_SOURCE_FILE><PATH_TO_DESTINATION>");
        description.add("If source path was invalid or not a file a message is displayed");
        description.add("If destionation already exist user is asked if allowed to overwrite");
        description
                .add("If destionation is a directory a file with the same name as the input will be created and copied");
        description
                .add("Othwerwise a new file with the specified name is created and filled");
        return Collections.unmodifiableList(description);
    }

}
