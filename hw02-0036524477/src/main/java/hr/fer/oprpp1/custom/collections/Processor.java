package hr.fer.oprpp1.custom.collections;

/**
 * A interface that processes data given to it
 *
 * @author Marko Tunjić
 */
public interface Processor {
	/**
	 * A function that processes the given object
	 *
	 * @param value the object that is to be processed
	 */
	void process(Object value);
}
