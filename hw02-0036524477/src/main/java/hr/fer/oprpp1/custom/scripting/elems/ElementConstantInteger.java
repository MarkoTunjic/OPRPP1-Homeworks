package hr.fer.oprpp1.custom.scripting.elems;

/**
 * A class that represents a integer read from a document
 * 
 * @author Marko Tunjić
 */
public class ElementConstantInteger extends Element {
	/** An attribute that contains the value of the read element */
	private int value;

	/**
	 * A constructor that assign the given value to the integer value of the element
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}

	/** A method that returns the string representation of this integer element */
	@Override
	public String asText() {
		return String.valueOf(this.value);
	}
}
