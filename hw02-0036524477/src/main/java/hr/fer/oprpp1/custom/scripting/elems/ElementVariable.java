package hr.fer.oprpp1.custom.scripting.elems;

/**
 * A class that represents a variable that has been read in a document
 * 
 * @author Marko Tunjić
 */
public class ElementVariable extends Element {
	/** An attribute that represents the name of the read variable */
	private String name;

	/**
	 * A constructor that assign the given value to the name and throws
	 * NullPointerException if null was given
	 * 
	 * @throws NullPointerException if null was given
	 */
	public ElementVariable(String name) {
		if (name == null)
			throw new NullPointerException();
		this.name = name;
	}

	/**
	 * A method that returns a String representation of the read variable
	 * 
	 * @return the string representation of the read variable
	 */
	@Override
	public String asText() {
		return this.name;
	}
}
