package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * A class that defines a Token for the Lexer to create
 * 
 * @author Marko Tunjić
 */
public class Token {
	/** An attribute that contains the type of this token */
	private TokenType type;

	/** An attribute that contains the value of this token */
	private Object value;

	/**
	 * A constructor that receives two arguments the token type and the value and
	 * creates a token with those attributes
	 * 
	 * @param type  the type of the token to be created
	 * @param value the value of the token to be created
	 */
	public Token(TokenType type, Object value) {
		this.type = type;
		this.value = value;
	}

	/** A method that returns the type of this token */
	public TokenType getType() {
		return type;
	}

	/** A method that returns this token value */
	public Object getValue() {
		return value;
	}
}
