package hr.fer.oprpp1.hw05.shell.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * A {@link ShellCommand} that displays the provided directorys direct children
 *
 * @author Marko Tunjić
 */
public class LsShellCommand implements ShellCommand {

    /**
     * A method that list the direct children of the directory provided in the
     * arguments. If not valid directory a message is displayed. Quotes are allowed.
     * Display fomrat is: -rw- 53412 2009-03-15 12:59:31 azuriraj.ZIP. First column
     * directory,read,write,execute second column size third column date of creation
     * thrird format name
     *
     * @param env       the {@link Environment} on which the children should be
     *                  displayed
     * @param arguments the arguments for this command
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
        // if failed exit from command
        catch (IllegalArgumentException e) {
            env.writeln(e.getMessage());
            return ShellStatus.CONTINUE;
        }

        // check if correct number of arguments was provided
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

        // for each child repeat the formating
        for (File child : f.listFiles()) {
            // String builder for formatting
            StringBuilder firstColumn = new StringBuilder();

            // create first column
            firstColumn = child.isDirectory() ? firstColumn.append("d") : firstColumn.append("-");
            firstColumn = child.canRead() ? firstColumn.append("r") : firstColumn.append("-");
            firstColumn = child.canWrite() ? firstColumn.append("w") : firstColumn.append("-");
            firstColumn = child.canExecute() ? firstColumn.append("x") : firstColumn.append("-");

            // create date formatter
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // get file attributes and through then the creation date
            Path path = child.toPath();
            BasicFileAttributeView faView = Files.getFileAttributeView(
                    path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
            BasicFileAttributes attributes;

            // reading file attributes can throw a exception. MUST HANDLE
            try {
                attributes = faView.readAttributes();
            } catch (IOException e) {
                env.writeln("Could not read FileAttributes of file: " + child.getName());
                return ShellStatus.CONTINUE;
            }

            // get creation date and format
            FileTime fileTime = attributes.creationTime();
            String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));

            // display data and if getting size was unsuccessfull exit from command
            try {
                env.writeln(String.format("%s %10d %s %s", firstColumn.toString(), Files.size(child.toPath()),
                        formattedDateTime, child.getName()));
            } catch (IOException e) {
                env.writeln("Could not read file size");
            }
        }

        // exit from command
        return ShellStatus.CONTINUE;
    }

    /**
     * A method that returns this {@link ShellCommand} name aka ls
     *
     * @return {@link ShellCommand} name
     */
    @Override
    public String getCommandName() {
        return "ls";
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
        description.add("Otherwise the command displays all the direct children of the directory");
        return Collections.unmodifiableList(description);
    }

}
