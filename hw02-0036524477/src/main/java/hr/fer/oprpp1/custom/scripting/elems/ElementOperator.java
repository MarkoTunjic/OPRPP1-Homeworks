package hr.fer.oprpp1.custom.scripting.elems;

/**
 * A class that represents a operator that has been read in a document
 * 
 * @author Marko Tunjić
 */
public class ElementOperator extends Element {
	/** An attribute that represents the read operator */
	private String symbol;

	/**
	 * A constructor that assigns the given value to the symbol and throws a
	 * NullPointerException if null was given
	 * 
	 * @throws NullPointerException if null is given
	 */
	public ElementOperator(String symbol) {
		if (symbol == null)
			throw new NullPointerException();
		this.symbol = symbol;
	}

	/**
	 * A method that return the String representation of the operator
	 * 
	 * @return the string representation of the operator
	 */
	@Override
	public String asText() {
		return this.symbol;
	};
}
