package hr.fer.oprpp1.hw04.db;

/**
 * A class that contains constant {@link IComparisonOperator} implementations
 *
 * @author Marko Tunjić
 */
public class ComparisonOperators {
    /**
     * A {@link IComparisonOperator} implementation that checks if the first string
     * is less than the second
     */
    public static final IComparisonOperator LESS = (s1, s2) -> s1.compareTo(s2) < 0;

    /**
     * A {@link IComparisonOperator} implementation that checks if the first string
     * is less or equal than the second
     */
    public static final IComparisonOperator LESS_OR_EQUALS = (s1, s2) -> s1.compareTo(s2) <= 0;

    /**
     * A {@link IComparisonOperator} implementation that checks if the first string
     * is greater than the second
     */
    public static final IComparisonOperator GREATER = (s1, s2) -> s1.compareTo(s2) > 0;

    /**
     * A {@link IComparisonOperator} implementation that checks if the first string
     * is greater or equal than the second
     */
    public static final IComparisonOperator GREATER_OR_EQUALS = (s1, s2) -> s1.compareTo(s2) >= 0;

    /**
     * A {@link IComparisonOperator} implementation that checks if the first string
     * is equal to the second
     */
    public static final IComparisonOperator EQUALS = (s1, s2) -> s1.equals(s2);

    /**
     * A {@link IComparisonOperator} implementation that checks if the first string
     * is not equal to the second
     */
    public static final IComparisonOperator NOT_EQUALS = (s1, s2) -> !s1.equals(s2);

    /**
     * A {@link IComparisonOperator} implementation that checks if the first string
     * is LIKE the second. The second string can contain stars which represents 0 or
     * more character. Example AAAA like AA*AA returns true but AAA like AA*AA
     * return false. Throws {@link IllegalArgumentException} if the second string
     * has more than one star
     */
    public static final IComparisonOperator LIKE = (s1, s2) -> {
        // check if there is a star
        if (s2.contains("*")) {
            // get the index of the star
            int firstStarIndex = s2.indexOf("*");

            // check if there are more stars
            if (firstStarIndex != s2.lastIndexOf("*"))
                throw new IllegalArgumentException("The string in the LIKE operator has more than 1 star");

            // get the string before the star
            String before = s2.substring(0, firstStarIndex);

            // get the string after the star if there is one
            String after = "";
            try {
                after = s2.substring(firstStarIndex + 1);
            } catch (IndexOutOfBoundsException e) {
                after = "";
            }

            // check if the string are alike
            if (s1.startsWith(before) && s1.endsWith(after) && s1.length() >= before.length() + after.length())
                return true;
            return false;
        }

        // if there is no start then we check if the strings are equal
        return s1.equals(s2);
    };
}
