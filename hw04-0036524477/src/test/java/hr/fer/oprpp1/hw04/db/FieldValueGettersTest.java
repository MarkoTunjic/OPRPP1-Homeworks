package hr.fer.oprpp1.hw04.db;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * A class that tests the FieldValueGetters
 *
 * @author Marko Tunjić
 */
public class FieldValueGettersTest {

    /** A private attribute that represents one student record */
    private final StudentRecord record = new StudentRecord("0036524477", "Marko", "Tunjić", 5);

    /** A test method that tests the first name getter */
    @Test
    void testFirstNameGetter() {
        assertEquals("Marko", FieldValueGetters.FIRST_NAME.get(record));
    }

    /** A test method that tests the last name getter */
    @Test
    void testLastNameGetter() {
        assertEquals("Tunjić", FieldValueGetters.LAST_NAME.get(record));
    }

    /** A test method that tests the jmbag getter */
    @Test
    void testJMBAGGetter() {
        assertEquals("Tunjić", FieldValueGetters.LAST_NAME.get(record));
    }
}
