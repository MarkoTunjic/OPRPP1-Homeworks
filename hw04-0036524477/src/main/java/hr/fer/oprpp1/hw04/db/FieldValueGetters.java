package hr.fer.oprpp1.hw04.db;

/**
 * A class taht contains constant {@link IFieldValueGetter} implementations
 *
 * @author Marko Tunjić
 */
public class FieldValueGetters {

    /** A {@link IFieldValueGetter} that gets the first name */
    public static final IFieldValueGetter FIRST_NAME = record -> record.getFirstName();

    /** A {@link IFieldValueGetter} that gets the last name */
    public static final IFieldValueGetter LAST_NAME = record -> record.getLastName();

    /** A {@link IFieldValueGetter} that gets the jmbag */
    public static final IFieldValueGetter JMBAG = record -> record.getJmbag();
}
