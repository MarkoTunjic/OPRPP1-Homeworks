package hr.fer.oprpp1.custom.collections;

/**
 * A interface that processes data given to it
 *
 * @author Marko Tunjić
 */
public interface Processor<T> {
	/**
	 * A function that processes the given object
	 *
	 * @param value the object that is to be processed
	 */
	public void process(T value);
}
