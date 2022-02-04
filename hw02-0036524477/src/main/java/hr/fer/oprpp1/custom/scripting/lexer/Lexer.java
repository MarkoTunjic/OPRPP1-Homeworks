package hr.fer.oprpp1.custom.scripting.lexer;

import java.util.function.Predicate;

/**
 * A class that simulates a Lexical Analyzer for a specific set of rules
 *
 * @author Marko Tunjić
 */
public class Lexer {
	/** An attribute that contains the Text to analyze */
	private char[] data;

	/** An attribute that contains the reference to the last analyzed token */
	private Token token;

	/**
	 * An attribute that represents the index of the first not analyzed character
	 */
	private int currentIndex;

	/** An attribute that defines the state in which the Lexer currently is */
	private LexerState state;

	/**
	 * A constructor that creates an Lexer tokenizer from the given string by
	 * creating a char array from it
	 *
	 * @param text the to be analyzed
	 */
	public Lexer(String text) {
		if (text == null)
			throw new NullPointerException();
		data = text.toCharArray();
		this.state = LexerState.TEXT;
	}

	/**
	 * A method that return the next token of the given String by following specific
	 * rules
	 *
	 * @return the next token
	 * @throws LexerException whenever a invalid token appears
	 */
	public Token nextToken() {
		switch (this.state) {
		case INSIDE_TAG:
			return this.nextTokenInsideTag();
		case TAG:
			return this.nextTokenTag();
		default:
			return this.nextTokenText();

		}
	}

	/**
	 * A method that returns tokens by following rules when in Text state
	 *
	 * @return allowed text state tokens
	 * @throws LexerException whenever a invalid token appears
	 */
	private Token nextTokenText() {
		// check if last token was EOF
		if (token != null && token.getType() == TokenType.EOF)
			throw new LexerException("Requesting token after EOF");

		// check if we are at the end of the text if yes return EOF
		if (this.currentIndex >= data.length) {
			this.token = new Token(TokenType.EOF, null);
			return this.token;
		}

		// A string builder that build the value of the current token by appending
		// characters from the data
		StringBuilder currentValue = new StringBuilder();

		// check if there is opening tag
		if (this.data[this.currentIndex] == '{') {

			// check if the opening tag is complete if yes skip it and return false
			try {
				if (data[this.currentIndex + 1] == '$') {
					currentIndex += 2;
					this.token = new Token(TokenType.OPEN_TAG, "{$");
					return this.token;
				}
			} catch (IndexOutOfBoundsException ex) {
			}
		}

		// add all elements for which the predicate returns true
		addWhileTrue(currentValue, (element) -> {

			// check for escaping
			if (element == '\\') {
				// check if escaping was done legally
				if (data[++this.currentIndex] == '\\' || data[this.currentIndex] == '{')
					return true;
				else
					throw new LexerException("Illegal escaping inside text");
			} else if (this.data[this.currentIndex] == '{') {

				// check if the opening tag is complete if yes skip it and return false
				try {
					if (data[this.currentIndex + 1] == '$') {
						return false;
					}
				} catch (IndexOutOfBoundsException ex) {
				}
				return true;

			}
			// everything else is allowed in text
			else {
				return true;
			}
		});

		// change the last found token
		this.token = new Token(TokenType.TEXT, currentValue.toString());

		// return the token
		return this.token;
	}

	/**
	 * A method that returns tokens by following rules when searching for tag name
	 *
	 * @return allowed tag token
	 * @throws LexerException whenever a invalid token appears
	 */
	private Token nextTokenTag() {
		// check if last token was EOF
		if (token != null && token.getType() == TokenType.EOF)
			throw new LexerException("Requesting next token after EOF");

		// skip all of the blanks
		skipBlanks();

		// check if we are at the end of the text if yes return EOF
		if (this.currentIndex >= data.length) {
			this.token = new Token(TokenType.EOF, null);
			return this.token;
		}

		StringBuilder currentValue = new StringBuilder();

		// check if echo token
		if (this.data[this.currentIndex] == '=') {
			this.token = new Token(TokenType.TAG_NAME, String.valueOf(this.data[this.currentIndex++]));
		}
		// check if variable
		else if (Character.isLetter(this.data[this.currentIndex])) {
			// A string builder that build the value of the current token by appending
			// characters from the data

			addWhileTrue(currentValue,
					(element) -> (Character.isLetter(element) || Character.isDigit(element) || element == '_')
							&& !Character.isWhitespace(element));
			this.token = new Token(TokenType.TAG_NAME, currentValue.toString());
		}
		// check if number
		else if (Character.isDigit(this.data[this.currentIndex]) || this.data[this.currentIndex] == '-') {
			tokenizeNumber(currentValue);

		}
		// check for end tag
		else if (this.data[this.currentIndex] == '$') {
			tokenizeCloseTag(currentValue);
		}
		// check if function
		else if (this.data[this.currentIndex] == '@') {
			tokenizeFunction(currentValue);
		}
		// check if operator
		else if (this.data[this.currentIndex] == '+' || this.data[this.currentIndex] == '*'
				|| this.data[this.currentIndex] == '/' || this.data[this.currentIndex] == '^') {
			this.token = new Token(TokenType.OPERATOR, this.data[this.currentIndex++]);
		}
		// check if string
		else if (this.data[this.currentIndex] == '\"') {
			tokenizeString(currentValue);
		}
		// if nothing from above then it is a symbol
		else {
			this.token = new Token(TokenType.SYMBOL, String.valueOf(this.data[this.currentIndex++]));
		}
		return this.token;
	}

	/**
	 * A method that returns tokens by following rules when tokenizing inside of a
	 * tag
	 *
	 * @return allowed inside tag tokens
	 * @throws LexerException whenever a invalid token appears
	 */
	private Token nextTokenInsideTag() {
		// check if last token was EOF
		if (token != null && token.getType() == TokenType.EOF)
			throw new LexerException("Requesting next token after EOF");

		// skip all of the blanks
		skipBlanks();

		// check if we are at the end of the text if yes return EOF
		if (this.currentIndex >= data.length) {
			this.token = new Token(TokenType.EOF, null);
			return this.token;
		}

		// A string builder that build the value of the current token by appending
		// characters from the data
		StringBuilder currentValue = new StringBuilder();

		// check for end tag
		if (this.data[this.currentIndex] == '$') {
			tokenizeCloseTag(currentValue);
		}
		// check if number
		else if (Character.isDigit(this.data[this.currentIndex]) || this.data[this.currentIndex] == '-') {
			tokenizeNumber(currentValue);
		}
		// check if function
		else if (this.data[this.currentIndex] == '@') {
			tokenizeFunction(currentValue);
		}
		// check if operator
		else if (this.data[this.currentIndex] == '+' || this.data[this.currentIndex] == '*'
				|| this.data[this.currentIndex] == '/' || this.data[this.currentIndex] == '^') {
			this.token = new Token(TokenType.OPERATOR, this.data[this.currentIndex++]);
		}
		// check if variable
		else if (Character.isLetter(this.data[this.currentIndex])) {
			addWhileTrue(currentValue,
					(element) -> Character.isLetter(element) || Character.isDigit(element) || element == '_');
			this.token = new Token(TokenType.VARIABLE, currentValue.toString());
		}
		// check if string
		else if (this.data[this.currentIndex] == '\"') {
			tokenizeString(currentValue);
		} else if (this.data[this.currentIndex] == '\\') {
			throw new LexerException("Escaping forbidden inside a tag except in strings string");
		}
		// if nothing from above then it is a symbol
		else {
			this.token = new Token(TokenType.SYMBOL, String.valueOf(this.data[this.currentIndex++]));
		}
		return this.token;
	}

	/**
	 * A function that defines the rules for tokenizing numbers. Thros a
	 * LexerException if it is not a number
	 *
	 * @param currentValue the value of the final token
	 * @throws LexerException if the token is not valid a number
	 */
	private void tokenizeNumber(StringBuilder currentValue) {
		if (this.data[this.currentIndex] == '-') {
			// check if next character is a number if not tokenize the minus as a operator
			if (!Character.isDigit(this.data[this.currentIndex + 1])) {
				this.token = new Token(TokenType.OPERATOR, String.valueOf(this.data[this.currentIndex++]));
				return;
			}

			// tokenize as negative number
			currentValue.append(String.valueOf(this.data[this.currentIndex++]));
		}
		// add all numbers
		addWhileTrue(currentValue, (element) -> Character.isDigit(element));

		// check if there is a dot
		if (this.data[this.currentIndex] == '.') {
			currentValue.append(this.data[this.currentIndex++]);

			// add all numbers after the dot
			addWhileTrue(currentValue, (element) -> Character.isDigit(element));
		}

		// try creating integer
		try {
			this.token = new Token(TokenType.CONSTANT_INTEGER, Integer.parseInt(currentValue.toString()));
		} catch (NumberFormatException ex) {

			// try creating double
			try {
				this.token = new Token(TokenType.CONSTANT_DOUBLE, Double.parseDouble(currentValue.toString()));
			} catch (NumberFormatException e) {

				// if nor int nor double throw a exception
				throw new LexerException("Invalid number");
			}
		}
	}

	/**
	 * A method that helps us tokenize a close tag if it is a close tag otherwise it
	 * tokenizes a symbol
	 *
	 * @param currentValue the current token value
	 */
	private void tokenizeCloseTag(StringBuilder currentValue) {
		// check if end tag complete
		try {
			if (this.data[this.currentIndex + 1] == '}') {
				currentIndex += 2;
				this.token = new Token(TokenType.CLOSE_TAG, "$}");
				return;
			}
		} catch (IndexOutOfBoundsException e) {
		}

		// if not return symbol
		this.token = new Token(TokenType.SYMBOL, String.valueOf(this.data[this.currentIndex++]));
	}

	/**
	 * A method that helps us tokenize a string and throws a
	 * IndexOutOfBoundsException if the string was never closed or a lexer exception
	 * if escaping was done illegaly
	 *
	 * @param currentValue the current token value
	 * @throws IndexOutOfBoundsException if string never closed
	 */
	private void tokenizeString(StringBuilder currentValue) {
		// add the opening "
		currentValue.append(this.data[this.currentIndex++]);

		// create the string
		addWhileTrue(currentValue, (element) -> {

			// check if string escaped correctly
			if (element == '\\') {
				if (this.data[this.currentIndex + 1] == '\"') {
					this.currentIndex++;
					currentValue.append("\\");
					return true;
				} else if (this.data[this.currentIndex + 1] == 'n') {
					this.data[++this.currentIndex] = '\n';
					return true;
				} else if (this.data[this.currentIndex + 1] == 'r') {
					this.data[++this.currentIndex] = '\r';
					return true;
				} else if (this.data[this.currentIndex + 1] == 't') {
					this.data[++this.currentIndex] = '\t';
					return true;
				} else {
					throw new LexerException("Illegal escaping");
				}
			} else if (element == '\"') {
				return false;
			} else
				return true;
		});

		// add the closing "
		currentValue.append(this.data[this.currentIndex++]);

		// create new token
		this.token = new Token(TokenType.STRING, currentValue.toString());
	}

	/**
	 * A method that helps us tokenize a function if it is a function otherwise it
	 * tokenizes a symbol
	 *
	 * @param currentValue the current token value
	 */
	private void tokenizeFunction(StringBuilder currentValue) {

		try {
			// check if valid function name
			if (!Character.isLetter(this.data[this.currentIndex + 1])) {
				this.token = new Token(TokenType.SYMBOL, String.valueOf(this.data[this.currentIndex++]));
				return;
			}
		} catch (IndexOutOfBoundsException e) {
			this.token = new Token(TokenType.SYMBOL, String.valueOf(this.data[this.currentIndex++]));
			return;
		}

		currentValue.append(this.data[this.currentIndex++]);

		// tokenize the function
		addWhileTrue(currentValue,
				(element) -> Character.isLetter(element) || Character.isDigit(element) || element == '_');
		this.token = new Token(TokenType.FUNCTION, currentValue.toString());
	}

	/**
	 * A method that helps to add all the characters while the predicate returns
	 * true
	 *
	 * @param currentValue the current token value
	 * @param predicate    the predicate that tells when to stop
	 */
	private void addWhileTrue(StringBuilder currentValue, Predicate<Character> predicate) {
		while (this.currentIndex < data.length && predicate.test(data[this.currentIndex])) {
			currentValue.append(data[this.currentIndex]);
			this.currentIndex++;
		}
	}

	/** A method that skips all whitespace */
	private void skipBlanks() {
		while (this.currentIndex < data.length) {
			if (Character.isWhitespace(data[this.currentIndex])) {
				this.currentIndex++;
				continue;
			}
			break;
		}
	}

	/**
	 * A method that changes the state of the lexer to the given state if null
	 * Throws a NullPointerException
	 *
	 * @throws NullPointerException if null was given
	 */
	public void setState(LexerState state) {
		if (state == null)
			throw new NullPointerException();
		this.state = state;
	}
}
