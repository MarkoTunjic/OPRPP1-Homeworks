package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DictionaryTest {
    /** A default dictionary */
    Dictionary<String, Integer> defaulDictionary;

    /** A method that initialzes the default dictionary before each test */
    @BeforeEach
    void initializeDefaultDictionary() {
        defaulDictionary = new Dictionary<>();
    }

    /** A method that tests the constructor and the is empty method */
    @Test
    void testIsEmptyMethodWhenCreatingEmptyDictionary() {
        assertEquals(true, defaulDictionary.isEmpty());
    }

    /** A method that test the constructor and the size method */
    @Test
    void testSizeMethodOnEmptyDictionary() {
        assertEquals(0, defaulDictionary.size());
    }

    /**
     * A method that test the put method together with the size and is empty method
     * when adding a new pair
     */
    @Test
    void testPutAndSizeAndIsEmptyMethodWhenAddingANewElement() {
        assertEquals(null, defaulDictionary.put("key", 0));
        assertEquals(1, defaulDictionary.size());
        assertEquals(false, defaulDictionary.isEmpty());
    }

    /**
     * A method that test the put method together with the size and is empty method
     * when adding a new pair and then changing the value
     */
    @Test
    void testPutAndSizeAndIsEmptyMethodWhenAddingANewElementAndThenChangingIt() {
        assertEquals(null, defaulDictionary.put("key", 0));
        assertEquals(0, defaulDictionary.put("key", 1));
        assertEquals(1, defaulDictionary.size());
        assertEquals(false, defaulDictionary.isEmpty());
    }

    /**
     * A method that test the put method when passing null key
     */
    @Test
    void testPutAndSizeAndIsEmptyMethodWhenAddingANewElementWithNullKey() {
        assertThrows(NullPointerException.class, () -> defaulDictionary.put(null, 0));
    }

    /**
     * A method that tests the get method when passing null on a non empty
     * dictionary
     */
    @Test
    void testGetMethodWhenPassingNullOnANonEmptyDictionary() {
        assertEquals(null, defaulDictionary.put("key", 0));
        assertEquals(null, defaulDictionary.get(null));
    }

    /**
     * A method that tests the get method when passing valid argument on a non empty
     * dictionary
     */
    @Test
    void testGetMethodWhenPassingVAlidArgumentOnANonEmptyDictionary() {
        assertEquals(null, defaulDictionary.put("key", 0));
        assertEquals(0, defaulDictionary.get("key"));
    }

    /**
     * A method that tests the remove method when passing null on a non empty
     * dictionary
     */
    @Test
    void testRemoveMethodWhenPassingNullOnANonEmptyDictionary() {
        assertEquals(null, defaulDictionary.put("key", 0));
        assertEquals(null, defaulDictionary.remove(null));
    }

    /**
     * A method that tests the remove method when passing valid argument on a non
     * empty dictionary
     */
    @Test
    void testRemoveMethodWhenPassingValidArgumentOnANonEmptyDictionary() {
        assertEquals(null, defaulDictionary.put("key", 0));
        assertEquals(0, defaulDictionary.remove("key"));
    }
}
