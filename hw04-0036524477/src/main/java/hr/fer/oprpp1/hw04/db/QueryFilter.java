package hr.fer.oprpp1.hw04.db;

import java.util.List;

/**
 * A class that filters records based on the giben conditional expressions
 *
 * @author Marko Tunjić
 */
public class QueryFilter implements IFilter {

    /** A private attribute that contains all the expressions to be checked */
    private List<ConditionalExpression> conditionalExpressions;

    /**
     * A constructor that creates a QueryFilter from the given parameters
     *
     * @param conditionalExpressions the expressions to be checked
     */
    public QueryFilter(List<ConditionalExpression> conditionalExpressions) {
        this.conditionalExpressions = conditionalExpressions;
    }

    /**
     * A method that returns true iff all expressions were satisfied and false
     * otherwise. Throws {@link IllegalArgumentException} if invalid strings were
     * given to the comparion operator
     *
     * @param record the record to check
     *
     * @return true if the record satisfied all the conditionals and false otherwise
     *
     * @throws IllegalArgumentExceptionif invalid strings were given to the
     *                                    comparison operator
     */
    @Override
    public boolean accepts(StudentRecord record) {

        // loop through all the conditionals and check if satisfied
        for (ConditionalExpression expression : conditionalExpressions)
            if (!expression.getComparisonOperator().satisfied(expression.getFieldGetter().get(record),
                    expression.getStringLiteral()))
                return false;
        

        return true;
    }

}
