package hr.fer.oprpp1.hw04.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/** A class that test the query parser */
public class QueryParserTest {

    /** A test method that tests the query parser when passing two attributes */
    @Test
    void testQueryParserWhenPassingTwoAttributes() {
        assertThrows(QueryParser.QueryParserException.class, () -> new QueryParser("query firstName=lastName"));
    }

    /** A test method that tests the query parser when passing two strings */
    @Test
    void testQueryParserWhenPassingTwoStrings() {
        assertThrows(QueryParser.QueryParserException.class, () -> new QueryParser("query \"firstName\"=\"lastName\""));
    }

    /**
     * A test method that tests the query parser when passing a query that is not
     * complete
     */
    @Test
    void testQueryParserWhenPassingNotFInishedQuery() {
        assertThrows(QueryParser.QueryParserException.class, () -> new QueryParser("query firstName="));
    }

    /** A test method that tests the query parser when passing a direct query */
    @Test
    void testQueryParserWhenPassingDirectQuery() {
        QueryParser parser = new QueryParser("query jmbag=\"003524477\"");
        assertEquals(true, parser.isDirectQuery());
    }

    /**
     * A test method that tests the query parser when passing a last name!="some
     * string" conditional
     */
    @Test
    void testQueryParserWhenLastNameNotEqualsQuery() {
        QueryParser parser = new QueryParser("query lastName!=\"003524477\"");
        assertEquals(FieldValueGetters.LAST_NAME, parser.getQuery().get(0).getFieldGetter());
        assertEquals(ComparisonOperators.NOT_EQUALS, parser.getQuery().get(0).getComparisonOperator());
        assertEquals("003524477", parser.getQuery().get(0).getStringLiteral());
    }

    /** A test method that tests the query parser when not passing the query word */
    @Test
    void testQueryParserWhenNotPassingQueryWord() {
        assertThrows(QueryParser.QueryParserException.class, () -> new QueryParser("jmbag=\"003524477\""));
    }

    /**
     * A test method that tests the query parser when passing the query word on the
     * wrong place
     */
    @Test
    void testQueryParserWhenPassingQueryWordLate() {
        assertThrows(QueryParser.QueryParserException.class, () -> new QueryParser("jmbag=\"003524477\" query"));
    }

    /**
     * A test method that tests the isDirectQuery when passing jmbag<"someString"
     */
    @Test
    void testIsDirectQUeryWhenPassingJmbagAndLessOperator() {
        QueryParser parser = new QueryParser("query jmbag<\"003524477\"");
        assertEquals(false, parser.isDirectQuery());
    }

    /**
     * A test method that tests the query parser when passing more expressions
     * connected with AND
     */
    @Test
    void testGetQueryWhenPassingMoreExpressions() {
        QueryParser parser = new QueryParser(
                "query lastName!=\"003524477\" AnD lastName!=\"003524477\" ANd firstName=\"Marko\"");
        assertEquals(3, parser.getQuery().size());
    }
}
