package hr.fer.oprpp1.hw04.db;

/**
 * A class that represents one query conditional
 *
 * @author Marko Tunjić
 */
public class ConditionalExpression {
    /**
     * A {@link IComparisonOperator} that is used to execute the query conditional
     */
    private IComparisonOperator comparisonOperator;

    /** A string literal to which we compare a {@link StudentRecord} attribute */
    private String stringLiteral;

    /**
     * A {@link IFieldValueGetter} that is used to get a {@link StudentRecord}
     * attribute
     */
    private IFieldValueGetter fieldGetter;

    /**
     * A constructor that creates an Conditional expression from the given
     * parameters. Trows a {@link NullPointerException} if any of the parameters is
     * null
     *
     * @param comparisonOperator a {@link IComparisonOperator} that is used to
     *                           execute the query conditional
     * @param stringLiteral      a string literal to which we compare a
     *                           {@link StudentRecord} attribute
     * @param fieldGetter        A {@link IFieldValueGetter} that is used to get a
     *                           {@link StudentRecord} attribute
     *
     * @throws NullPointerException if any of the parameters is null
     */
    public ConditionalExpression(IComparisonOperator comparisonOperator, String stringLiteral,
            IFieldValueGetter fieldGetter) {
        if (comparisonOperator == null || stringLiteral == null || fieldGetter == null)
            throw new NullPointerException("ComparisonOperator and stringLiteral and fieldGetter must not be null");
        this.comparisonOperator = comparisonOperator;
        this.stringLiteral = stringLiteral;
        this.fieldGetter = fieldGetter;
    }

    /**
     * A method that return the comparisonOperator of this object
     *
     * @return the comparisonOperator
     */
    public IComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }

    /**
     * A method that return the string literal of this object
     *
     * @return the string literal
     */
    public String getStringLiteral() {
        return stringLiteral;
    }

    /**
     * A method that return the field getter of this object
     *
     * @return the field getter
     */
    public IFieldValueGetter getFieldGetter() {
        return fieldGetter;
    }
}
