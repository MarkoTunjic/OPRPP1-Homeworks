package hr.fer.oprpp1.hw04.db;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A class that tests the StudentDatabase
 *
 * @author Marko Tunjić
 */
public class StudentDatabaseTest {

    /** A private {@link IFilter} implementation that always returns true */
    private final IFilter alwaysTrueFilter = (record) -> true;

    /** A private {@link IFilter} implementation that always returns false */
    private final IFilter alwaysFalseFilter = (record) -> false;

    /** A private attribute that represents the database */
    private StudentDatabse db;

    /** A method that reinitializes the database beforeeach test */
    @BeforeEach
    void initializeDatabase() throws IOException {
        db = new StudentDatabse(StudentDB.readLinesFromDatabase());
    }

    /** A test method that tests the forJmbag method when passing null */
    @Test
    void testForJmbagWhenPassingNull() {
        assertEquals(null, db.forJMBAG(null));
    }

    /** A test method that tests the forJmbag method when passingexisting jmbag */
    @Test
    void testForJmbagWhenPassingValidAndExistingJmbag() {
        assertEquals(new StudentRecord("0000000041", "Jakša", "Orešković", 2), db.forJMBAG("0000000041"));
    }

    /**
     * A test method that tests the filter method when passing the always true
     * filter
     */
    @Test
    void testFilterMethodWhenPassingAlwaysTrueIFilter() {
        assertEquals(db.getDb(), db.filter(alwaysTrueFilter));
    }

    /**
     * A test method that tests the filter method when passing the always false
     * filter
     */
    @Test
    void testFilterMethodWhenPassingAlwaysFalseIFilter() {
        assertEquals(new ArrayList<>(), db.filter(alwaysFalseFilter));
    }
}
