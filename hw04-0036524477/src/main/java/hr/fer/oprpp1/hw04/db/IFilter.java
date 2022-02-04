package hr.fer.oprpp1.hw04.db;

/**
 * A strategy interface that is used as a filter for students in the database
 *
 * @author Marko Tunjić
 */
public interface IFilter {
    /**
     * A method that returns true if the given StudentRecord is acceptable and false
     * otherwise
     *
     * @param record the student record to be checked
     *
     * @return true if the record is acceptable and false otherwise
     */
    public boolean accepts(StudentRecord record);
}
