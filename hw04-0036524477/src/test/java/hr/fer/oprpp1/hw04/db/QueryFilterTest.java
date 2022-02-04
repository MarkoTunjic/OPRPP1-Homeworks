package hr.fer.oprpp1.hw04.db;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * A class that test the QueryFilter
 *
 * @author Marko Tunjić
 */
public class QueryFilterTest {

    /** A private attribute that represents one student record */
    private final StudentRecord record = new StudentRecord("0036524477", "Marko", "Tunjić", 5);

    /**
     * A test method that tests the query filter when passing a filter that should
     * say true
     */
    @Test
    void testQueryFilterWhenPassingFilter() {
        QueryParser parser = new QueryParser("query firstName=\"Marko\" AnD lastName LIKE \"T*\"");
        QueryFilter filter = new QueryFilter(parser.getQuery());
        assertEquals(true, filter.accepts(record));
    }

    /**
     * A test method that tests the query filter when passing a filter that should
     * say false
     */
    @Test
    void testQueryFilterWhenNonPassingFilter() {
        QueryParser parser = new QueryParser("query firstName=\"Marko\" AnD lastName LIKE \"A*\"");
        QueryFilter filter = new QueryFilter(parser.getQuery());
        assertEquals(false, filter.accepts(record));
    }
}
