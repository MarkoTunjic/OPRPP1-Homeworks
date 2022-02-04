package hr.fer.oprpp1.hw02.prob1;

import java.util.function.Predicate;

/**
 * A class that simulates a lexical analyzer for a specific set of rules</br>
 * 1. A word is a sequence of characters </br>
 * 2. A number is a sequence of numbers </br>
 * 3. A symbol is everything that is not a number or a word or a whitespace or a
 * escape character</br>
 * 4. "\" is used for escaping
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
		this.state = LexerState.BASIC;
	}

	/**
	 * A method that calculates and returns the next token from the string given to
	 * the lexer to tokenize. A LexerException is thrown if a invalid letter is
	 * escaped or a invalid number was given
	 * 
	 * @return the next token of the text
	 * @throws LexerException if invalid number or invalid character escaped
	 */
	public Token nextToken() {
		if (this.state == LexerState.BASIC)
			return this.nextTokenBasic();
		else
			return this.nextTokenExtended();

	}

	/**
	 * A method that helps to calculate the next token in the basic state from the
	 * string given string to the lexer to tokenize. A LexerException is thrown if a
	 * invalid letter is escaped or a invalid number was given
	 * 
	 * @return the next token of the text
	 * @throws LexerException if invalid number or invalid character escaped
	 */
	private Token nextTokenBasic() {
		// check if last token was EOF
		if (token != null && token.getType() == TokenType.EOF)
			throw new LexerException("No tokens available");

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

		// the current token we are calculating
		Token currentToken;

		// check if the current character is a letter or a escape character
		if (Character.isLetter(data[this.currentIndex]) || data[this.currentIndex] == '\\') {

			// keep adding elements to the string builder while we are adding only letters
			addWhileTrue(currentValue, (element) -> {

				// check if letter
				if (Character.isLetter(data[this.currentIndex]))
					return true;

				// check if escape character
				else if (data[this.currentIndex] == '\\') {
					try {

						// check if the next character is a number or another escape character if yes
						// return true if not throw a LexerException
						if (Character.isDigit(data[++this.currentIndex]) || data[this.currentIndex] == '\\') {
							return true;
						} else {
							throw new LexerException();
						}
					}
					// if a escape character is the last index the previous if clause will cause a
					// index out of bound because of the data[++currentIndex]
					catch (IndexOutOfBoundsException ex) {
						throw new LexerException();
					}
				}
				// if not a letter or a escaped character return false
				else {
					return false;
				}
			});

			// create the new token
			currentToken = new Token(TokenType.WORD, currentValue.toString());
		}
		// check if the current character is a number
		else if (Character.isDigit(data[this.currentIndex])) {
			addWhileTrue(currentValue, (element) -> Character.isDigit(element));

			// check if the number can be a long
			try {
				currentToken = new Token(TokenType.NUMBER, Long.parseLong(currentValue.toString()));
			} catch (NumberFormatException ex) {
				throw new LexerException();
			}
		}
		// if it is neither a letter nor a number nor a "#" it must be a symbol
		else {
			currentToken = new Token(TokenType.SYMBOL, data[this.currentIndex++]);
		}

		// change the last token to the current token
		this.token = currentToken;

		// return the token
		return currentToken;
	}

	/**
	 * A method that helps to calculate the next token in the basic state from the
	 * string given string to the lexer to tokenize. A LexerException is thrown if a
	 * invalid letter is escaped or a invalid number was given
	 * 
	 * @return the next token of the text
	 * @throws LexerException if invalid number or invalid character escaped
	 */
	private Token nextTokenExtended() {
		// check if last token was EOF
		if (token != null && token.getType() == TokenType.EOF)
			throw new LexerException("No tokens available");

		// skip all of the blanks
		skipBlanks();

		// check if we are at the end of the text if yes return EOF
		if (this.currentIndex >= data.length) {
			this.token = new Token(TokenType.EOF, null);
			return this.token;
		}

		// the current token we are calculating
		Token currentToken;

		// check if #
		if (data[this.currentIndex] == '#') {
			currentToken = new Token(TokenType.SYMBOL, data[this.currentIndex++]);
		}
		// all other characters are part of WORD in extended state
		else {
			// A string builder that build the value of the current token by appending
			// characters from the data
			StringBuilder currentValue = new StringBuilder();

			// add all elements except whitespaces and #
			addWhileTrue(currentValue, (element) -> !Character.isWhitespace(element) && element != '#');

			// create token
			currentToken = new Token(TokenType.WORD, currentValue.toString());
		}
		// change last found token
		this.token = currentToken;

		// return token
		return currentToken;
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
	 * A method that helps to add all the characters while the predicate returns
	 * true
	 */
	private void addWhileTrue(StringBuilder value, Predicate<Character> predicate) {
		while (this.currentIndex < data.length && predicate.test(data[this.currentIndex])) {
			value.append(data[this.currentIndex]);
			this.currentIndex++;
		}
	}

	/**
	 * A method that returns the last made token
	 * 
	 * @return last token
	 */
	public Token getToken() {
		return this.token;
	}

	/** A method that changes the lexer state to the given state */
	public void setState(LexerState state) {
		if (state == null)
			throw new NullPointerException();
		this.state = state;
	}
}
