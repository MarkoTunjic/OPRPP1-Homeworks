package hr.fer.oprpp1.custom.scripting.parser;

/**
 * A exception that is being used when another exception during parsing in the
 * SmartScripParser class appears
 * 
 * @author Marko Tunjić
 */
public class SmartScriptParserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SmartScriptParserException() {
		super();
	}

	public SmartScriptParserException(String message) {
		super(message);
	}
}
