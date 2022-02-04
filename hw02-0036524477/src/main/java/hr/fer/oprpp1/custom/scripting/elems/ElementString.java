package hr.fer.oprpp1.custom.scripting.elems;

/**
 * A class that represents a string that has been read from a document
 * 
 * @author Marko Tunjić
 */
public class ElementString extends Element {
	/** An attribute that represents the string that has been read */
	private String value;

	/**
	 * A constructor that assign the given value to the string and throws
	 * NullPointerException if null was given
	 * 
	 * @throws NullPointerException if null was given
	 */
	public ElementString(String value) {
		if (value == null)
			throw new NullPointerException();
		this.value = value;
	}

	/**
	 * A method that returns the string representation of the string that has been
	 * read
	 * 
	 * @return the string representation of the read string
	 */
	@Override
	public String asText() {
		return this.value;
	}
}
