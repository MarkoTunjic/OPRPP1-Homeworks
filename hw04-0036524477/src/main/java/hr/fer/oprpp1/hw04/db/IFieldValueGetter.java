package hr.fer.oprpp1.hw04.db;

/**
 * A srategy interface that defones one method that returns a specified
 * attribute from the {@link StudentRecord} class
 *
 * @author Marko Tunjić
 */
public interface IFieldValueGetter {
    /**
     * A method that returns a specific attribute from the given
     * {@link StudentRecord}
     *
     * @param the student recorded that is used for extracting attributes
     *
     * @return the attribute that the method is created to return
     */
    public String get(StudentRecord record);
}
