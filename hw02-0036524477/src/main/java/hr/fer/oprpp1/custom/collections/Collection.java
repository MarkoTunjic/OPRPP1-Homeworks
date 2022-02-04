package hr.fer.oprpp1.custom.collections;

/**
 * A interface that represents a collection of objects
 * 
 * @author Marko Tunjić
 */
public interface Collection {

	/**
	 * A method that checks if the collection is empty by checking its size
	 * 
	 * @return true if collection is empty false otherwise
	 */
	default boolean isEmpty() {
		return this.size() == 0;
	}

	/** A method that returns the number of objects inside the collection */
	int size();

	/**
	 * A method that adds objects inside the collection
	 * 
	 * @param value the object we want to add
	 */
	void add(Object value);

	/**
	 * A method that check if the collection contains an object
	 * 
	 * @param value of the object that is searched for
	 * @return true if the collection contains the object and false otherwise
	 */
	boolean contains(Object value);

	/**
	 * A method that removes the given object from the collection
	 * 
	 * @param value the object that is to be removed
	 * @return true if the object was inside the collection false otherwise
	 */
	boolean remove(Object value);

	/**
	 * A method that creates an array of objects from the collection
	 * 
	 * @return the array of objects inside the collection
	 */
	Object[] toArray();

	/**
	 * A method that calls the process method of the given processor on each element
	 * of this collection
	 * 
	 * @param processor the processor with which we want to process the elements of
	 *                  the collection
	 */
	default void forEach(Processor processor) {
		ElementsGetter eg = this.createElementsGetter();
		while (eg.hasNextElement()) {
			processor.process(eg.getNextElement());
		}
	}

	/**
	 * A method that adds all elements from the given collection into this
	 * collection by calling the forEach method with a processor that calls the
	 * method add.
	 * 
	 * @param other the collection we want to copy
	 * @throws NullPointerException if adding null element
	 */
	default void addAll(Collection other) {
		other.forEach((value) -> this.add(value));
	}

	/** A method that clears the collection */
	void clear();

	/**
	 * A method that returns a elements getter for the collection
	 * 
	 * @return the elements getter
	 */
	ElementsGetter createElementsGetter();

	/**
	 * A method that adds all elements from the collection col that pass the testers
	 * test function to the current collection.
	 * 
	 * @param col    the collection from which the elements are added to the current
	 *               collection
	 * @param tester the tester that tests the objects
	 */
	default void addAllSatisfying(Collection col, Tester tester) {
		ElementsGetter eg = col.createElementsGetter();
		while (eg.hasNextElement()) {
			Object current = eg.getNextElement();
			if (tester.test(current))
				this.add(current);
		}
	}
}
