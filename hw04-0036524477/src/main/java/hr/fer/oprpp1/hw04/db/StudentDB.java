package hr.fer.oprpp1.hw04.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import hr.fer.oprpp1.hw04.db.QueryParser.QueryParserException;

/**
 * A class that is used to read a database file and to create a databse
 *
 * @author Marko Tunjić
 */
public class StudentDB {
    /** A main method that reads the database file and creates a databse */
    public static void main(String[] args) {
        List<String> lines;

        // never dump an exception to the user
        try {
            lines = readLinesFromDatabase();
        } catch (IOException e) {
            System.out.println("Could not open the specified file.");
            return;
        }

        // create database
        StudentDatabse db = new StudentDatabse(lines);

        // read user input
        System.out.print("> ");
        Scanner sc = new Scanner(System.in);
        String line;
        while (!(line = sc.nextLine()).equals("exit")) {
            // get records if we get errors in parsing or in ComparisonOperators just print
            // them and don't break
            List<StudentRecord> records;
            try {
                // get all the records
                records = selectFromDatabaseBasedOnFilteringCondition(db, line);
            }
            // exception in aprsing
            catch (QueryParserException e) {
                System.out.println(e.getMessage());
                continue;
            }
            // exception in comparing
            catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                continue;
            }
            // format the output
            List<String> output = RecordFormatter.format(records);

            // print the formatted output
            output.forEach(System.out::println);
            System.out.print("> ");
        }
        System.out.println("Goodbye!");
        sc.close();
    }

    /**
     * A method that selects all records that match the given query. Throws
     * {@link hr.fer.oprpp1.hw04.db.QueryParser.QueryParserException} for any
     * parsing error and {@link IllegalArgumentException} for comparison excepton
     *
     * @param db   the database to select from
     * @param line the queryString
     *
     * @return all the record that passed the filter
     *
     * @throws QueryParserException     in case of any parsing exception
     * @throws IllegalArgumentException in case of comparison exception
     */
    private static List<StudentRecord> selectFromDatabaseBasedOnFilteringCondition(StudentDatabse db, String line) {
        QueryParser parser = null;

        // parse
        parser = new QueryParser(line);

        // check if direct
        if (parser.isDirectQuery()) {
            // find by jmbag
            StudentRecord r = db.forJMBAG(parser.getQueriedJMBAG());
            return List.of(r);
        } else {
            // filter
            List<StudentRecord> result = db.filter(new QueryFilter(parser.getQuery()));
            return result;
        }
    }

    /**
     * A method that reads all lines from the database file
     *
     * @return the read lines
     */
    public static List<String> readLinesFromDatabase() throws IOException {
        return Files.readAllLines(Path.of("src/main/resources/database.txt"));
    }
}
