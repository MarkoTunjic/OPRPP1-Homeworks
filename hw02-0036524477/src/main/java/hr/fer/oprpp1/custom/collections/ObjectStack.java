package hr.fer.oprpp1.custom.collections;

/**
 * A class that uses the ArrayIndexedCollection to simulate a stack
 * 
 * @author Marko Tunjić
 */
public class ObjectStack {
	/**
	 * A private attribute that contains the ArrayIndexedCollection that simulates a
	 * stack
	 */
	private ArrayIndexedCollection stack;

	/** A default constructor that creates a empty stack */
	public ObjectStack() {
		this.stack = new ArrayIndexedCollection();
	}

	/**
	 * A method that checks if the stack is empty by calling the isEmpty method of
	 * the ArrayIndexedCollection
	 * 
	 * @return true if the stack is empty false otherwise
	 */
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	/**
	 * A method that returns the number of elements on the stack by calling the size
	 * method of the ArrayIndexedCollection
	 * 
	 * @return the number of elements on the stack
	 */
	public int size() {
		return stack.size();
	}

	/**
	 * A method that pushes an object on the stack by calling the add method of the
	 * ArrayIndexedCollection
	 * 
	 * @param value the object to be added
	 */
	public void push(Object value) {
		stack.add(value);
	}

	/**
	 * A method that removes and returns the last pushed object on the stack by
	 * calling the remove method of the ArrayIndexedCollection. If the stack is
	 * empty throws a EmptyStackException
	 * 
	 * @return the popped object
	 * @throws EmptyStackException if the stack is empty
	 */
	public Object pop() {
		if (this.isEmpty())
			throw new EmptyStackException();
		// save the last object
		Object element = stack.get(stack.size() - 1);

		// remove the last object
		stack.remove(stack.size() - 1);

		// return the save object
		return element;
	}

	/**
	 * A method that returns the last pushed object on the stack. If the stack is
	 * empty throws a EmptyStackException
	 * 
	 * @return the popped object
	 * @throws EmptyStackException if the stack is empty
	 */
	public Object peek() {
		if (this.isEmpty())
			throw new EmptyStackException();

		return stack.get(stack.size() - 1);
	}

	/**
	 * A method that clears the stack by calling the clear method of the
	 * ArrayIndexedCollection
	 */
	public void clear() {
		stack.clear();
	}
}
