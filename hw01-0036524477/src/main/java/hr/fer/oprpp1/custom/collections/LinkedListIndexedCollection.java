package hr.fer.oprpp1.custom.collections;

/**
 * A class that simulates a linked list
 *
 * @author Marko Tunjić
 */
public class LinkedListIndexedCollection extends Collection {

	/**
	 * A static class that defines a linked list element
	 *
	 * @author Marko Tunjić
	 */
	private static class ListNode {
		/** A private attribute that contains a pointer to the previous element */
		private ListNode previous;

		/** A private attribute that contains a pointer to the next element */
		private ListNode next;
		/** A private attribute that contains the data of the list element */
		private Object data;
	}

	/**
	 * A private attribute that represents the number of elements inside the
	 * Collection
	 */
	private int size;

	/**
	 * A private attribute that contains a pointer to the first element of the
	 * collection
	 */
	private ListNode first;

	/**
	 * A private attribute that contains a pointer to the last element of the
	 * collection
	 */
	private ListNode last;

	/** A default constructor that creates an empty collection */
	public LinkedListIndexedCollection() {
		this.first = this.last = null;
		this.size = 0;
	}

	/**
	 * A constructor that creates a LinkedListIndexedCollection from the given
	 * collection by copying all of the elements
	 *
	 * @param collection the collection to be copied
	 */
	public LinkedListIndexedCollection(Collection collection) {
		this.addAll(collection);
		this.size = collection.size();
	}

	/**
	 * A method that adds an object to the end of this collection by changing the
	 * previous and next pointers of specific elements. If the object is null a
	 * NullPointerException is thrown
	 *
	 * @param value the object to add
	 * @throws NullPointerException if the object is null
	 */
	@Override
	public void add(Object value) {
		if (value == null)
			throw new NullPointerException();

		// create a new ListNode with the given value
		ListNode element = new ListNode();
		element.data = value;

		// check if collection is empty
		if (this.size == 0) {
			// change the first element pointer
			this.first = element;
		} else {
			// change the previous ListNode pointer of the element
			element.previous = this.last;

			// change the next ListNode pointer of the last element
			this.last.next = element;
		}

		// change the last element pointer
		this.last = element;

		// increase size
		this.size++;
	}

	/**
	 * A method that clears the collection by binding the first and last pointer to
	 * null and by resetting size
	 */
	@Override
	public void clear() {
		this.size = 0;
		this.first = null;
		this.last = null;
	}

	/**
	 * A method that calls the process method of the given processor on each element
	 * of the collection
	 *
	 * @param processor the processor that will process the elements
	 */
	@Override
	public void forEach(Processor processor) {
		for (ListNode iterator = this.first; iterator != null; iterator = iterator.next)
			processor.process(iterator.data);
	}

	/**
	 * A method that removes the given object from the collection and return true if
	 * the object was present false otherwise. It is okay to try and remove null.
	 *
	 * @param value the object we want to remove
	 * @return true if object was present otherwise false
	 */
	@Override
	public boolean remove(Object value) {
		if (value == null)
			throw new NullPointerException();
		int index = this.indexOf(value);

		// try to remove the object at a given index and catch exception if index was -1
		// (object was not found)
		try {
			this.remove(index);
			return true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	/**
	 * A method that returns the number of elements inside the collection
	 *
	 * @return the number of elements inside the collection
	 */
	@Override
	public int size() {
		return this.size;
	}

	/**
	 * A method that converts this collection into a array of objects
	 *
	 * @return an array of all objects inside this collection
	 */
	@Override
	public Object[] toArray() {
		Object[] array = new Object[this.size];
		int i = 0;
		for (ListNode iterator = this.first; iterator != null; iterator = iterator.next, i++)
			array[i] = iterator.data;

		return array;
	}

	/**
	 * A method that returns true if the collections contains a element and false if
	 * it doesn't. It is okay to ask if contains null.
	 *
	 * @param value the object that we want to check if it is in the collection
	 * @return true if the object is in the collection false otherwise
	 */
	@Override
	public boolean contains(Object value) {
		return this.indexOf(value) != -1;
	}

	/**
	 * A method that returns a object on the specified index. If the index is
	 * negative or beyond range a IndexOutOfBoundsException is thrown.
	 *
	 * @param index the index at from which we want to grab the object
	 * @return the object at the specified index
	 * @throws IndexOutOfBoundsException if the index is negative or greater then
	 *                                   size-1
	 */
	public Object get(int index) {
		if (index < 0 || index > this.size - 1)
			throw new IndexOutOfBoundsException();
		return loopToIndex(index).data;
	}

	/**
	 * A method that inserts the given objects into the collection on the specified
	 * index by changing specific ListNode pointers. If the index is negative or
	 * greater then the collection size a IndexOutOfBOundsException is thrown and if
	 * the object is null a NullPointerException is thrown.
	 *
	 * @param value the object to insert
	 * @param index the index at which to insert the object
	 *
	 * @throws IndexOutOfBoundsException if the index is negative or greater then
	 *                                   size
	 * @throws NullPointerException      if the given object is null
	 */
	public void insert(Object value, int index) {
		if (index < 0 || index > this.size)
			throw new IndexOutOfBoundsException();
		if (value == null)
			throw new NullPointerException();

		// create a new ListNode with the given value
		ListNode element = new ListNode();
		element.data = value;

		// loop to the index
		ListNode iterator = loopToIndex(index);

		// check if we have to change the first pointer (insertion at the beginning
		if (iterator == this.first) {
			// change the first pointer
			this.first = element;

			// add values to the new element pointers
			element.previous = iterator.previous;
			element.next = iterator;

			// add the element to the collection by connecting pointers
			iterator.previous = element;

		} // check if we have to change the last pointer insertion at the end
		else if (index == size) {
			// change last pointer
			this.last = element;

			// add values to the new element pointers
			element.previous = iterator;

			// add the element to the collection by connecting pointers
			iterator.next = element;
		} else {
			// add values to the new element pointers
			element.previous = iterator.previous;
			element.next = iterator;

			// add the element to the collection by connecting pointers
			iterator.previous.next = element;
			iterator.previous = element;
		}

		// increase the size
		this.size++;
	}

	/**
	 * A method that returns the index inside the collection of the given object or
	 * -1 if not found. Objects are compared with the equals method.
	 *
	 * @param value the object which is searched for
	 * @return the index of the object if found else -1
	 */
	public int indexOf(Object value) {
		int i = 0;
		for (ListNode iterator = this.first; iterator != null; iterator = iterator.next, i++) {
			if (iterator.data.equals(value))
				return i;
		}
		return -1;
	}

	/**
	 * A method that removes a element on the specified index by setting specific
	 * pointers to null. If the index is negative or greater than size-1 a
	 * IndexOutOfBounds exception is thrown.
	 *
	 * @param index the index on which a element is going to be removed
	 */
	public void remove(int index) {
		if (index < 0 || index > this.size - 1)
			throw new IndexOutOfBoundsException();

		// get to the index
		ListNode iterator = loopToIndex(index);

		// check if collection has only 1 element
		if (this.size == 1) {
			// empty the collection
			this.first = null;
			this.last = null;
		} // check if the first element has to be removed
		else if (iterator == this.first) {
			// change the first pointer
			this.first = iterator.next;

			// remove the element by changing ListNode pointers
			iterator.next.previous = iterator.previous;
		} // check if the last element has to be removed
		else if (iterator == this.last) {
			// change the last pointer
			this.last = iterator.previous;

			// remove the element by changing ListNode pointers
			iterator.previous.next = iterator.next;
		} else {
			// remove the element by changing ListNode pointers
			iterator.previous.next = iterator.next;
			iterator.next.previous = iterator.previous;
		}
		// for safety set pointers of the removed element to null
		iterator.next = null;
		iterator.previous = null;

		// reduce size
		this.size--;
	}

	/**
	 * A private method that returns a pointer to the element on a specified index
	 *
	 * @param index the index to which the method loops
	 * @return a pointer to the element
	 */
	private ListNode loopToIndex(int index) {
		ListNode iterator;
		// if the index is on the left half use loop from the beginning
		if (index <= this.size / 2) {
			iterator = this.first;
			for (int i = 0; i < index; i++, iterator = iterator.next)
				;
		} // if the index is on the right half loop from the end
		else {
			iterator = this.last;
			for (int i = this.size - 1; i > index; i--, iterator = iterator.previous)
				;
		}
		return iterator;
	}

}
