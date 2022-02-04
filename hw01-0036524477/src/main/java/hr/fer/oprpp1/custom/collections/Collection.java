package hr.fer.oprpp1.custom.collections;

/**
 * A class that simulates an interface. The "class" represents a collection of
 * objects
 * 
 * @author Marko Tunjić
 */
public class Collection {

	/** A default constructor that does nothing because it simulates an Interface */
	protected Collection() {
	}

	/**
	 * A method that checks if the collection is empty by checking its size
	 * 
	 * @return true if collection is empty false otherwise
	 */
	public boolean isEmpty() {
		return this.size() == 0;
	}

	/** A method that returns the number of objects inside the collection */
	public int size() {
		return 0;
	}

	/**
	 * A method that adds objects inside the collection
	 * 
	 * @param value the object we want to add
	 */
	public void add(Object value) {
	}

	/**
	 * A method that check if the collection contains an object
	 * 
	 * @param value of the object that is searched for
	 * @return true if contains
	 * @return false otherwise
	 */
	public boolean contains(Object value) {
		return false;
	}

	/**
	 * A method that removes the given object from the collection
	 * 
	 * @param value the object that is to be removed
	 * @return true if the object was inside the collection false otherwise
	 */
	public boolean remove(Object value) {
		return false;
	}

	/**
	 * A method that creates an array of objects from the collection
	 * 
	 * @return the array of objects inside the collection
	 * @throws UnsupportedOperationException if not Overridden
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	/**
	 * A method that calls the process method of the given processor on each element
	 * of the collection
	 * 
	 * @param the processor that processes the objects
	 */
	public void forEach(Processor processor) {
	}

	/**
	 * A method that adds all elements from the given collection into this
	 * collection by calling the forEach method with a processor that calls the
	 * method add.
	 * 
	 * @param other the collection we want to copy
	 */
	public void addAll(Collection other) {

		class myProcessor extends Processor {
			@Override
			public void process(Object value) {
				add(value);
			}
		}
		other.forEach(new myProcessor());
	}

	/** A method that clears the collection */
	public void clear() {
	}
}
