package hr.fer.oprpp1.hw05.shell;

import java.util.SortedMap;

/**
 * A interface that provides all the methods that a shell must be able to
 * complete
 *
 * @author Marko Tunjic
 */
public interface Environment {
    /**
     * A method that reads user input from the console and returns the string that
     * has been read. Throws {@link ShellIOException} if someting went wrong while
     * reading
     *
     * @return the user input from the console
     *
     * @throws ShellIOException if something went wrong while reading
     */
    String readLine() throws ShellIOException;

    /**
     * A method that writes a given String to the console. Throws
     * {@link ShellIOException} if something went wrong while writing.
     *
     * @param text the text to be displayed
     *
     * @throws ShellIOException if something went wrong while writing
     */
    void write(String text) throws ShellIOException;

    /**
     * A method that writes a given String to the console and eneters a new line.
     * Throws
     * {@link ShellIOException} if something went wrong while writing.
     *
     * @param text the text to be displayed
     *
     * @throws ShellIOException if something went wrong while writing
     */
    void writeln(String text) throws ShellIOException;

    /**
     * A method that returns a collection of all supoorted commands.
     *
     * @return All supported commands
     */
    SortedMap<String, ShellCommand> commands();

    /**
     * A method that returns the current multi line symbol
     *
     * @return the current multi line symbol
     */
    Character getMultilineSymbol();

    /**
     * A method that sets the current multi line symbol to the given param. Throws
     * {@link NullPointerException} if null was given
     *
     * @param symbol the new value of the multi line symbol
     *
     * @throws NullPointerException if null was given
     */
    void setMultilineSymbol(Character symbol);

    /**
     * A method that returns the current prompt symbol
     *
     * @return the current multi line symbol
     */
    Character getPromptSymbol();

    /**
     * A method that sets the current prompt symbol to the given param. Throws
     * {@link NullPointerException} if null was given
     *
     * @param symbol the new value of the prompt symbol
     *
     * @throws NullPointerException if null was given
     */
    void setPromptSymbol(Character symbol);

    /**
     * A method that returns the current more lines symbol
     *
     * @return the current multi line symbol
     */
    Character getMorelinesSymbol();

    /**
     * A method that sets the current more line symbol to the given param. Throws
     * {@link NullPointerException} if null was given
     *
     * @param symbol the new value of the more line symbol
     *
     * @throws NullPointerException if null was given
     */
    void setMorelinesSymbol(Character symbol);
}
