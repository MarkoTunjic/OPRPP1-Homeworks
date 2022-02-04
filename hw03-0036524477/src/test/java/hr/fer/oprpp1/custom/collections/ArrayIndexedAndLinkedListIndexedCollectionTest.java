package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A class that tests both the ArrayIndexed and the LinkedListIndexed collection
 * depending on the instance of the attribute: defaultCollection
 *
 * @author Marko Tunjić
 */
class ArrayIndexedAndLinkedListIndexedCollectionTest {

	/** A attribute that contains the collection we want to test */
	Collection<String> defaultCollection;

	/** A method that reinitializes the default collection before every test */
	@BeforeEach
	void getTheCollectionWeAreTesting() {
		defaultCollection = new ArrayIndexedCollection<>();
	}

	/**
	 * A test method that test the default constructor by checking if it was
	 * initialized without any elements
	 */
	@Test
	void testDefaultConstructor() {
		assertEquals(0, defaultCollection.size());
		assertArrayEquals(new Object[0], defaultCollection.toArray());
	}

	/** A test method that test the constructor when passing null as the argument */
	@Test
	void testCollectionConstructorWithOtherCollectionArgumentWhenPassingNull() {
		if (defaultCollection instanceof ArrayIndexedCollection<String>)
			assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection<>(null));
		else
			assertThrows(NullPointerException.class, () -> new LinkedListIndexedCollection<>(null));
	}

	/**
	 * A test method that test the constructor when passing an
	 * ArrayIndexedCollection
	 */
	@Test
	void testCollectionConstructorWithOtherCollectionArgumentWhenPassingValidArrayIndexedCollection() {
		ArrayIndexedCollection<String> collection = new ArrayIndexedCollection<>();
		collection.add("a");
		if (defaultCollection instanceof ArrayIndexedCollection<String>) {
			ArrayIndexedCollection<String> testCollection = new ArrayIndexedCollection<>(collection);
			assertArrayEquals(new Object[] { "a" }, testCollection.toArray());
		}

		else {
			LinkedListIndexedCollection<String> testCollection = new LinkedListIndexedCollection<>(collection);
			assertArrayEquals(new Object[] { "a" }, testCollection.toArray());
		}
	}

	/**
	 * A test method that test the constructor when passing a
	 * LinkedListIndexedCollection
	 */
	@Test
	void testCollectionConstructorWithOtherCollectionArgumentWhenPassingValidLinkedListIndexedCollection() {
		LinkedListIndexedCollection<String> collection = new LinkedListIndexedCollection<>();
		collection.add("a");
		if (defaultCollection instanceof ArrayIndexedCollection<String>) {
			defaultCollection = new ArrayIndexedCollection<>(collection);
			assertArrayEquals(new Object[] { "a" }, defaultCollection.toArray());
		}

		else {
			defaultCollection = new LinkedListIndexedCollection<>(collection);
			assertArrayEquals(new Object[] { "a" }, defaultCollection.toArray());
		}
	}

	/**
	 * A test method that tests the size and the addAll method by adding elements
	 * from a non empty collection to a non empty collection
	 */
	@Test
	void testSizeAfterAddAllMethodOnANotEmptyCollection() {
		Collection<String> adder = new ArrayIndexedCollection<>();
		int size = 10;
		defaultCollection.add("b");
		for (int i = 0; i < size; i++)
			adder.add("a");
		defaultCollection.addAll(adder);
		assertEquals(size + 1, defaultCollection.size());
	}

	/**
	 * A test method that test the size method when adding an empty collection to a
	 * non empty collection
	 */
	@Test
	void testSizeWhenAddingEmptyCollection() {
		defaultCollection.add("a");
		Collection<String> adder = new ArrayIndexedCollection<>();
		defaultCollection.addAll(adder);
		assertEquals(1, defaultCollection.size());
	}

	/**
	 * A test method that test the toArray method adding a non empty collection to a
	 * non empty collection
	 */
	@Test
	void testToArrayMethodAndAddAllMethodOnANotEmptyCollection() {
		Collection<String> adder = new ArrayIndexedCollection<>();
		int size = 10;
		defaultCollection.add("b");
		Object[] answer = new Object[size + 1];
		answer[0] = "b";
		for (int i = 0; i < size; i++) {
			adder.add("a");
			answer[i + 1] = "a";
		}
		defaultCollection.addAll(adder);
		assertArrayEquals(answer, defaultCollection.toArray());
	}

	/** A test method that test the contains method when passing null */
	@Test
	void testContainsMethodWhenPassingNull() {
		assertEquals(false, defaultCollection.contains(null));
	}

	/** A test method that test the contains method when passing valid value */
	@Test
	void testContainsMethodWhenPassingValidValue() {
		defaultCollection.add("a");
		assertEquals(true, defaultCollection.contains("a"));
	}

	/**
	 * A test method that tests the toArray method when passing an array with enough
	 * space
	 */
	@Test
	void testToArrayWhenPassingParametrizedArrayWithEnoughSpace() {
		String[] newArray = new String[16];
		for (int i = 0; i < 16; i++) {
			defaultCollection.add("a");
			newArray[i] = "a";
		}

		String[] array = new String[16];

		assertArrayEquals(newArray, defaultCollection.toArray(array));
	}

	/**
	 * A test method that tests the toArray method when passing an array with not
	 * enough space
	 */
	@Test
	void testToArrayWhenPassingParametrizedArrayWithNotEnoughSpace() {
		String[] newArray = new String[16];
		for (int i = 0; i < 16; i++) {
			defaultCollection.add("a");
			newArray[i] = "a";
		}

		String[] array = new String[2];

		assertArrayEquals(newArray, defaultCollection.toArray(array));
	}

	/**
	 * A test method that test the remove object method when removing the first
	 * element
	 */
	@Test
	void testRemoveObjectMethodWhenRemovingFirstElement() {
		defaultCollection.add("a");
		defaultCollection.add("b");
		defaultCollection.add("c");
		Object[] answer = new Object[] { "b", "c" };
		defaultCollection.remove("a");
		assertArrayEquals(answer, defaultCollection.toArray());
	}

	/**
	 * A test method that test the remove object method when removing the middle
	 * element
	 */
	@Test
	void testRemoveObjectMethodWhenRemovingMiddleElement() {
		defaultCollection.add("a");
		defaultCollection.add("b");
		defaultCollection.add("c");
		Object[] answer = new Object[] { "a", "c" };
		defaultCollection.remove("b");
		assertArrayEquals(answer, defaultCollection.toArray());
	}

	/**
	 * A test method that test the remove object method when removing from the end
	 */
	@Test
	void testRemoveObjectMethodWhenRemovingLastElement() {
		defaultCollection.add("a");
		defaultCollection.add("b");
		defaultCollection.add("c");
		Object[] answer = new Object[] { "a", "b" };
		defaultCollection.remove("c");
		assertArrayEquals(answer, defaultCollection.toArray());
	}

	/** A test method that test the is empty method on empty collection */
	@Test
	void testIsEmptyMethodOnEmptyCollection() {
		assertEquals(true, defaultCollection.isEmpty());
	}

	/** A test method that test the isEmpty method on a non empty collection */
	@Test
	void testIsEmptyMethodOnNonEmptyCollection() {
		defaultCollection.add("a");
		assertEquals(false, defaultCollection.isEmpty());
	}

	/** A test method that test the isEmpty method after clear */
	@Test
	void testIsEmptyMethodAfterClear() {
		defaultCollection.add("a");
		defaultCollection.clear();
		assertEquals(true, defaultCollection.isEmpty());
	}

	/** A test method that test the add method when adding null */
	@Test
	void testAddMethodWhenAddingNull() {
		assertThrows(NullPointerException.class, () -> defaultCollection.add(null));
	}

	/** A test method that test the add method when adding a valid value */
	@Test
	void testAddMethodWhenAddingValidValue() {
		defaultCollection.add("a");
		defaultCollection.add("b");
		assertArrayEquals(new Object[] { "a", "b" }, defaultCollection.toArray());
	}

	/** A test method that test the get method when passing an negative index */
	@Test
	void testGetMethodWhenPassingNegativeIndex() {
		if (defaultCollection instanceof ArrayIndexedCollection<String>)
			assertThrows(IndexOutOfBoundsException.class,
					() -> ((ArrayIndexedCollection<String>) defaultCollection).get(-1));
		else
			assertThrows(IndexOutOfBoundsException.class,
					() -> ((LinkedListIndexedCollection<String>) defaultCollection).get(-1));
	}

	/**
	 * A test method that test the get method when a passing an index greater then
	 * size-1
	 */
	@Test
	void testGetMethodWhenPassingIndexGreaterThenSizeMinusOne() {
		if (defaultCollection instanceof ArrayIndexedCollection<String>)
			assertThrows(IndexOutOfBoundsException.class,
					() -> ((ArrayIndexedCollection<String>) defaultCollection).get(1));
		else
			assertThrows(IndexOutOfBoundsException.class,
					() -> ((LinkedListIndexedCollection<String>) defaultCollection).get(1));
	}

	/**
	 * A test method that test the get method on a non empty collection with a
	 * existent element
	 */
	@Test
	void testGetMethodWhenAddingLegalElements() {
		String a = "a";
		String b = "b";
		defaultCollection.add(a);
		defaultCollection.add(b);
		if (defaultCollection instanceof ArrayIndexedCollection<String>)
			assertEquals(b, ((ArrayIndexedCollection<String>) defaultCollection).get(1));
		else
			assertEquals(b, ((LinkedListIndexedCollection<String>) defaultCollection).get(1));
	}

	/** A test method that test the clear method by checking size */
	@Test
	void testClearMethod() {
		String a = "a";
		String b = "b";
		defaultCollection.add(a);
		defaultCollection.add(b);
		defaultCollection.clear();
		assertEquals(0, defaultCollection.size());
	}

	/**
	 * A test method that test the insert method when inserting null value on a
	 * valid index
	 */
	@Test
	void testInsertMethodWhenPassingNullAndValidIndex() {
		defaultCollection.add("a");
		if (defaultCollection instanceof ArrayIndexedCollection<String>)
			assertThrows(NullPointerException.class,
					() -> ((ArrayIndexedCollection<String>) defaultCollection).insert(null, 0));
		else
			assertThrows(NullPointerException.class,
					() -> ((LinkedListIndexedCollection<String>) defaultCollection).insert(null, 0));

	}

	/**
	 * A test method that test the insert method when inserting valid value on a
	 * invalid index
	 */
	@Test
	void testInsertMethodWhenPassingValidObjectAndInvalidIndex() {
		if (defaultCollection instanceof ArrayIndexedCollection<String>)
			assertThrows(IndexOutOfBoundsException.class,
					() -> ((ArrayIndexedCollection<String>) defaultCollection).insert("a", -1));
		else
			assertThrows(IndexOutOfBoundsException.class,
					() -> ((LinkedListIndexedCollection<String>) defaultCollection).insert("a", -1));
	}

	/**
	 * A test method that test the insert method when inserting valid value on a
	 * invalid index
	 */
	@Test
	void testInsertMethodWhenPassingNullAndIndexBeyondRange() {
		defaultCollection.add("a");
		if (defaultCollection instanceof ArrayIndexedCollection<String>)
			assertThrows(IndexOutOfBoundsException.class,
					() -> ((ArrayIndexedCollection<String>) defaultCollection).insert(null, 2));
		else
			assertThrows(IndexOutOfBoundsException.class,
					() -> ((LinkedListIndexedCollection<String>) defaultCollection).insert(null, 2));
	}

	/**
	 * A test method that test the insert method when inserting on the beginning
	 */
	@Test
	void testInsertMethodWhenInsertingOnTheBeggining() {
		defaultCollection.add("a");
		defaultCollection.add("b");
		defaultCollection.add("c");
		Object[] correct = new Object[] { "d", "a", "b", "c" };
		if (defaultCollection instanceof ArrayIndexedCollection<String>)
			((ArrayIndexedCollection<String>) defaultCollection).insert("d", 0);
		else
			((LinkedListIndexedCollection<String>) defaultCollection).insert("d", 0);
		assertArrayEquals(correct, defaultCollection.toArray());
	}

	/**
	 * A test method that test the insert method when inserting in the middle
	 */
	@Test
	void testInsertMethodWhenInsertingInTheMiddle() {
		defaultCollection.add("a");
		defaultCollection.add("b");
		defaultCollection.add("c");
		Object[] correct = new Object[] { "a", "d", "b", "c" };
		if (defaultCollection instanceof ArrayIndexedCollection<String>)
			((ArrayIndexedCollection<String>) defaultCollection).insert("d", 1);
		else
			((LinkedListIndexedCollection<String>) defaultCollection).insert("d", 1);
		assertArrayEquals(correct, defaultCollection.toArray());
	}

	/**
	 * A test method that test the insert method when inserting on the one before
	 * last
	 */
	@Test
	void testInsertMethodWhenInsertingOnOneBeforeLast() {
		defaultCollection.add("a");
		defaultCollection.add("b");
		defaultCollection.add("c");
		Object[] correct = new Object[] { "a", "b", "d", "c" };
		if (defaultCollection instanceof ArrayIndexedCollection<String>)
			((ArrayIndexedCollection<String>) defaultCollection).insert("d", 2);
		else
			((LinkedListIndexedCollection<String>) defaultCollection).insert("d", 2);
		assertArrayEquals(correct, defaultCollection.toArray());
	}

	/**
	 * A test method that test the insert method when inserting on the end
	 */
	@Test
	void testInsertMethodWhenInsertingOnTheEnd() {
		defaultCollection.add("a");
		defaultCollection.add("b");
		defaultCollection.add("c");
		Object[] correct = new Object[] { "a", "b", "c", "d" };
		if (defaultCollection instanceof ArrayIndexedCollection<String>)
			((ArrayIndexedCollection<String>) defaultCollection).insert("d", 3);
		else
			((LinkedListIndexedCollection<String>) defaultCollection).insert("d", 3);
		assertArrayEquals(correct, defaultCollection.toArray());
	}

	/** A method that tests the indexOf method when passing null */
	@Test
	void testIndexOfMethodWhenPassingNull() {
		if (defaultCollection instanceof ArrayIndexedCollection<String>)
			assertEquals(-1, ((ArrayIndexedCollection<String>) defaultCollection).indexOf(null));
		else
			assertEquals(-1, ((LinkedListIndexedCollection<String>) defaultCollection).indexOf(null));
	}

	/**
	 * A method that tests the indexOf method when passing an object that exists
	 * inside the collection
	 */
	@Test
	void testIndexOfMethodWhenPassingExistingObject() {
		String a = "a";
		defaultCollection.add(a);
		if (defaultCollection instanceof ArrayIndexedCollection<String>)
			assertEquals(0, ((ArrayIndexedCollection<String>) defaultCollection).indexOf(a));
		else
			assertEquals(0, ((LinkedListIndexedCollection<String>) defaultCollection).indexOf(a));
	}

	/** A method that tests the remove method when passing negative index */
	@Test
	void testRemoveMethodWhenPassingNegativeIndex() {
		if (defaultCollection instanceof ArrayIndexedCollection<String>)
			assertThrows(IndexOutOfBoundsException.class,
					() -> ((ArrayIndexedCollection<String>) defaultCollection).remove(-1));
		else
			assertThrows(IndexOutOfBoundsException.class,
					() -> ((LinkedListIndexedCollection<String>) defaultCollection).remove(-1));
	}

	/**
	 * A method that tests the remove method when passing a index greater than
	 * size-1
	 */
	@Test
	void testRemoveMethodWhenPassingIndexBeyondSizeMinusOne() {
		defaultCollection.add("a");
		if (defaultCollection instanceof ArrayIndexedCollection<String>)
			assertThrows(IndexOutOfBoundsException.class,
					() -> ((ArrayIndexedCollection<String>) defaultCollection).remove(1));
		else
			assertThrows(IndexOutOfBoundsException.class,
					() -> ((LinkedListIndexedCollection<String>) defaultCollection).remove(1));
	}

	/**
	 * A method that tests the remove method when removing from the beginning
	 */
	@Test
	void testRemoveMethodWhenRemovingFromBeggining() {
		defaultCollection.add("a");
		defaultCollection.add("b");
		defaultCollection.add("c");
		Object[] correct = new Object[] { "b", "c" };
		if (defaultCollection instanceof ArrayIndexedCollection<String>)
			((ArrayIndexedCollection<String>) defaultCollection).remove(0);
		else
			((LinkedListIndexedCollection<String>) defaultCollection).remove(0);
		assertArrayEquals(correct, defaultCollection.toArray());
	}

	/**
	 * A method that tests the remove method when removing from the middle
	 */
	@Test
	void testRemoveMethodWhenRemovingFromMiddle() {
		defaultCollection.add("a");
		defaultCollection.add("b");
		defaultCollection.add("c");
		Object[] correct = new Object[] { "a", "c" };
		if (defaultCollection instanceof ArrayIndexedCollection<String>)
			((ArrayIndexedCollection<String>) defaultCollection).remove(1);
		else
			((LinkedListIndexedCollection<String>) defaultCollection).remove(1);
		assertArrayEquals(correct, defaultCollection.toArray());
	}

	/**
	 * A method that tests the remove method when removing from the end
	 */
	@Test
	void testRemoveMethodWhenRemovingFromEnd() {
		defaultCollection.add("a");
		defaultCollection.add("b");
		defaultCollection.add("c");
		Object[] correct = new Object[] { "a", "b" };
		if (defaultCollection instanceof ArrayIndexedCollection<String>)
			((ArrayIndexedCollection<String>) defaultCollection).remove(2);
		else
			((LinkedListIndexedCollection<String>) defaultCollection).remove(2);
		assertArrayEquals(correct, defaultCollection.toArray());
	}

}
