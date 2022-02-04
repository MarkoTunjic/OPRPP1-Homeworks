package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * This is a class that simulates an "infinite" capacity array by dynamically
 * doubling the size on overflow
 *
 * @author Marko Tunjić
 */
public class ArrayIndexedCollection<T> implements List<T> {
	/** A constant that represents the default initial array size */
	private static final int DEFAULT_CAPACITY = 16;

	/**
	 * A private attribute that represents the number of elements inside our
	 * collection
	 */
	private int size;

	/** A private Attribute that represents the "infinite" capacity array */
	private Object[] elements;

	/**
	 * An attribute that represents the number of times this collection has been
	 * modified (elements added or removed)
	 */
	private long modificationCount;

	/** A default constructor that allocates an empty array with default capacity */
	public ArrayIndexedCollection() {
		this.size = 0;
		this.elements = new Object[DEFAULT_CAPACITY];
		this.modificationCount = 0;
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
		this.modificationCount = 0;
	}

	/**
	 * A constructor that creates a copy of the given collection. If the given
	 * collection is null a {@link NullPointerException} is thrown
	 *
	 * @param other the collection we want to copy into this collection
	 * @throws NullPointerException if the other collection is null
	 */
	public ArrayIndexedCollection(Collection<? extends T> other) {
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
	public ArrayIndexedCollection(Collection<? extends T> other, int initialCapacity) {
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
	public void add(T value) {
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

		// increase the modification count
		modificationCount++;
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
	 * A method that fills the given array with the parametrized values of this
	 * collection if there is not enough space a new array is created. Throws a
	 * NullPointerException if null array was given
	 *
	 * @param array the array that is used to check the type of the elements
	 * @return the parametrized array representation of this collection
	 * @throws NullPointerException if null array was given
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T[] toArray(T[] array) {
		if (array == null)
			throw new NullPointerException();

		// if too small allocate new array and copy elements
		if (array.length < this.size) {
			array = (T[]) Arrays.copyOf(this.elements, size, array.getClass());
			System.arraycopy(this.elements, 0, array, 0, size());
			return array;
		}

		// copy elements
		System.arraycopy(this.elements, 0, array, 0, this.size);

		// if too big set the element at index=size on null
		if (array.length > this.size)
			array[this.size] = null;
		return array;
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

		// increase the modification count
		modificationCount++;
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
	@Override
	public void insert(T value, int index) {
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

		// increase the modification count
		modificationCount++;
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
	@Override
	@SuppressWarnings("unchecked")
	public T get(int index) {
		if (index < 0 || index > this.size - 1)
			throw new IndexOutOfBoundsException();
		return (T) this.elements[index];
	}

	/**
	 * A method that returns the index inside the collection of the given object or
	 * -1 if not found. Objects are compared with the equals method.
	 *
	 * @param value the object which is searched for
	 * @return the index of the object if found else -1
	 */
	@Override
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
	@Override
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

		// increase the modification count
		modificationCount++;
	}

	/**
	 * A method that returns a ElemetsGetter that returns elements one by one from
	 * the beginning
	 *
	 * @return the ElementsGetter
	 */
	@Override
	public ElementsGetter<T> createElementsGetter() {
		return new ArrayIndexedElementsGetter<T>(this);
	}

	/**
	 * A class that implements the ElementsGetter interface and overrides the
	 * necessary methods so that they function specifically for the
	 * ArrayIndexedCollection
	 *
	 * @author Marko Tunjić
	 */
	private static class ArrayIndexedElementsGetter<T> implements ElementsGetter<T> {
		/**
		 * A reference to the ArrayIndexedCollection which elements have to be returned
		 */
		private ArrayIndexedCollection<T> collection;

		/**
		 * An attribute that represents the current index from which the next element
		 * has to be returned
		 */
		private int current;

		/**
		 * An attribute that contains the number of collection modification at the time
		 * of creation
		 */
		private long savedModificationCount;

		/**
		 * A constructor that receives an array of objects representing the elements
		 * that have to be returned and connects it to the elements of this class and
		 * sets the current element index to 0
		 *
		 * @param elements the array from the ArrayIndexedCollection
		 */
		public ArrayIndexedElementsGetter(ArrayIndexedCollection<T> collection) {
			this.collection = collection;
			this.current = 0;
			this.savedModificationCount = collection.modificationCount;
		}

		/**
		 * A method that returns the next object from the ArrayIndexedCollection and
		 * throws NoSuchElementException if the current element is null
		 *
		 * @return the next object
		 * @throws NoSuchElementException if the element to be returned is null
		 */
		@Override
		@SuppressWarnings("unchecked")
		public T getNextElement() {
			if (this.hasNextElement())
				return (T) this.collection.elements[current++];
			else
				throw new NoSuchElementException();
		}

		/**
		 * A method that check if there are more elements to return
		 *
		 * @return true if there are more elements and false otherwise
		 */
		@Override
		public boolean hasNextElement() {
			if (this.savedModificationCount != collection.modificationCount)
				throw new ConcurrentModificationException();
			return this.current < this.collection.size();
		}
	}
}