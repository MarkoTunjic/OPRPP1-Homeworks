package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * An exception that is thrown whenever the Lexer runs into a not allowed
 * situation
 * 
 * @author Marko Tunjić
 */
public class LexerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LexerException() {
		super();
	}

	public LexerException(String message) {
		super(message);
	}
}
