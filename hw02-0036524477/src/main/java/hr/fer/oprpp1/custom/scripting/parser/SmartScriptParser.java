package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.ObjectStack;
import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementConstantDouble;
import hr.fer.oprpp1.custom.scripting.elems.ElementConstantInteger;
import hr.fer.oprpp1.custom.scripting.elems.ElementFunction;
import hr.fer.oprpp1.custom.scripting.elems.ElementOperator;
import hr.fer.oprpp1.custom.scripting.elems.ElementString;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;
import hr.fer.oprpp1.custom.scripting.lexer.Lexer;
import hr.fer.oprpp1.custom.scripting.lexer.LexerState;
import hr.fer.oprpp1.custom.scripting.lexer.Token;
import hr.fer.oprpp1.custom.scripting.lexer.TokenType;
import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.nodes.EchoNode;
import hr.fer.oprpp1.custom.scripting.nodes.ForLoopNode;
import hr.fer.oprpp1.custom.scripting.nodes.Node;
import hr.fer.oprpp1.custom.scripting.nodes.TextNode;

/**
 * A class that parses a document by specific rules
 *
 * @author Marko Tunjić
 */
public class SmartScriptParser {
	/** A private attribute that contains the reference to the document node */
	private DocumentNode documentNode;

	/**
	 * A private attribute that contains the reference to the lexer dedicated for
	 * parsing the given text
	 */
	private Lexer lexer;

	/**
	 * A constructor that receives a text to parse. It creates a lexer with the
	 * given string and a document node to start parsing. If an error occured during
	 * parsing a SmartScriptParserException is thrown
	 *
	 * @param text the text to be parsed
	 *
	 * @throws SmartScriptParserException in case of any exception during parsing
	 */
	public SmartScriptParser(String text) {
		documentNode = new DocumentNode();
		try {
			lexer = new Lexer(text);
			parse();
		} catch (Exception ex) {
			throw new SmartScriptParserException(ex.getMessage());
		}
	}

	/**
	 * A method that simulates a documentParser by receiving tokens from a lexer and
	 * building the document tree by specific rules. Throws a
	 * SmartScriptParserException in case of any exception
	 *
	 * @throws SmartScriptParser in case of any exception
	 */
	private void parse() {
		// create the stack that helps us to create the document tree
		ObjectStack stack = new ObjectStack();
		stack.push(this.documentNode);

		// the tokens we receive from the lexer
		Token nextToken;

		// the current state of the lexer
		LexerState currentState = LexerState.TEXT;

		// the tag properties for example the for loop has 4
		ArrayIndexedCollection forLoopTagProperties = null;

		// the tag properties for example the echo has unlimited
		ArrayIndexedCollection echoProperties = null;

		// read the whole document
		while ((nextToken = lexer.nextToken()).getType() != TokenType.EOF) {
			// if the current state is text and we come to a opening tag we switch state to
			// searching for tag name
			if (currentState == LexerState.TEXT && nextToken.getType() == TokenType.OPEN_TAG) {
				lexer.setState(LexerState.TAG);
				currentState = LexerState.TAG;
			}
			// if we are in tag state and find our tag name we switch state to inside tag
			else if (currentState == LexerState.TAG && nextToken.getType() == TokenType.TAG_NAME) {
				lexer.setState(LexerState.INSIDE_TAG);
				currentState = LexerState.INSIDE_TAG;

				// if the tag was a for tag create for loop properties
				if (nextToken.getValue().toString().toUpperCase().equals("FOR"))
					forLoopTagProperties = new ArrayIndexedCollection(4);
				else if (nextToken.getValue().toString().toUpperCase().equals("="))
					echoProperties = new ArrayIndexedCollection();
				else if (nextToken.getValue().toString().toUpperCase().equals("END"))
					stack.pop();
				else
					throw new SmartScriptParserException("Unsupported tag name");
			}
			// if we were inside a tag and find the closing tag we change the state to text
			// and add the new node to the children
			else if (currentState == LexerState.INSIDE_TAG && nextToken.getType() == TokenType.CLOSE_TAG) {
				lexer.setState(LexerState.TEXT);
				currentState = LexerState.TEXT;

				// if it was a for loop
				if (forLoopTagProperties != null) {

					// create a for loop node from forLoopTagProperties
					ForLoopNode forLoop = createForLoopNode(forLoopTagProperties);

					// add the for loop to children of the last node
					((Node) stack.peek()).addChildNode(forLoop);

					// push the for loop on the stack
					stack.push(forLoop);
					forLoopTagProperties = null;
				} else if (echoProperties != null) {
					EchoNode echoNode = new EchoNode(getEchoElements(echoProperties));

					// add the for loop to children of the last node
					((Node) stack.peek()).addChildNode(echoNode);
					echoProperties = null;
				}
			}
			// if we are inside a tag we got to get the tag properties if there are any
			else if (currentState == LexerState.INSIDE_TAG) {

				// if it is a for loop add the elements to the for loop
				if (forLoopTagProperties != null)
					fillForLoopProperties(forLoopTagProperties, nextToken);
				else if (echoProperties != null)
					fillEchoProperties(echoProperties, nextToken);
			} else if (currentState == LexerState.TEXT && nextToken.getType() == TokenType.TEXT)
				((Node) stack.peek()).addChildNode(new TextNode(nextToken.getValue().toString()));
			else
				throw new SmartScriptParserException("Invalid token");
			// System.out.println(nextToken.getValue());
		}
		if (stack.size() != 1)
			throw new SmartScriptParserException("1 or more END tags missing");
	}

	/**
	 * A method that helps us create an array of elements for the echo node
	 *
	 * @param echoProperties the properties we want to convert into an array
	 * @return the array of elements to be echoed
	 */
	private Element[] getEchoElements(ArrayIndexedCollection echoProperties) {
		Element[] elements = new Element[echoProperties.size()];
		for (int i = 0, h = echoProperties.size(); i < h; i++)
			elements[i] = (Element) echoProperties.get(i);
		return elements;
	}

	/**
	 * A function that helps us fill the echo properties. Throws a
	 * SmartScriptParserException if invalid token type was given.
	 *
	 * @param echoProperties the properties we want to fill
	 * @param nextToken      the token we want to add to the properties
	 *
	 * @throws SmartScriptParserException in case of invalid token type
	 */
	private void fillEchoProperties(ArrayIndexedCollection echoProperties, Token nextToken) {
		switch (nextToken.getType()) {
		case CONSTANT_DOUBLE:
			echoProperties.add(new ElementConstantDouble((double) nextToken.getValue()));
			break;
		case CONSTANT_INTEGER:
			echoProperties.add(new ElementConstantInteger((int) nextToken.getValue()));
			break;
		case FUNCTION:
			echoProperties.add(new ElementFunction(nextToken.getValue().toString()));
			break;
		case OPERATOR:
			echoProperties.add(new ElementOperator(nextToken.getValue().toString()));
			break;
		case STRING:
			echoProperties.add(new ElementString(nextToken.getValue().toString()));
			break;
		case VARIABLE:
			echoProperties.add(new ElementVariable(nextToken.getValue().toString()));
			break;
		default:
			throw new SmartScriptParserException("Invalid FOR loop argument type");
		}
	}

	/**
	 * A method that helps us create a forLoopNode from the forLoopProperties an
	 * throws a SmartScriptParserException in case of invalid properties
	 *
	 * @param forLoopTagProperties the properties from which we want to create the
	 *                             for loop
	 * @return the ForLoopNode created from the given properties
	 *
	 * @throws SmartScriptParserException in case of invalid properties
	 */
	private ForLoopNode createForLoopNode(ArrayIndexedCollection forLoopTagProperties) {

		// check if for loop was ok and create it
		ForLoopNode forLoop;

		if (forLoopTagProperties.size() == 4) {
			forLoop = new ForLoopNode((ElementVariable) forLoopTagProperties.get(0),
					(Element) forLoopTagProperties.get(1), (Element) forLoopTagProperties.get(2),
					(Element) forLoopTagProperties.get(3));
		} else if (forLoopTagProperties.size() == 3) {
			forLoop = new ForLoopNode((ElementVariable) forLoopTagProperties.get(0),
					(Element) forLoopTagProperties.get(1), (Element) forLoopTagProperties.get(2), null);
		} else if (forLoopTagProperties.size() > 4)
			throw new SmartScriptParserException("Too many arguments in for loop");
		else
			throw new SmartScriptParserException("Too few arguments in for loop");
		return forLoop;
	}

	/**
	 * A method that helps to fill up the properties of a for loop tag. Throws a
	 * SmartScriptParserException in case of illegal arguments
	 *
	 * @param forLoopTagProperties the properties which we want to fill
	 * @param nextToken            the token we want to add
	 *
	 * @throws SmartScriptParserException in case of illegal arguments
	 */
	private void fillForLoopProperties(ArrayIndexedCollection forLoopTagProperties, Token nextToken) {
		// first property has to be a variable
		if (forLoopTagProperties.size() == 0) {
			if (nextToken.getType() == TokenType.VARIABLE)
				forLoopTagProperties.add(new ElementVariable(nextToken.getValue().toString()));
			else
				throw new SmartScriptParserException("First argument in a for loop must be a variable");
		}
		// maximum 4 properties of type number string or variable
		else if (forLoopTagProperties.size() < 4) {
			switch (nextToken.getType()) {
			case CONSTANT_DOUBLE:
				forLoopTagProperties.add(new ElementConstantDouble((double) nextToken.getValue()));
				break;
			case CONSTANT_INTEGER:
				forLoopTagProperties.add(new ElementConstantInteger((int) nextToken.getValue()));
				break;
			case STRING:
				forLoopTagProperties.add(new ElementString(nextToken.getValue().toString()));
				break;
			case VARIABLE:
				forLoopTagProperties.add(new ElementVariable(nextToken.getValue().toString()));
				break;
			default:
				throw new SmartScriptParserException("Invalid for loop argument");
			}
		} else
			throw new SmartScriptParserException("Too many arguments in for loop");
	}

	/**
	 * A getter for the documentNode
	 *
	 * @return the document node
	 */
	public DocumentNode getDocumentNode() {
		return this.documentNode;
	}
}
