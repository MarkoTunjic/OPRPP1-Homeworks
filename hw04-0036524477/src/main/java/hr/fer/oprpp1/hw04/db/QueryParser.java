package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/**
 * A class taht parses a queryString
 *
 * @author Marko Tunjić
 */
public class QueryParser {
	/**
	 * A class that represents a lexer token
	 *
	 * @author Marko Tunjić
	 */
	private static class Token {
		/** A private attribute that represents the tokens type */
		private TokenType type;

		/** A private attribute that represents the tokens value */
		private String value;

		/**
		 * A constructor that creates a token from the given parameters
		 *
		 * @param type  the token type
		 * @param value the token value
		 */
		public Token(TokenType type, String value) {
			this.type = type;
			this.value = value;
		}
	}

	/**
	 * A class that completes the lexical analasys of a given string following
	 * special rules
	 *
	 * @author Marko Tunjić
	 */
	private static class QueryLexer {
		/** A provate attribute that contains all possible operators */
		private List<Character> operators = List.of('>', '<', '=', '!');

		/** A private attribute that contains the data to be analysed */
		private char[] data;

		/** the last returned token */
		private Token lastToken;

		/** the current character to be checked */
		private int currentIndex;

		/**
		 * A constructor that creates a lexer from the given parameters
		 *
		 * @param text the text to be lexically analysed
		 */
		public QueryLexer(String text) {
			if (text == null)
				throw new NullPointerException();
			data = text.toCharArray();
			currentIndex = 0;
		}

		/**
		 * A method that returns the next lexcial token
		 *
		 * @return the next lexciacal token
		 */
		public Token nextToken() {
			// check if the last token was EOF
			if (lastToken != null && lastToken.type == TokenType.EOF)
				throw new NoSuchElementException();

			// check if we came to the end
			if (currentIndex == data.length)
				return new Token(TokenType.EOF, "EOF");

			// skipp all whitespaces
			skipAllWhitespaces();

			// declare a new token
			Token a = null;

			// check if the first chracter is a letter a operator or a string

			// letter
			if (Character.isLetter(data[currentIndex])) {
				// create a string builder that whill build the token value
				StringBuilder currentTokenValueBuilder = new StringBuilder();

				// fill the string builder with letters
				addWhileTrue(currentTokenValueBuilder, c -> Character.isLetter(c));
				String currentTokenValue = currentTokenValueBuilder.toString();

				// check what we got in the builder
				// operator and
				if (currentTokenValue.equalsIgnoreCase("AND"))
					a = new Token(TokenType.AND, currentTokenValue);

				// query word
				else if (currentTokenValue.equals("query"))
					// skip
					a = new Token(TokenType.OTHER, currentTokenValue);

				// LIKE operator
				else if (currentTokenValue.equals("LIKE"))
					a = new Token(TokenType.OPERATOR, currentTokenValue);

				// a attribute
				else
					a = new Token(TokenType.ATTRIBUTE, currentTokenValue);

			}
			// string
			else if (data[currentIndex] == '\"') {
				// create a string builder that whill build the token value
				StringBuilder currentTokenValueBuilder = new StringBuilder();

				// skipp first "
				currentIndex++;

				// add everything inside the ""
				addWhileTrue(currentTokenValueBuilder, c -> c != '\"');

				// skip second "
				currentIndex++;

				a = new Token(TokenType.STRING_LITERAL, currentTokenValueBuilder.toString());
			}
			// operator
			else if (operators.contains(data[currentIndex])) {
				// check if NOT_EQUALS
				if (data[currentIndex] == '!' && data[currentIndex + 1] == '=') {
					currentIndex += 2;
					a = new Token(TokenType.OPERATOR, "!=");
				}
				// check if GREATER_OR_EQUALS
				else if (data[currentIndex] == '>' && data[currentIndex + 1] == '=') {
					currentIndex += 2;
					a = new Token(TokenType.OPERATOR, ">=");
				}
				// check if LESS_OR_EQUALS
				else if (data[currentIndex] == '<' && data[currentIndex + 1] == '=') {
					currentIndex += 2;
					a = new Token(TokenType.OPERATOR, "<=");
				}
				// else add the single character operator aka >,< or =
				else
					a = new Token(TokenType.OPERATOR, Character.toString(data[currentIndex++]));
			}
			// if we don't know what it is return other
			else
				a = new Token(TokenType.OTHER, Character.toString(data[currentIndex++]));

			// change the last token
			lastToken = a;

			// return token
			return a;
		}

		/**
		 * A method that adds characters into the string builder while the predicate
		 * says true
		 *
		 * @param currentTokenValue the string builder to fill up
		 * @param predicate         the predicate that says when to stop
		 */
		private void addWhileTrue(StringBuilder currentTokenValue, Predicate<Character> predicate) {
			while (currentIndex < data.length && predicate.test(data[currentIndex]))
				currentTokenValue.append(data[currentIndex++]);
		}

		/** A method that skips all whitespaces */
		private void skipAllWhitespaces() {
			while (Character.isWhitespace(data[currentIndex]))
				currentIndex++;
		}
	}

	/**
	 * A enum that contains all possbile token types
	 *
	 * @author Marko Tunjić
	 */
	private enum TokenType {
		OPERATOR, STRING_LITERAL, AND, ATTRIBUTE, OTHER, EOF
	}

	/**
	 * A class that represenent a query parser exception
	 *
	 * @author Marko Tunjić
	 */
	public static class QueryParserException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public QueryParserException(String text) {
			super(text);
		}
	}

	/** A private attribute that contains all of the expressions inside a query */
	private List<ConditionalExpression> query;

	/**
	 * A constructor that creates a query parser from the given parameters. Throws a
	 * null pointer exception if null was given
	 *
	 * @param queryString the string to be parsed
	 *
	 * @throws NullPointerException if null was given
	 */
	public QueryParser(String queryString) {
		// check for null
		if (queryString == null)
			throw new NullPointerException();

		// prepare a lexer and querys
		QueryLexer lexer = new QueryLexer(queryString);
		query = new ArrayList<>();

		// try parsing
		try {
			parse(lexer);
		}
		// if we tried to create a Conditional expression with too little arguments
		catch (NullPointerException e) {
			throw new QueryParserException(e.getMessage());
		}
		// if we had more than onestring literal or comparisonOperator or fieldgetter in
		// one expression
		catch (IllegalStateException e) {
			throw new QueryParserException(e.getMessage());
		}
		// if a illegal character was given or a illegal StudentRecord attribute was
		// requested
		catch (IllegalArgumentException e) {
			throw new QueryParserException(e.getMessage());
		}
	}

	/**
	 * A method that return true if this parsed query is a direct query
	 *
	 * @return true if direct query and false otherwise
	 */
	public boolean isDirectQuery() {
		ConditionalExpression firstQueryExpression = query.get(0);
		return query.size() == 1 && firstQueryExpression.getComparisonOperator().equals(ComparisonOperators.EQUALS)
				&& firstQueryExpression.getFieldGetter().equals(FieldValueGetters.JMBAG);
	}

	/**
	 * A method that returns the queried string of the direct query or throws a
	 * illegal state exception if not a direct query
	 *
	 * @return the queried JMBAG
	 *
	 * @throws IllegalStateException if not a direct query
	 */
	public String getQueriedJMBAG() {
		if (!isDirectQuery())
			throw new IllegalStateException();
		return query.get(0).getStringLiteral();
	}

	/**
	 * A method that returns a list of all conditional expression of the parsed
	 * query string
	 *
	 * @return a list of all conditional expression of the parsed query string
	 */
	public List<ConditionalExpression> getQuery() {
		return query;
	}

	/**
	 * A method that uses the given lexer to parse the string. Throws a
	 * {@link IllegalStateException} if a ConditionalExpression had more arguments
	 * than required or no query word, throws {@link NullPointerException} if a
	 * conditional exception had less arguments that required, throws
	 * {@link IllegalArgumentException} if a invalid character or unsupported
	 * {@link StudentRecord} attribute was given
	 *
	 * @param lexer the lexer used for lexical analasys
	 *
	 * @throws IllegalStateException    if a ConditionalExpression had more
	 *                                  arguments than required or no query word
	 * @throws NullPointerException     if a conditional exception had less
	 *                                  arguments than required
	 * @throws IllegalArgumentException if a invalid character or unsupported
	 *                                  {@link StudentRecord} attribute was given
	 */
	private void parse(QueryLexer lexer) {
		// the next token from the lexer
		Token nextToken;

		// the attributes we need to create a Conditional expression
		String stringLiteral = null;
		IComparisonOperator comparisonOperator = null;
		IFieldValueGetter fieldGetter = null;

		boolean foundQueryWord = false;

		// repeat to the end of the query string
		while ((nextToken = lexer.nextToken()).type != TokenType.EOF) {
			if (!foundQueryWord && nextToken.value.equals("query")) {
				foundQueryWord = true;
				continue;
			}

			if (!foundQueryWord)
				throw new IllegalStateException("Did not find query word");
			// check token type
			switch (nextToken.type) {
			case AND:
				// if the token was the add token create a new conditional expression and add it
				// to the querys
				query.add(new ConditionalExpression(comparisonOperator, stringLiteral, fieldGetter));

				// reset attributes for conditionla expression
				comparisonOperator = null;
				stringLiteral = null;
				fieldGetter = null;

				break;
			case ATTRIBUTE:
				// if we already added one field getter that means that we have two attributes
				// in one expression
				if (fieldGetter != null)
					throw new IllegalStateException("Two attributes in one expression");

				// check which getter we need
				if (nextToken.value.equals("jmbag"))
					fieldGetter = FieldValueGetters.JMBAG;
				else if (nextToken.value.equals("firstName"))
					fieldGetter = FieldValueGetters.FIRST_NAME;
				else if (nextToken.value.equals("lastName"))
					fieldGetter = FieldValueGetters.LAST_NAME;

				// if its neither firstName, nor lastName nor jmbag throw exception
				else
					throw new IllegalArgumentException("Illegal attribute was given");
				break;
			case OPERATOR:
				// if we already added one operator throw an exception
				if (comparisonOperator != null)
					throw new IllegalStateException("Two operators in one expression");

				// check which operator to get
				if (nextToken.value.equals(">="))
					comparisonOperator = ComparisonOperators.GREATER_OR_EQUALS;
				else if (nextToken.value.equals("<="))
					comparisonOperator = ComparisonOperators.LESS_OR_EQUALS;
				else if (nextToken.value.equals("<"))
					comparisonOperator = ComparisonOperators.LESS;
				else if (nextToken.value.equals(">"))
					comparisonOperator = ComparisonOperators.GREATER;
				else if (nextToken.value.equals("="))
					comparisonOperator = ComparisonOperators.EQUALS;
				else if (nextToken.value.equals("LIKE"))
					comparisonOperator = ComparisonOperators.LIKE;
				else
					comparisonOperator = ComparisonOperators.NOT_EQUALS;
				break;
			case OTHER:
				// if we got something we didn't recognize in the lexer throw an exception
				throw new IllegalArgumentException("Illegal character");
			case STRING_LITERAL:
				// if we already had one string literal in the lexer throw exception
				if (stringLiteral != null)
					throw new IllegalStateException("Two string literals in one expression");
				stringLiteral = nextToken.value;
				break;
			default:
				break;
			}
		}
		// add the last conditional to the querys becouse we don't have an AND at the
		// end
		query.add(new ConditionalExpression(comparisonOperator, stringLiteral, fieldGetter));
	}
}
