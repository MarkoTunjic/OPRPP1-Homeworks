package hr.fer.oprpp1.custom.scripting.nodes;

/**
 * A class that helps to organize a document by splitting it into different
 * parts this class represents the whole document
 * 
 * @author Marko Tunjić
 */
public class DocumentNode extends Node {
	
	/**A method that returns the string representation of a document*
	 * 
	 * @return the string value of the Document*/
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0, j = this.numberOfChildren(); i < j; i++) {
			sb.append(this.getChild(i).toString());
		}
		return sb.toString();
	}

}
