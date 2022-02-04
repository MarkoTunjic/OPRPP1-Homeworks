package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;

/**
 * A class that helps us organize a document by splitting it into different
 * parts. This one represents a for loop tag
 * 
 * @author Marko Tunjić
 */
public class ForLoopNode extends Node {
	/** An attribute that represents the for loop variable */
	private ElementVariable variable;

	/** An attribute that represents the for loop begin value */
	private Element startExpression;

	/** An attribute that represents the for loop end value */
	private Element endExpression;

	/** An attribute that represents the for loop step value */
	private Element stepExpression;

	/**
	 * A constructor that assign the given values to the for loop elements and
	 * throws a NullPointerException if anything was null except the stepExpression
	 * value
	 * 
	 * @throws NullPointerException if anything was null except the stepExpression
	 *                              value
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression,
			Element stepExpression) {
		if (variable == null || startExpression == null || endExpression == null)
			throw new NullPointerException();

		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}

	/**
	 * A getter method for the for loop variable
	 * 
	 * @return the for loop variable
	 */
	public ElementVariable getVariable() {
		return variable;
	}

	/**
	 * A getter method for the for loop start expression
	 * 
	 * @return the for loop start expression
	 */
	public Element getStartExpression() {
		return startExpression;
	}

	/**
	 * A getter method for the for loop end expression
	 * 
	 * @return the for loop end expression
	 */
	public Element getEndExpression() {
		return endExpression;
	}

	/**
	 * A getter method for the for loop step expression
	 * 
	 * @return the for loop step expression
	 */
	public Element getStepExpression() {
		return stepExpression;
	}

	/**
	 * A method that returns the string representation of a for loop node
	 * 
	 * @return the string value of a for loop node
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{$ FOR ").append(variable.asText()).append(" ").append(startExpression.asText()).append(" ")
				.append(endExpression.asText()).append(" ")
				.append(stepExpression == null ? "" : stepExpression.asText() + " ").append("$}");
		for (int i = 0, j = this.numberOfChildren(); i < j; i++)
			sb.append(this.getChild(i).toString());
		sb.append("{$END$}");
		return sb.toString();
	}

}
