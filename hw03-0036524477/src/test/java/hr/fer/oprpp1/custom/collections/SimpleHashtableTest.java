package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SimpleHashtableTest {
    SimpleHashtable<String, Integer> defaultHashtable;

    @BeforeEach
    void initializeDefaultHashtable() {
        defaultHashtable = new SimpleHashtable<>(2);
    }

    /** A method that test the put method when passing null key */
    @Test
    void testPutMethodWhenPassingNull() {
        assertThrows(NullPointerException.class, () -> defaultHashtable.put(null, 1));
    }

    /** A method that test the put method when passing valid key */
    @Test
    void testPutMethodWhenPassingValid() {
        assertEquals(null, defaultHashtable.put("Marko", 1));
    }

    /**
     * A method that test the put method when passing valid key and then chaning the
     * value
     */
    @Test
    void testPutMethodWhenPassingValidAndThenChanging() {
        assertEquals(null, defaultHashtable.put("Marko", null));
        assertEquals(null, defaultHashtable.put("Marko", 3));
    }

    /**
     * A method that test the put method when putting above critical treshhold
     */
    @Test
    void testPutMethodWhenPuttingAboveCriticalTreshold() {
        assertEquals(null, defaultHashtable.put("Marko", null));
        assertEquals(null, defaultHashtable.put("Mark", null));
        assertEquals(null, defaultHashtable.put("Mar", null));
    }

    /**
     * A method that test the get method when passing null on empty hash table
     */
    @Test
    void testGetMethodOnEmpty() {
        assertEquals(null, defaultHashtable.get(null));
    }

    /**
     * A method that test the get method when passing valid
     */
    @Test
    void testGetMethodWhenValidAndExistingArguments() {
        assertEquals(null, defaultHashtable.put("Marko", null));
        assertEquals(null, defaultHashtable.get("Marko"));
    }

    /** A test method that test the contains key method when passing null */
    @Test
    void testContainsKeyWhenPassingNull() {
        assertEquals(false, defaultHashtable.containsKey(null));
    }

    /**
     * A test method that test the contains key method when passing valid and
     * existing argument
     */
    @Test
    void testContainsKeyWhenPassingValidAndExisting() {
        assertEquals(null, defaultHashtable.put("Marko", null));
        assertEquals(true, defaultHashtable.containsKey("Marko"));
    }

    /**
     * A test method that test the contains value method when passing valid and
     * existing argument null
     */
    @Test
    void testContainsValueWhenPassingValidAndExistingNull() {
        assertEquals(null, defaultHashtable.put("Marko", null));
        assertEquals(true, defaultHashtable.containsValue(null));
    }

    /**
     * A test method that tests the remove method when passing valid and existing
     * arugument
     */
    @Test
    void testRemoveMethodWhenPassingValidAndExistingArguments() {
        defaultHashtable.put("Ivana", 2);
        defaultHashtable.put("Ante", 2);
        defaultHashtable.put("Jasna", 2);
        defaultHashtable.put("Kristina", 5);
        defaultHashtable.put("Ivana", 5);
        assertEquals(5, defaultHashtable.remove("Ivana"));
    }

    /**
     * A test method that test the iterator remove method when removing all together
     * with isEmpty and size method
     */
    @Test
    @SuppressWarnings("unused")
    void testIteratorRemoveMethodWhenRemovingAllWithSizeAndIsEmptyMethod() {
        defaultHashtable.put("Ivana", 2);
        defaultHashtable.put("Ante", 2);
        defaultHashtable.put("Jasna", 2);
        defaultHashtable.put("Kristina", 5);
        defaultHashtable.put("Ivana", 5);
        Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = defaultHashtable.iterator();
        while (iter.hasNext()) {
            SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
            // System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
            iter.remove();
        }
        assertEquals(0, defaultHashtable.size());
        assertEquals(true, defaultHashtable.isEmpty());
    }

    /**
     * A test method that test the iterator remove method when removing specific
     * together with isEmpty and size method
     */
    @Test
    void testIteratorRemoveMethodWhenRemovingSpecificTogetherWithIsEmptyAndSizeMethod() {
        defaultHashtable.put("Ivana", 2);
        defaultHashtable.put("Ante", 2);
        defaultHashtable.put("Jasna", 2);
        defaultHashtable.put("Kristina", 5);
        defaultHashtable.put("Ivana", 5);
        Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = defaultHashtable.iterator();
        while (iter.hasNext()) {
            SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
            if (pair.getKey().equals("Ivana")) {
                iter.remove();
            }
        }
        assertEquals(false, defaultHashtable.containsKey("Ivana"));
        assertEquals(false, defaultHashtable.isEmpty());
        assertEquals(3, defaultHashtable.size());
    }

    /**
     * A test method that test the iterator remove method when double remove
     */
    @Test
    void testIteratorRemoveMethodWhenRemovingTwoTimes() {
        defaultHashtable.put("Ivana", 2);
        defaultHashtable.put("Ante", 2);
        defaultHashtable.put("Jasna", 2);
        defaultHashtable.put("Kristina", 5);
        defaultHashtable.put("Ivana", 5);
        Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = defaultHashtable.iterator();
        assertThrows(IllegalStateException.class, () -> {
            while (iter.hasNext()) {
                SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
                if (pair.getKey().equals("Ivana")) {
                    iter.remove();
                    iter.remove();
                }
            }
        });
    }

    /**
     * A test method that test the iterator remove method when illegal modification
     */
    @Test
    void testIteratorRemoveMethodWhenRemovingIllegaly() {
        defaultHashtable.put("Ivana", 2);
        defaultHashtable.put("Ante", 2);
        defaultHashtable.put("Jasna", 2);
        defaultHashtable.put("Kristina", 5);
        defaultHashtable.put("Ivana", 5);
        Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = defaultHashtable.iterator();
        assertThrows(IllegalStateException.class, () -> {
            while (iter.hasNext()) {
                SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
                if (pair.getKey().equals("Ivana")) {
                    iter.remove();
                }
                defaultHashtable.remove("Ante");
            }
        });
    }

    /** test clear and isEmpty method */
    @Test
    void testClearAndIsEmptyMethod() {
        defaultHashtable.put("Ivana", 2);
        defaultHashtable.put("Ante", 2);
        defaultHashtable.put("Jasna", 2);
        defaultHashtable.put("Kristina", 5);
        defaultHashtable.put("Ivana", 5);
        defaultHashtable.clear();
        assertEquals(true, defaultHashtable.isEmpty());
    }

}
