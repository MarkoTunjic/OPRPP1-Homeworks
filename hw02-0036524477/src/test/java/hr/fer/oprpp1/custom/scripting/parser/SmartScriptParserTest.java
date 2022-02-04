package hr.fer.oprpp1.custom.scripting.parser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import hr.fer.oprpp1.custom.scripting.nodes.EchoNode;

/**
 * A class that tests the smart script parser
 * 
 * @author Marko Tunjić
 */
class SmartScriptParserTest {

	/**
	 * A test method that test if we get an exception when Passing For Without End
	 * Tag
	 */
	@Test
	void testThrowingExceptionWhenPassingForWithoutEndTag() {
		assertThrows(SmartScriptParserException.class, () -> {
			SmartScriptParser parser = new SmartScriptParser("{$ for i n 1 2$}");
		});
	}

	/**
	 * A test method that tests if we are going to get a exception when Passing
	 * Symbol Inside Tag
	 */
	@Test
	void testThrowinExceptionWhenPassingSymbolInsideTag() {
		assertThrows(SmartScriptParserException.class, () -> {
			SmartScriptParser parser = new SmartScriptParser("{$ for i n , 2$}{$end$}");
		});
	}

	/**
	 * A test method that tests if we are going to get a exception when Passing
	 * invalid tag name
	 */
	@Test
	void testThrowingExceptionWhenPassingUnsupportedTagName() {
		assertThrows(SmartScriptParserException.class, () -> {
			SmartScriptParser parser = new SmartScriptParser("{$ p i n , 2$}{$end$}");
		});
	}

	/**
	 * A test method that tests if we are going to get a exception when Passing too
	 * many arguments in for
	 */
	@Test
	void testThrowingExceptionWhenPassingTooManyArgumentsInFor() {
		assertThrows(SmartScriptParserException.class, () -> {
			SmartScriptParser parser = new SmartScriptParser("{$ for i n 1 2 3$}{$end$}");
		});
	}

	/**
	 * A test method that tests if we are going to get a exception when Passing too
	 * few arguments in for
	 */
	@Test
	void testThrowingExceptionWhenPassingTooFewArgumentsInFor() {
		assertThrows(SmartScriptParserException.class, () -> {
			SmartScriptParser parser = new SmartScriptParser("{$ for i n $}{$end$}");
		});
	}

	/**
	 * A test method that tests if we are going to get a single node when passing
	 * valid for
	 */
	@Test
	void testThrowingExceptionWhenPassingEnoughArgumentsInFor() {
		SmartScriptParser parser = new SmartScriptParser("{$ for i n 3.2 $}{$end$}");
		assertEquals(parser.getDocumentNode().numberOfChildren(), 1);
	}

	/**
	 * A test method that tests if we are going to get a exception when Passing a
	 * invalid decimal number
	 */
	@Test
	void testThrowingExceptionWhenPassingInvalidDecimalNumber() {
		assertThrows(SmartScriptParserException.class, () -> {
			SmartScriptParser parser = new SmartScriptParser("{$ for i n 1.22.3$}{$end$}");
		});
	}

	/**
	 * A test method that tests if we are going to get a exception when Passing a
	 * string as first parameter in for loop
	 */
	@Test
	void testThrowingExceptionWhenPassingInvalidFirstForLoopArgument() {
		assertThrows(SmartScriptParserException.class, () -> {
			SmartScriptParser parser = new SmartScriptParser("{$ for \"i\" n 1.22.3$}{$end$}");
		});
	}

	/** A test method that test if the echo node contains all the given arguments */
	@Test
	void testIfEchoNodeContainsAllGivenArguments() {
		SmartScriptParser parser = new SmartScriptParser("{$ = i n 1.22 3 \"123\" $}");
		assertEquals(5, ((EchoNode) (parser.getDocumentNode().getChild(0))).getElements().length);
	}

	/**
	 * A test method that test if we get an exception when we pass null as string
	 */
	@Test
	void testConstructorThrowingExceptionWhenPassingNull() {
		assertThrows(SmartScriptParserException.class, () -> {
			SmartScriptParser parser = new SmartScriptParser(null);
		});

	}

}
