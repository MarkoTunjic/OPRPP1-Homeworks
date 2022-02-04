package hr.fer.oprpp1.hw04.db;

/**
 * A strategy interface that is used to compares 2 strings on a specific way
 *
 * @author Marko Tunjić
 */
public interface IComparisonOperator {

    /** A method that returns true if the strings satisify the operation */
    public boolean satisfied(String value1, String value2);
}
