package hr.fer.oprpp1.custom.collections;

/**
 * This is a class that simulates an "infinite" capacity array by dynamically
 * doubling the size on overflow
 *
 * @author Marko Tunjić
 */
public class ArrayIndexedCollection extends Collection {
	/** A constant that represents the default initial array size */
	private static final int DEFAULT_CAPACITY = 16;

	/**
	 * A private attribute that represents the number of elements inside our
	 * collection
	 */
	private int size;

	/** A private Attribute that represents the "infinite" capacity array */
	private Object[] elements;

	/** A default constructor that allocates an empty array with default capacity */
	public ArrayIndexedCollection() {
		this.size = 0;
		this.elements = new Object[DEFAULT_CAPACITY];
	}

	/**
	 * A constructor that takes an initial capacity argument and allocates the array
	 * with that initial size. If the capacity is negative a
	 * IllegalArgumentException is thrown
	 *
	 * @param initialCapacity the initial capacity the array
	 * @throws IllegalArgumentException when initial capacity negative
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		if (initialCapacity < 1)
			throw new IllegalArgumentException();
		this.size = 0;
		this.elements = new Object[initialCapacity];
	}

	/**
	 * A constructor that creates a copy of the given collection. If the given
	 * collection is null a {@link NullPointerException} is thrown
	 *
	 * @param other the collection we want to copy into this collection
	 * @throws NullPointerException if the other collection is null
	 */
	public ArrayIndexedCollection(Collection other) {
		this();
		if (other == null)
			throw new NullPointerException();

		// if the size of the given collection is greater then DEFAULT_CAPPACITY
		// reallocate the array
		if (other.size() > this.elements.length) {
			this.elements = new Object[other.size()];
		}

		// copy the elements
		this.addAll(other);
	}

	/**
	 * A constructor that creates a collection with the given initial capacity and
	 * copies the elements from the other array. If the initial capacity is less
	 * then the other collections size we use the size to allocate the array. If a
	 * negative capacity is given a IllegalArgumentException is thrown and if the
	 * other collection is null a NullPointerException is thrown
	 *
	 * @param other           the collection we want to copy into this one
	 * @param initialCapacity the initial capacity we want the array to be
	 * @throws IllegalArgumentException if the initial capacity is negative
	 * @throws NullPointerException     if the other collection is null
	 */
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		this(initialCapacity);
		if (other == null)
			throw new NullPointerException();

		// if the size of the other array is greater then the initial capacity then
		// reallocate the array
		if (other.size() > initialCapacity) {
			this.elements = new Object[other.size()];
		}

		// copy the elements
		this.addAll(other);
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
	 * A method that adds a object to the end of our collection if the size would
	 * overflow the capacity then we double the capacity. Throws a
	 * NullPointerException if the object is null
	 *
	 * @param value the object we want to add
	 * @throws NullPointerException if the object is null
	 */
	@Override
	public void add(Object value) {
		if (value == null)
			throw new NullPointerException();

		// check for overflow
		if (this.size == this.elements.length) {
			// copy the elements into a temporary container
			Object[] helper = this.toArray();

			// reallocate the array
			this.elements = new Object[this.elements.length * 2];

			// copy the elements back
			System.arraycopy(helper, 0, this.elements, 0, helper.length);
		}

		// add the element to the end and increase the size by 1
		this.elements[this.size++] = value;
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
	 * A method that removes the given object from the collection and return true if
	 * the object was present false otherwise. It is okay to try and remove null.
	 *
	 * @param value the object we want to remove
	 * @return true if object was present otherwise false
	 */
	@Override
	public boolean remove(Object value) {
		int index = this.indexOf(value);

		// try to remove the object at a given index and catch exception if index was -1
		// (object was not found)
		try {
			this.remove(index);
		} catch (IndexOutOfBoundsException ex) {
			return false;
		}
		return true;
	}

	/**
	 * A method that converts this collection into a array of objects
	 *
	 * @return a array of all objects inside this collection
	 */
	@Override
	public Object[] toArray() {
		Object[] helper = new Object[this.size];
		System.arraycopy(this.elements, 0, helper, 0, this.size);
		return helper;
	}

	/**
	 * A method that calls the process method of the given processor on each element
	 * of this collection
	 *
	 * @param processor the processor with which we want to process the elements of
	 *                  the collection
	 */
	@Override
	public void forEach(Processor processor) {
		for (int i = 0; i < this.size; i++)
			processor.process(this.elements[i]);
	}

	/**
	 * A method that simulates clearing the collection but it actually sets every
	 * element to null and resets the size
	 */
	@Override
	public void clear() {
		for (int i = 0; i < this.size; i++) {
			this.elements[i] = null;
		}
		this.size = 0;
	}

	/**
	 * A method that inserts a object into the collection on the given index. If the
	 * object is null a NullPointerException is thrown and if the index is negative
	 * or greater then size a IndexOutOfBoundException is thrown
	 *
	 * @param value the object we want to insert
	 * @param the   index at which we want to insert the object
	 *
	 * @throws IndexOutOfBoundsException if the index is negative or greater than
	 *                                   size
	 * @throws NullPointerException      if the given value is null
	 */
	public void insert(Object value, int index) {
		if (index < 0 || index > this.size)
			throw new IndexOutOfBoundsException();
		if (value == null)
			throw new NullPointerException();

		// check for overflow
		if (this.size == this.elements.length) {
			// copy all of the elements into a temporary container
			Object[] helper = this.toArray();

			// reallocate the collection
			this.elements = new Object[this.elements.length * 2];

			// copy all the elements up to the index we want to insert our new value at
			System.arraycopy(helper, 0, this.elements, 0, index);

			// add the new value
			this.elements[index] = value;

			// copy the rest of the elements
			System.arraycopy(helper, index, this.elements, index + 1, helper.length - index);
		} else {
			// copy all of the elements up to the given index into a temporary container
			Object[] elementsToTheRight = new Object[this.size - index];
			System.arraycopy(this.elements, index, elementsToTheRight, 0, this.size - index);

			// add the new element
			this.elements[index] = value;

			// copy the rest of the elements
			System.arraycopy(elementsToTheRight, 0, this.elements, index + 1, elementsToTheRight.length);
		}

		// increase the size
		this.size++;
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
		return this.elements[index];
	}

	/**
	 * A method that returns the index inside the collection of the given object or
	 * -1 if not found. Objects are compared with the equals method.
	 *
	 * @param value the object which is searched for
	 * @return the index of the object if found else -1
	 */
	public int indexOf(Object value) {
		for (int i = 0; i < this.size; i++) {
			if (this.elements[i].equals(value))
				return i;
		}
		return -1;
	}

	/**
	 * A method that removes a object at the specified index by moving all of the
	 * object from the index=index+1 to the left. A IndexOutOfBounds exception is
	 * thrown if the index is negative or greater then size-1
	 */
	public void remove(int index) {
		if (index < 0 || index > this.size - 1)
			throw new IndexOutOfBoundsException();

		// move all of the elements from index+1 to the left by 1 place
		for (int i = index; i < this.size - 1; i++)
			this.elements[i] = this.elements[i + 1];

		// remove the last element
		this.elements[this.size] = null;

		// decrease size
		this.size--;
	}

}