package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class that simulates a databse of StudentRecords
 *
 * @author Marko Tunjić
 */
public class StudentDatabse {
    /** A private attribute that contains all available student records */
    private List<StudentRecord> db;

    /**
     * A private attribute that represents a index of all available records so that
     * the get method is O(1) complexity
     */
    private Map<String, StudentRecord> index;

    /**
     * A constructor that gets a list of strings that represnt a StudentRecord. Each
     * element should have 4 attributes seperated by a single tab and they
     * represent: jmbag (must have 10 numbers, and must be unique in the databse),
     * last name (not null), first name (not null), <br>
     * grade(between 1 and 5 [including]) the constructor throws
     * {@link IllegalArgumentException} if any of the constraints was violated
     *
     * @param rows a collection of rows which represent a StudentRecord
     *
     *
     */
    public StudentDatabse(List<String> rows) {
        // allocate the database
        db = new ArrayList<>();
        index = new HashMap<>();

        // loop through all the rows
        for (String row : rows) {
            // split the row
            String[] splittedRow = row.split("\t");

            // check if correct number of arguments was given
            if (splittedRow.length != 4)
                throw new IllegalArgumentException(String
                        .format("4 parameters were expected but %d were given in row: %s", splittedRow.length, row));
            // check if jmbag is in correct format
            if (!validateJmbag(splittedRow[0]))
                throw new IllegalArgumentException(
                        String.format("Invalid jmbag in row: %s. Jmbag must have 10 numbers in it!", row));

            // check if jmbag is unique
            if (index.containsKey(splittedRow[0]))
                throw new IllegalArgumentException(String.format("Duplicate jmbag in row: %s.", row));

            // get the grade and check if valid
            int grade = validateGrade(splittedRow[3]);
            if (grade < 0)
                throw new IllegalArgumentException(String.format(
                        "Invalid final grade in row: %s. Final grade must be between 1 and 5 (inclusive)", row));

            // if everything passed create a new record and put it inside the database and
            // inside the index
            StudentRecord record = new StudentRecord(splittedRow[0], splittedRow[2], splittedRow[1], grade);
            db.add(record);
            index.put(splittedRow[0], record);
        }
    }

    /**
     * Returns the StudentRecord that matches the given jmbag or null if the jmbag
     * is not tracked in the database
     *
     * @param jmbag the jmbag of the student
     *
     * @return the student record matching the given jmbag
     */
    public StudentRecord forJMBAG(String jmbag) {
        return index.get(jmbag);
    }

    /**
     * A method that returns all the students that the given filter accepts. Throws
     * a {@link NullPointerException} if null was given as a filter.
     *
     * @param filter the filter used to determine if a StudentRecord is acceptable
     *               or not
     *
     * @return a list of students that passed the filter
     *
     * @throws NullPointerException if null filter was given
     */
    public List<StudentRecord> filter(IFilter filter) {
        if (filter == null)
            throw new NullPointerException("The given filter is null!");
        List<StudentRecord> temp = new ArrayList<>();
        db.stream().filter(record -> filter.accepts(record)).forEach(record -> temp.add(record));
        return temp;
    }

    /**
     * A getter that returns the list of students which represents the database
     *
     * @return the database
     */
    public List<StudentRecord> getDb() {
        return db;
    }

    /**
     * A private method that checks if the given string is a valid final grade if it
     * is true is the method returns true and false otherwise. A grade is valid if
     * it is a number between 1 nd 5 (inclusive)
     *
     * @param finalGrade the string that will be checked if it is a valid grade
     *
     * @return true if valid grade and false otherwise
     */
    private int validateGrade(String finalGrade) {
        int grade = -1;

        // try to parse the string
        try {
            grade = Integer.parseInt(finalGrade);

            // check if the number matches the constraints
            if (!(grade >= 1 && grade <= 5))
                return -1;
        }
        // it wasn't a number
        catch (NumberFormatException e) {
            return -1;
        }

        return grade;
    }

    /**
     * A private method that check if the given string is a valid jmbag and returns
     * true if it is and false otherwise. The jmbag is valid if it contains 10
     * numbers.
     *
     * @param jmbag the string that will be validated
     *
     * @return true if the given string is a valid jmbag
     */
    private boolean validateJmbag(String jmbag) {
        // check if lenght is ok
        if (jmbag.length() != 10)
            return false;

        // check if every character is a number
        for (Character a : jmbag.toCharArray()) {
            if (!Character.isDigit(a))
                return false;
        }

        return true;
    }
}
