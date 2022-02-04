package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/** A class that contains additional tests for the ArrayIndexed collection */
class ArrayIndexedCollectionAdditionalTest {

	/**
	 * A test method that test the constructor when passing negative initial
	 * capacity
	 */
	@Test
	void testArrayIndexedCollectionInitialCapacityConstructorWhenPassingNegativeCapacity() {
		assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection(-1));
	}

	/**
	 * A test method that test the constructor when passing positive initial
	 * capacity
	 */
	@Test
	void testArrayIndexedCollectionInitialCapacityConstructorWhenPassingPositiveCapacity() {
		int capacity = 1;
		ArrayIndexedCollection collection = new ArrayIndexedCollection(capacity);
		assertEquals(0, collection.size());
	}

	/**
	 * A test method that test the constructor when passing positive initial
	 * capacity and a null collection
	 */
	@Test
	void testArrayIndexedCollectionOtherCollectionAndInitialCapacityConstructorWhenPassingNull() {
		assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null, 1));
	}

	/**
	 * A test method that test the constructor when passing negative initial
	 * capacity and a null collection
	 */
	@Test
	void testArrayIndexedCollectionOtherCollectionAndInitialCapacityConstructorWhenPassingNegativeCapacity() {
		assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection(null, -1));
	}

	/**
	 * A test method that test the constructor when passing positive initial
	 * capacity and a collection that has more elements than the initial capacity
	 */
	@Test
	void testArrayIndexedCollectionOtherCollectionAndInitialCapacityConstructorWhenPassingCollectionWithGreaterSizeThanInitialCapacity() {
		int collectionCapacity = 2;
		int initialCapacity = collectionCapacity - 1;
		Collection collection = new ArrayIndexedCollection(collectionCapacity);
		for (int i = 0; i < collectionCapacity; i++)
			collection.add("a");
		ArrayIndexedCollection testCollection = new ArrayIndexedCollection(collection, initialCapacity);
		assertEquals(collectionCapacity, testCollection.size());
	}

}
