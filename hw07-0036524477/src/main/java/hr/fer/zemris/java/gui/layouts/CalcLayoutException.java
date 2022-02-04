package hr.fer.zemris.java.gui.layouts;

/**
 * A exception that is thrown whenever the {@link CalcLayout} is used
 * innappropiately
 *
 * @author Marko Tunjić
 */
public class CalcLayoutException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /** A constructor with no message */
    public CalcLayoutException() {
        super();
    }

    /** A constructor with a message that will be displayed */
    public CalcLayoutException(String msg) {
        super(msg);
    }
}
