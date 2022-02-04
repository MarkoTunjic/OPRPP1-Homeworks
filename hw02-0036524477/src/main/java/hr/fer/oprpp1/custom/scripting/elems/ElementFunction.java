package hr.fer.oprpp1.custom.scripting.elems;

public class ElementFunction extends Element {
	/** An attribute that represents the name of the read function */
	private String name;

	/**
	 * A constructor that assign the given value to the name and throws
	 * NullPointerException if null was given
	 * 
	 * @throws NullPointerException if null was given
	 */
	public ElementFunction(String name) {
		if (name == null)
			throw new NullPointerException();
		this.name = name;
	}

	/**
	 * A method that returns a String representation of the read function
	 * 
	 * @return the string representation of the read variable
	 */
	@Override
	public String asText() {
		return this.name;
	}
}
