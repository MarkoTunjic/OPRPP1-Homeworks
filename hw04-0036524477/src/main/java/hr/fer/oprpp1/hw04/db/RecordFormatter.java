package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that contains static methods used for formattig records
 *
 * @author Marko Tunjić
 */
public class RecordFormatter {

    /**
     * A method that formats givenrecords like this: <br>
     * +============+========+========+=====+</br>
     * <br/>
     * | 0000000003 | Bosnić | Andrea | 4 | <br/>
     * <br/>
     * +============+========+========+=====+ <br/>
     *
     * @param records the records to format
     *
     * @return a list of formatted records
     *
     */
    public static List<String> format(List<StudentRecord> records) {
        // create the output list
        List<String> output = new ArrayList<>();

        // check if there are records
        int size = records.size();
        if (size != 0) {
            // get the longest names
            int longestFirstName = records.stream().mapToInt(record -> record.getFirstName().length()).max().getAsInt();
            int longestLastName = records.stream().mapToInt(record -> record.getLastName().length()).max().getAsInt();

            // add the first row
            output.add(String.format("+%s+%s+%s+%s+", "=".repeat(12), "=".repeat(longestLastName + 2),
                    "=".repeat(longestFirstName + 2), "=".repeat(3)));

            // add the records
            records.stream()
                    .forEach(record -> output.add(String.format(
                            "| %s | %-" + longestLastName + "s | %-" + longestFirstName + "s | %d |", record.getJmbag(),
                            record.getLastName(), record.getFirstName(), record.getFinalGrade())));

            // add the last row
            output.add(String.format("+%s+%s+%s+%s+", "=".repeat(12), "=".repeat(longestLastName + 2),
                    "=".repeat(longestFirstName + 2), "=".repeat(3)));
        }

        // add the count
        output.add(String.format("Records selected: %d", records.size()));
        return output;
    }
}
