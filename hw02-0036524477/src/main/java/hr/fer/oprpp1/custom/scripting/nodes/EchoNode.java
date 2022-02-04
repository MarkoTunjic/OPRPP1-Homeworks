package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;

/**
 * A class that helps us organize a document by splitting it into different
 * parts. This one represents a echo tag
 * 
 * @author Marko Tunjić
 */
public class EchoNode extends Node {
	/** An attribute that contains all of the things to echo */
	private Element[] elements;

	/**
	 * A constructor that assign the given array to the elements to echo and throws
	 * a NullPointerException if null was given
	 * 
	 * @throws NullPointerException if null was given
	 */
	public EchoNode(Element[] elements) {
		if (elements == null)
			throw new NullPointerException();
		this.elements = elements;
	}

	/**
	 * A getter method for the elements attribute
	 * 
	 * @return the elements to be echoed
	 */
	public Element[] getElements() {
		return this.elements;
	}

	/**
	 * A method that return the string representation of a EchoNode
	 * 
	 * @return String representation of a echo node
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{$").append("= ");
		for (int i = 0, j = this.elements.length; i < j; i++) {
			sb.append(this.elements[i].asText()).append(" ");
		}
		sb.append("$}");
		return sb.toString();
	}
}
