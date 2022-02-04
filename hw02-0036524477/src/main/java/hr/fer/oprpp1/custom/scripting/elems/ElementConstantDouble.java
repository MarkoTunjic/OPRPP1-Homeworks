package hr.fer.oprpp1.custom.scripting.elems;

/**
 * A class that represents a constant double value that has been read in a
 * document
 * 
 * @author Marko Tunjić
 */
public class ElementConstantDouble extends Element {
	/** A attribute that contains the value of the double that was read */
	private double value;

	/** A constructor that assign the given value to the attribute value */
	public ElementConstantDouble(double value) {
		this.value = value;
	}

	/**
	 * A method that return the string representation of a double element
	 * 
	 * @return string representation of a double value read from a document
	 */
	@Override
	public String asText() {
		return String.valueOf(this.value);
	}
}
