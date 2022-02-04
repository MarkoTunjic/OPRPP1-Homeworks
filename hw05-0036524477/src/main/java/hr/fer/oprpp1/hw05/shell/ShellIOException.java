package hr.fer.oprpp1.hw05.shell;

/**
 * A Exception class that is thrown whenever a exception during reading or
 * writing in a shell occurs
 *
 * @author Marko Tunjić
 */
public class ShellIOException extends RuntimeException {

    /** A default constructors that calls the super default constructor */
    public ShellIOException() {
        super();
    }

    /**
     * A constructors that recevies a {@link String} that represents a message from
     * the programmer and calls the super constructor with this message.
     *
     * @param msg the message to be sent
     */
    public ShellIOException(String msg) {
        super(msg);
    }
}
