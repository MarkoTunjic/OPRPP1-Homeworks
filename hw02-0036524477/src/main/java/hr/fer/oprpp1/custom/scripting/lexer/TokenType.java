package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * A enum that represents all of the possible token types
 * 
 * @author Marko Tunjić
 */
public enum TokenType {
	VARIABLE, CONSTANT_INTEGER, CONSTANT_DOUBLE, STRING, FUNCTION, OPERATOR, EOF, OPEN_TAG, CLOSE_TAG, TAG_NAME, SYMBOL,
	TEXT;
}
