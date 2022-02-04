package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;

/**
 * A class that helps us organize a document by splitting it into different
 * parts. This one represents the root node f every other node
 * 
 * @author Marko Tunjić
 */
public class Node {
	/** A collection that contains all of the nodes children */
	private ArrayIndexedCollection children;

	/**
	 * A method that adds a child and creates the children collection if the node
	 * was empty previously. If a null object is given a NullPointerException is
	 * thrown
	 * 
	 * @param child the node to be added
	 * 
	 * @throws NullPointerException if null was given
	 */
	public void addChildNode(Node child) {
		if (child == null)
			throw new NullPointerException();
		if (this.children == null)
			this.children = new ArrayIndexedCollection();
		this.children.add(child);
	}

	/**
	 * A method that return the number of child nodes
	 * 
	 * @return number of child nodes
	 */
	public int numberOfChildren() {
		if (this.children == null)
			return 0;
		else
			return this.children.size();
	}

	/**
	 * A method that return a child at a specific index and throws a
	 * IndexOutOfBoundsException if an invalid index was given
	 * 
	 * @param index the index from which the child node has to be returned
	 * @return the child node at the given index
	 * @throws IndexOutOfBoundsException if an invalid index was given
	 */
	public Node getChild(int index) {
		return (Node) this.children.get(index);
	}
	
}
