package hr.fer.oprpp1.hw04.db;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * A class taht test the Condtional expressions
 *
 * @author Marko Tunjić
 */
public class ConditionalExpressionTest {

    /** A private attribute that represents one student record */
    private final StudentRecord record = new StudentRecord("0036524477", "Marko", "Tunjić", 5);

    /**
     * A test method that tests the conditional expression when passing the like
     * operator and a first name getter
     */
    @Test
    void testConditionalExpressionWithLikeOperatorAndFirstNameGetter() {
        ConditionalExpression expression = new ConditionalExpression(ComparisonOperators.LIKE, "M*",
                FieldValueGetters.FIRST_NAME);
        assertEquals(true, expression.getComparisonOperator().satisfied(expression.getFieldGetter().get(record),
                expression.getStringLiteral()));
    }
}
