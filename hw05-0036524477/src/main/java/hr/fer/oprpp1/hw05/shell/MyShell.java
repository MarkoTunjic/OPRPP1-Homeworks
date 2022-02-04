package hr.fer.oprpp1.hw05.shell;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.oprpp1.hw05.shell.commands.*;

/**
 * A class that represents a simple implementation of a shell
 *
 * @author Marko Tunjic
 */
public class MyShell implements Environment {

    /** An private attribute that contains all of the supported commands */
    private SortedMap<String, ShellCommand> commands;

    /** An attribute that contains the current multi line symbol */
    private Character multiLineSymbol;

    /** An attribute that contains the current prompt symbol */
    private Character promptSymbol;

    /** An attribute that contains the current more line symbol */
    private Character moreLinesSymbol;

    /**
     * An private attribute that contains the refernce of object that can read user
     * input
     */
    private Scanner scanner;

    /**
     * A default constructor that sets the private attributes to the default value
     */
    public MyShell() {
        // default prompt symbol is '>'
        this.promptSymbol = '>';

        // default multi line symbol is '|'
        this.multiLineSymbol = '|';

        // default prompt is symbol '\'
        this.moreLinesSymbol = '\\';

        // fill up a map where the key is the name of the command and the value is a
        // object of that command
        SortedMap<String, ShellCommand> map = new TreeMap<>();
        map.put("cat", new CatShellCommand());
        map.put("charsets", new CharsetsShellCommand());
        map.put("copy", new CopyShellCommand());
        map.put("hexdump", new HexdumpShellCommand());
        map.put("ls", new LsShellCommand());
        map.put("mkdir", new MkdirShellCommand());
        map.put("tree", new TreeShellCommand());
        map.put("symbol", new SymbolShellCommand());
        map.put("exit", new ExitShellCommand());
        map.put("help", new HelpShellCommand());

        // save as unmodifiable map
        this.commands = Collections.unmodifiableSortedMap(map);

        // initialize scanner
        scanner = new Scanner(System.in);

        // write welcoming message
        this.writeln("Welcome to MyShell v 1.0");
    }

    /**
     * A method that reads user input from the console. Multiple lines can be read
     * if the line ends with the more line symbol. Throws a {@link ShellIOException}
     * if an error occured during reading
     *
     * @return the line that has been read or multiple lines that have been read
     *
     * @throws ShellIOException if something went wrong while reading
     */
    @Override
    public String readLine() throws ShellIOException {
        try {
            // Create string builder for multiple lines
            StringBuilder sb = new StringBuilder();

            // add new line
            sb.append(scanner.nextLine());

            // while line ends with moreLinesSymbol
            while (sb.toString().endsWith(moreLinesSymbol.toString())) {
                // remove moreLinesSymbol
                sb.deleteCharAt(sb.length() - 1);

                // write a Shell symbol for multiple lines on the console
                this.write(multiLineSymbol + " ");

                // read new line
                sb.append(scanner.nextLine());
            }

            // return all lines that have been read
            return sb.toString();
        }
        // these exceptions can happen druing reading and if some occured the shell
        // should terminate
        catch (NoSuchElementException | IllegalStateException e) {
            throw new ShellIOException(e.getMessage());
        }
    }

    /**
     * A method that writes a String on the terminal. Throws
     * {@link ShellIOException} if something went wrong while writing
     *
     * @param text the text that should be outputed to the console
     *
     * @throws ShellIOException if something went wrong while wirting
     */
    @Override
    public void write(String text) throws ShellIOException {
        try {
            System.out.print(text);
        } catch (Exception e) {
            throw new ShellIOException(e.getMessage());
        }
    }

    /**
     * A method that writes a String on the terminal and eneters a new line. Throws
     * {@link ShellIOException} if something went wrong while writing
     *
     * @param text the text that should be outputed to the console
     *
     * @throws ShellIOException if something went wrong while wirting
     */
    @Override
    public void writeln(String text) throws ShellIOException {
        try {
            System.out.println(text);
        } catch (Exception e) {
            throw new ShellIOException(e.getMessage());
        }
    }

    /**
     * A method that returns a unmodifiable collection of all supported commands
     *
     * @return a unmodifiable collection of all supported commands
     */
    @Override
    public SortedMap<String, ShellCommand> commands() {
        return commands;
    }

    /**
     * A method that returns the current multi line symbol
     *
     * @return the current multi line symbol
     */
    @Override
    public Character getMultilineSymbol() {
        return this.multiLineSymbol;
    }

    /**
     * A method that sets the current multi line symbol to the given param. Throws
     * {@link NullPointerException} if null was given
     *
     * @param symbol the new value of the multi line symbol
     *
     * @throws NullPointerException if null was given
     */
    @Override
    public void setMultilineSymbol(Character symbol) {
        if (symbol == null)
            throw new NullPointerException("Symbol can not be null!");

        this.multiLineSymbol = symbol;

    }

    /**
     * A method that returns the current prompt symbol
     *
     * @return the current multi line symbol
     */
    @Override
    public Character getPromptSymbol() {
        return this.promptSymbol;
    }

    /**
     * A method that sets the current prompt symbol to the given param. Throws
     * {@link NullPointerException} if null was given
     *
     * @param symbol the new value of the prompt symbol
     *
     * @throws NullPointerException if null was given
     */
    @Override
    public void setPromptSymbol(Character symbol) {
        if (symbol == null)
            throw new NullPointerException("Symbol can not be null!");

        this.promptSymbol = symbol;

    }

    /**
     * A method that returns the current more lines symbol
     *
     * @return the current multi line symbol
     */
    @Override
    public Character getMorelinesSymbol() {
        return this.moreLinesSymbol;
    }

    /**
     * A method that sets the current more line symbol to the given param. Throws
     * {@link NullPointerException} if null was given
     *
     * @param symbol the new value of the more line symbol
     *
     * @throws NullPointerException if null was given
     */
    @Override
    public void setMorelinesSymbol(Character symbol) {
        if (symbol == null)
            throw new NullPointerException("Symbol can not be null!");

        this.moreLinesSymbol = symbol;

    }

    /**
     * +A main method that creates a shell and runs the read command in a while loop
     * and calls the corresponding shell command. Throws {@link ShellIOException} if
     * anythign goes wrong while reading or writing
     *
     * @param args the command line arguments received from the user
     */
    public static void main(String[] args) {

        // initialize new shell
        MyShell myShell = new MyShell();

        ShellStatus status = null;

        // infinite loop
        do {
            // write a new prompt symbol
            myShell.write(myShell.getPromptSymbol().toString() + " ");

            // read next line
            String line = myShell.readLine();

            // get command name
            int firstSpace = line.indexOf(" ");
            String commandName = line.substring(0, firstSpace == -1 ? line.length() : firstSpace);

            // if new line was enetered just go to a new iteration
            if (commandName.length() == 0)
                continue;

            // find command
            ShellCommand command = myShell.commands.get(commandName);

            // if command was found
            if (command != null)
                // execute command and receive new status
                status = command.executeCommand(myShell,
                        line.substring(firstSpace == -1 ? line.length() : firstSpace + 1));

            // if command was not found then display the message
            else
                myShell.writeln(String.format("Unknown command %s.", commandName));

        } while (status != ShellStatus.TERMINATE);
    }

}
