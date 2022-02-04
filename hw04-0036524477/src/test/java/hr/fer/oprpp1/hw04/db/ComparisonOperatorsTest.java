package hr.fer.oprpp1.hw04.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * A test class that test the comparison operators
 *
 * * @author Marko Tunjić
 */
public class ComparisonOperatorsTest {

    /** A test method that tests the less operator */
    @Test
    void testLessOperator() {
        assertEquals(false, ComparisonOperators.LESS.satisfied("B", "AAAAAAAAAA"));
        assertEquals(true, ComparisonOperators.LESS.satisfied("AAAAAAAAA", "B"));
        assertEquals(false, ComparisonOperators.LESS.satisfied("B", "B"));
    }

    /** A test method that tests the less or equals operator */
    @Test
    void testLessOrEqualsOperator() {
        assertEquals(false, ComparisonOperators.LESS_OR_EQUALS.satisfied("B", "AAAAAAAAAA"));
        assertEquals(true, ComparisonOperators.LESS_OR_EQUALS.satisfied("AAAAAAAAA", "B"));
        assertEquals(true, ComparisonOperators.LESS_OR_EQUALS.satisfied("B", "B"));
    }

    /** A test method that tests the equals operator */
    @Test
    void testEqualsOperator() {
        assertEquals(true, ComparisonOperators.EQUALS.satisfied("B", "B"));
        assertEquals(false, ComparisonOperators.EQUALS.satisfied("B", "A"));
    }

    /** A test method that tests the nt equals operator */
    @Test
    void testNotEqualsOperator() {
        assertEquals(false, ComparisonOperators.NOT_EQUALS.satisfied("B", "B"));
        assertEquals(true, ComparisonOperators.NOT_EQUALS.satisfied("B", "A"));
    }

    /** A test method that tests the greater operator */
    @Test
    void testGreaterOperator() {
        assertEquals(true, ComparisonOperators.GREATER.satisfied("B", "AAAAAAAAAA"));
        assertEquals(false, ComparisonOperators.GREATER.satisfied("AAAAAAAAA", "B"));
        assertEquals(false, ComparisonOperators.GREATER.satisfied("B", "B"));
    }

    /** A test method that tests the greater or equals operator */
    @Test
    void testGreaterOrEqualsOperator() {
        assertEquals(true, ComparisonOperators.GREATER_OR_EQUALS.satisfied("B", "AAAAAAAAAA"));
        assertEquals(false, ComparisonOperators.GREATER_OR_EQUALS.satisfied("AAAAAAAAA", "B"));
        assertEquals(true, ComparisonOperators.GREATER_OR_EQUALS.satisfied("B", "B"));
    }

    /** A test method that tests the LIKE operator */
    @Test
    void testLikeOperator() {
        assertEquals(true, ComparisonOperators.LIKE.satisfied("BBBB", "BB*BB"));
        assertEquals(true, ComparisonOperators.LIKE.satisfied("BBABB", "BB*BB"));
        assertEquals(false, ComparisonOperators.LIKE.satisfied("AAA", "AA*AA"));
        assertEquals(true, ComparisonOperators.LIKE.satisfied("AAB", "AA*"));
        assertEquals(true, ComparisonOperators.LIKE.satisfied("AA", "AA*"));
        assertEquals(true, ComparisonOperators.LIKE.satisfied("AA", "*AA"));
        assertEquals(true, ComparisonOperators.LIKE.satisfied("AA", "*AA"));
        assertEquals(true, ComparisonOperators.LIKE.satisfied("AA", "AA"));
        assertEquals(false, ComparisonOperators.LIKE.satisfied("AA", "AB"));
        assertThrows(IllegalArgumentException.class, () -> ComparisonOperators.LIKE.satisfied("AA", "*AA*"));
    }
}
