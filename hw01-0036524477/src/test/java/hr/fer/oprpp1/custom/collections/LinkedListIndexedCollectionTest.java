package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.BeforeEach;

/** A class that contains tests for the LinkedListIndexedCollection */
class LinkedListIndexedCollectionTest extends ArrayIndexedAndLinkedListIndexedCollectionTest {

	/** A method that reinitializes the default collection before every test */
	@Override
	@BeforeEach
	void getTheCollectionWeAreTesting() {
		super.defaultCollection = new LinkedListIndexedCollection();
	}

}
