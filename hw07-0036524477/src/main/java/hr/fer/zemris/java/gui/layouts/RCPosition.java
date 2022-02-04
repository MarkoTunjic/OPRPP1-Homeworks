package hr.fer.zemris.java.gui.layouts;

/**
 * A class that defines the position of a component in the {@link CalcLayout}
 *
 * @author Marko Tunjić
 */
public class RCPosition {
    /**
     * An private attribute that represents the row in which the component will be
     * placed
     */
    private int row;

    /**
     * An private attribute that represents the column in which the component will
     * be placed
     */
    private int column;

    /**
     * A contructor that creates a {@link RCPosition} from the given params.
     *
     * @param row    represents the row in which the component will be placed
     * @param column represents the column in which the component will be placed
     */
    public RCPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * A method that returns the row of this position
     *
     * @return row of this position
     */
    public int getRow() {
        return row;
    }

    /**
     * A method that returns the column of this position
     *
     * @return row of this position
     */
    public int getColumn() {
        return column;
    }

    /**
     * A static method that parses the given string into a {@link RCPosition} the
     * expected format is (row,column). Throws {@link NullPointerException} if null
     * was given
     *
     * @param text the text to be parsed
     *
     * @return the {@link RCPosition} defined by the given string
     *
     * @throws NullPointerException if null was given
     */
    public static RCPosition parse(String text) {
        if (text == null)
            throw new NullPointerException("RCPosition text can not be null");

        // split row and column
        String[] splittedText = text.split(",");
        if (splittedText.length != 2)
            throw new IllegalArgumentException("Illegal format was given expected (row,column)");

        // try parsing string to int
        int givenRow;
        int givenColumn;
        try {
            givenRow = Integer.parseInt(splittedText[0].trim());
            givenColumn = Integer.parseInt(splittedText[1].trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Could not parse numbers for given text");
        }
        return new RCPosition(givenRow, givenColumn);
    }

    /**
     * A method that returns the hash code of this object based on the row and
     * column
     *
     * @return the hash code of this object
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + column;
        result = prime * result + row;
        return result;
    }

    /**
     * A method that returns true if this object and the given object are equal and
     * false otherwise
     *
     * @return true if equal false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RCPosition other = (RCPosition) obj;
        if (column != other.column)
            return false;
        if (row != other.row)
            return false;
        return true;
    }

}
