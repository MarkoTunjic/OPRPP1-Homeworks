package hr.fer.oprpp1.hw05.shell.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A class that contains various utilities for
 * {@link hr.fer.oprpp1.hw05.shell.ShellCommand}
 *
 * @author Marko Tunjić
 */
public class CommandUtils {

    /**
     * A method that takes a string and a set of allowed indexes to split the text
     * based on spaces and double quotes. Throws a {@link IllegalArgumentException}
     * if the given text was illegal. It's illegal if there is a single quote, or
     * after or before quotes comes a character that is not a space or EOF
     *
     * @param text           the text to be splitted
     * @param allowedIndexes the indexes of tokens on which quotes are allowed
     *
     * @return a list of all tokens created by splitting
     */
    public static List<String> splitWithQuotes(String text, Set<Integer> allowedIndexes) {
        // create a char array for fast access
        char[] chars = text.toCharArray();

        // string builder for tokens
        StringBuilder sb = new StringBuilder();

        // index of last character appended to a token
        int lastToken = 0;

        // result
        List<String> solution = new ArrayList<>();

        // a variable that indicates if we are searching for the second quote
        boolean secondQuote = false;

        // loop through all characters
        for (int i = 0; i < chars.length; i++) {
            // check if space or quote which indicates a new token has been potentially
            // found
            if (chars[i] == ' ' || chars[i] == '\"') {

                // check if next character after second quote is a space or EOF
                if (secondQuote && chars[i] == '\"' && i + 1 < chars.length && chars[i + 1] != ' ')
                    throw new IllegalArgumentException("After quotes only end of line or space is allowed");

                // check if previous of first quote is a space or begging of file
                if (!secondQuote && chars[i] == '\"' && i - 1 > 0 && chars[i - 1] != ' ')
                    throw new IllegalArgumentException("After quotes only beggining of line or space is allowed");

                // Check if the quotes are on a allowed index
                if (chars[i] == '\"' && !secondQuote && !allowedIndexes.contains(solution.size()))
                    throw new IllegalArgumentException(
                            String.format("Quotes are not allowed in the %d argument", solution.size()));

                // if we are searching for a second quote and space is found just continue to
                // next iteration
                if (chars[i] == ' ' && secondQuote)
                    continue;

                // append substring to token
                sb.append(text.substring(lastToken, i));

                // if builder not empty add to result
                if (sb.length() > 0)
                    solution.add(sb.toString());

                // empty string builder
                sb.delete(0, sb.length());

                // move to next token
                lastToken = i + 1;

                // if we found a first quote set to searching for second or if we found a first
                // unset searching for second
                if (chars[i] == '\"')
                    secondQuote = !secondQuote;
            }
        }

        // if we were searching for a second quote but none was found throw exception
        if (secondQuote)
            throw new IllegalArgumentException("Missing a quote");

        // add final token if note empty
        sb.append(text.substring(lastToken, chars.length));
        if (sb.length() > 0)
            solution.add(sb.toString());

        // return result
        return solution;
    }
}
