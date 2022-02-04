package hr.fer.oprpp1.custom.collections;

/**
 * A interface that is used to get elements one by one from a collection
 *
 * @author Marko Tunjić
 */
public interface ElementsGetter<T> {

	/**
	 * A method that returns the next element of a collection
	 *
	 * @return the next object
	 */
	T getNextElement();

	/**
	 * A method that returns true if there are more elements to return and false
	 * otherwise
	 *
	 * @return true if there are more object and false otherwise
	 */
	boolean hasNextElement();

	/**
	 * A method that calls the process method from the given processor on each
	 * reaming element in the collection
	 *
	 * @param p the processor that processes the remaining elements
	 */
	default void processRemaining(Processor<? super T> p) {
		while (this.hasNextElement()) {
			p.process(this.getNextElement());
		}
	}
}
