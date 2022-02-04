package hr.fer.oprpp1.custom.scripting.nodes;

/**
 * A class that helps us organize a document by splitting it into different
 * parts. This one represents a text
 * 
 * @author Marko Tunjić
 */
public class TextNode extends Node {
	/** The text that has been read */
	private String text;

	/**
	 * A constructor that assign the given string to the text and throws a
	 * NullPointerException if null was given
	 * 
	 * @throws NullPointerException if null was given
	 */
	public TextNode(String text) {
		this.text = text;
	}

	/**
	 * A getter method for the text attribute
	 * 
	 * @return the text attribute
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * A method that returns the string representation of a text node
	 * 
	 * @return string representation of a text node
	 */
	@Override
	public String toString() {
		return this.getText();
	}
}
