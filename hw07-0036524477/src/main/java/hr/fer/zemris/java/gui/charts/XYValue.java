package hr.fer.zemris.java.gui.charts;

/**
 * A class that represents the x and y value for a chart column
 *
 * @author Marko Tunjić
 */
public class XYValue {
    /** An private attribute that contains the x value of the chart column */
    private int x;

    /** An private attribute that contains the y value of the chart column */
    private int y;

    /**
     * A constructor that creates an {@link XYValue} object from the given params.
     *
     * @param x the x value of the chart column
     * @param y the y value of the chart column
     */
    public XYValue(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * A method that returns the x value of this chart column
     *
     * @return the x value of this chart column
     */
    public int getX() {
        return x;
    }

    /**
     * A method that returns the y value of this chart column
     *
     * @return the y value of this chart column
     */
    public int getY() {
        return y;
    }

}
