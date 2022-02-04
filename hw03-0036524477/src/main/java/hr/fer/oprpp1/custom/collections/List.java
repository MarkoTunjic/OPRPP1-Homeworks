package hr.fer.oprpp1.custom.collections;

public interface List<T> extends Collection<T> {
	/**
	 * A method that returns the element at the given index. If the index is
	 * negative or greater than size-1 a IndexOutOfBounds exception is thrown.
	 *
	 * @param index the index from which the element has to be returned
	 * @return the wanted object
	 * @throws IndexOutOfBoundsException if index invalid
	 */
	T get(int index);

	/**
	 * A method that inserts the given object into the collection on the specified
	 * index. If the index is negative or greater then the collection size a
	 * IndexOutOfBOundsException is thrown and if the object is null a
	 * NullPointerException is thrown.
	 *
	 * @param value the object to insert
	 * @param index the index at which to insert the object
	 *
	 * @throws IndexOutOfBoundsException if the index is negative or greater then
	 *                                   size
	 * @throws NullPointerException      if the given object is null
	 */
	void insert(T value, int position);

	/**
	 * A method that returns the index inside the collection of the given object or
	 * -1 if not found. Objects are compared with the equals method.
	 *
	 * @param value the object which is searched for
	 * @return the index of the object if found else -1
	 */
	int indexOf(Object value);

	/**
	 * A method that removes a element on the specified index. If the index is
	 * negative or greater than size-1 a IndexOutOfBounds exception is thrown.
	 *
	 * @param index the index on which a element is going to be removed
	 */
	void remove(int index);
}
