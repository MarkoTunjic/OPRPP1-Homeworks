package hr.fer.zemris.java.gui.charts;

import java.util.List;

/**
 * A class that contains all the inforamtion of a Bar chart
 *
 * @author Marko Tunjić
 */
public class BarChart {
    /** An private attribute that represents the chart values */
    private List<XYValue> values;

    /** An private attribute that represents the description of the x axis */
    private String xDescription;

    /** An private attribute that represents the description of the y axis */
    private String yDescription;

    /** An private attribute that represents the minimal y value */
    private int minY;

    /** An private attribute that represents the maximal y value */
    private int maxY;

    /** An private attribute that represents the distance between 2 y values */
    private int yDistance;

    /**
     * A constructor that creates a {@link BarChart} from the given params. Throws
     * {@link NullPointerException} if nullwas given, or
     * {@link IllegalArgumentException} if minimal y value is less than 0 or if
     * minimal is greater than maximal y value, or if some value is smaller thatn
     * the minimal value
     *
     * @param values       represents the chart values
     * @param xDescription represents the description of the x axis
     * @param yDescription represents the description of the y axis
     * @param minY         represents the minimal y value
     * @param maxY         represents the maximal y value
     * @param yDistance    represents the distance between 2 y values
     */
    public BarChart(List<XYValue> values, String xDescription, String yDescription, int minY, int maxY, int yDistance) {
        if (values == null || xDescription == null || yDescription == null)
            throw new NullPointerException("Values and descriptions can not be null.");
        if (minY < 0)
            throw new IllegalArgumentException("Minimal y value can not be negative");
        if (maxY <= minY)
            throw new IllegalArgumentException("Maximal y value must be greater than the minimal y value");
        for (XYValue value : values)
            if (value.getY() < minY)
                throw new IllegalArgumentException("All all Ys from the given values must be >= than the minimal y");
        this.values = values;
        this.xDescription = xDescription;
        this.yDescription = yDescription;
        this.maxY = maxY;
        this.minY = minY;
        if ((maxY - minY) % yDistance == 0)
            this.yDistance = yDistance;
        else
            while ((maxY - minY) % yDistance != 0)
                yDistance++;
        this.yDistance = yDistance;
    }

    /**
     * A method that returns the values of this chart
     *
     * @return the values of this chart
     */
    public List<XYValue> getValues() {
        return values;
    }

    /**
     * A method that returns the x description of this chart
     *
     * @return the x description of this chart
     */
    public String getxDescription() {
        return xDescription;
    }

    /**
     * A method that returns the y description of this chart
     *
     * @return the y description of this chart
     */
    public String getyDescription() {
        return yDescription;
    }

    /**
     * A method that returns the minimal y value of this chart
     *
     * @return the minimal y value of this chart
     */
    public int getMinY() {
        return minY;
    }

    /**
     * A method that returns the maximal y value of this chart
     *
     * @return the maximal y value of this chart
     */
    public int getMaxY() {
        return maxY;
    }

    /**
     * A method that returns the y distance of this chart
     *
     * @return the y distance of this chart
     */
    public int getyDistance() {
        return yDistance;
    }

}
