package hr.fer.zemris.java.gui.charts;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.JComponent;

/**
 * A class that draws a bar chart
 *
 * @author Marko Tunjić
 */
public class BarChartComponent extends JComponent {
    /** An private attribute that contains the information about the bar chart */
    private BarChart barChart;

    /** A constant that defines the gap between text description and numbers */
    private static final int GAP_TEXT_AND_NUMBERS = 10;

    /** A constant that defines the lenght of the small line on the chart */
    private static final int SMALL_LINE_LENGTH = 7;

    /** A constant that defines the gap between numbers and small lines */
    private static final int GAP_NUMBER_AND_SMALL_LINE = 5;

    /** A constant that defines the triangle dimensions */
    private static final int TRIANGLE_HEIGHT = 12;

    /** A constant that defines the gap between two bars */
    private static final int GAP_BETWEEN_COLUMNS = 1;

    /**
     * A constructor that creates a {@link BarChartComponent} from the given params.
     * Throws {@link NullPointerException} f null was given
     *
     * @param barChart the {@link BarChart} object that contains information about
     *                 the bar chart
     *
     * @throws NullPointerException if null was given
     */
    public BarChartComponent(BarChart barChart) {
        if (barChart == null)
            throw new NullPointerException("Bar chart can not be null");
        this.barChart = barChart;
    }

    /**
     * A method that defines how the component will be paintet.
     *
     * @param g the object used for paintig
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // get the insets and dimensions so we can draw the graph regarding these
        // parameters
        Insets ins = getInsets();
        Dimension dim = getSize();

        // the availible height for drawing
        double availableHeight = dim.getHeight() - ins.top - ins.bottom;

        // swing gurantees that the Graphics object will be a Graphics2D so cast to get
        // more methods
        Graphics2D g2d = (Graphics2D) g;

        // get the font metrics for this graphic
        FontMetrics fm = g.getFontMetrics();

        // create a vertical drawing direction
        AffineTransform defaultAt = g2d.getTransform();
        AffineTransform at = new AffineTransform();
        at.rotate(-Math.PI / 2);

        // set the vertical drawing direction
        g2d.setTransform(at);

        // determine the coordintes of the text
        int x = ins.left + fm.getAscent();
        int y = -(int) ((dim.getHeight() + fm.stringWidth(barChart.getyDescription())) / 2);

        // draw the text
        g2d.drawString(barChart.getyDescription(), y, x);

        // reset the direction
        g2d.setTransform(defaultAt);

        // get the starting coordinates of the numbers on the y axis
        int numberX = ins.left + fm.getAscent() + GAP_TEXT_AND_NUMBERS;
        int numberY = (int) ((dim.getHeight() - ins.bottom - 2 * fm.getHeight()));

        // a variable that indicates the lenght of the biggest number
        int biggestNumber = 0;

        // a varibale that contains the number of horizontal lines
        int numberOfHorizontalLines = (barChart.getMaxY() - barChart.getMinY()) / barChart.getyDistance();

        // calculate the gap between horizontal lines
        double distance = (availableHeight - 3 * fm.getHeight() - TRIANGLE_HEIGHT) / (numberOfHorizontalLines);

        // get the width of the longest number
        for (int i = 0; i <= numberOfHorizontalLines; i++) {
            String number = String.valueOf(barChart.getMinY() + i * barChart.getyDistance());
            int width = fm.stringWidth(number);
            if (width > biggestNumber)
                biggestNumber = width;
        }

        // determine the x coordinate of the small lines on the y axis
        int smallLineX = numberX + biggestNumber + GAP_NUMBER_AND_SMALL_LINE;

        // draw each number and horizontal line
        for (int i = 0; i <= numberOfHorizontalLines; i++) {
            // first draw number
            g2d.setColor(Color.BLACK);
            String number = String.valueOf(barChart.getMinY() + i * barChart.getyDistance());
            g2d.drawString(number, numberX + biggestNumber - fm.stringWidth(number),
                    (int) (numberY - i * distance));

            // based on the font get the center of the number so we can draw the small line
            int smallLineY = (int) (numberY - i * distance - fm.getAscent() / 2 + 2);

            // draw small line line
            g2d.setColor(Color.GRAY);
            Line2D line = new Line2D.Float(smallLineX, smallLineY,
                    (float) (smallLineX + SMALL_LINE_LENGTH),
                    smallLineY);
            g2d.draw(line);

            // draw the horizontal lines
            g2d.setColor(Color.ORANGE);
            Line2D horizontLine = new Line2D.Float(smallLineX + SMALL_LINE_LENGTH, smallLineY,
                    (float) (dim.getWidth() - TRIANGLE_HEIGHT),
                    smallLineY);
            g2d.draw(horizontLine);
        }

        // draw the y axis
        g2d.setColor(Color.GRAY);
        int verticalLineX = smallLineX + SMALL_LINE_LENGTH;
        Line2D verticalLine = new Line2D.Float(verticalLineX, numberY - fm.getAscent() / 2 + 2, verticalLineX,
                TRIANGLE_HEIGHT);
        g2d.draw(verticalLine);

        // draw the x axis
        Line2D horizontalLine = new Line2D.Float(verticalLineX, numberY - fm.getAscent() / 2 + 2,
                (float) (dim.getWidth() - TRIANGLE_HEIGHT),
                numberY - fm.getAscent() / 2 + 2);
        g2d.draw(horizontalLine);

        // draw the triangle on the y axis
        Triangle yTriangle = new Triangle(
                new Point2D.Double(verticalLineX - TRIANGLE_HEIGHT / 2, TRIANGLE_HEIGHT),
                new Point2D.Double(verticalLineX + TRIANGLE_HEIGHT / 2, TRIANGLE_HEIGHT),
                new Point2D.Double(verticalLineX, 0));
        g2d.fill(yTriangle);

        // draw the triangle on the x axis
        Triangle xTriangle = new Triangle(
                new Point2D.Double(dim.getWidth() - TRIANGLE_HEIGHT,
                        numberY - fm.getAscent() / 2 + 2 - TRIANGLE_HEIGHT / 2),
                new Point2D.Double(dim.getWidth() - TRIANGLE_HEIGHT,
                        numberY - fm.getAscent() / 2 + 2 + TRIANGLE_HEIGHT / 2),
                new Point2D.Double(dim.getWidth(),
                        numberY - fm.getAscent() / 2 + 2));
        g2d.fill(xTriangle);

        g2d.setColor(Color.BLACK);

        // draw the description of the x axis on the bottom
        g2d.drawString(barChart.getxDescription(),
                (int) (dim.getWidth() - fm.stringWidth(barChart.getxDescription())) / 2,
                (int) dim.getHeight() - fm.getDescent());

        // get the number of columns
        int numOfColumns = barChart.getValues().size();

        // calculate the width of each column
        double columnWidth = (dim.getWidth() - verticalLineX - TRIANGLE_HEIGHT - SMALL_LINE_LENGTH)
                / numOfColumns
                - GAP_BETWEEN_COLUMNS;

        // counter of columns used for determining x coordinate of the column
        int counter = 0;

        y = numberY - fm.getAscent() / 2 + 2;
        // for each bar chart information draw a column
        for (XYValue value : barChart.getValues()) {
            // determine the top right corner of the column
            x = (int) (verticalLineX + counter * (columnWidth + GAP_BETWEEN_COLUMNS)) + 1;

            // calculate the height of the column
            int height = (int) (value.getY() * 1.f / barChart.getyDistance() * 1.f * distance);

            g2d.setColor(Color.decode("#ff4d01"));
            // draw the column
            g2d.fillRect(x, y - height - 1, (int) columnWidth, height + 1);

            // draw the value of the column
            g2d.setColor(Color.BLACK);
            String text = String.valueOf(value.getX());
            g2d.drawString(text, (int) (x + columnWidth / 2 - fm.stringWidth(text) / 2),
                    y + SMALL_LINE_LENGTH + fm.getAscent());

            // draw the small lines on the x axis
            g2d.setColor(Color.GRAY);
            if (counter == 0)
                x--;
            Line2D small = new Line2D.Float(x, y + SMALL_LINE_LENGTH, x, y);
            g2d.draw(small);

            if (counter > 0) {
                // draw the vertical line parralel with the y axis
                g2d.setColor(Color.ORANGE);
                Line2D parralelLine = new Line2D.Float(x, y, x, TRIANGLE_HEIGHT);
                g2d.draw(parralelLine);
            }

            // go to the next column
            counter++;
        }
        // determine the top right corner of the column
        x = (int) (verticalLineX + counter * (columnWidth + GAP_BETWEEN_COLUMNS));

        // draw the small lines on the x axis
        g2d.setColor(Color.GRAY);
        Line2D small = new Line2D.Float(x,
                y + SMALL_LINE_LENGTH, x, y);
        g2d.draw(small);

        // draw the vertical line parralel with the y axis
        g2d.setColor(Color.ORANGE);
        Line2D parralelLine = new Line2D.Float(x, y, x, TRIANGLE_HEIGHT);
        g2d.draw(parralelLine);

    }

    /**
     * A private nested class that represents a triangle in swing
     *
     * @author Marko Tunjić
     */
    private static class Triangle extends Path2D.Double {
        /**
         * A constructor that creates a Triangle from the given Point2D array which
         * represents the cornesrs of the triangle. Throws NullPointerException if null
         * was given, or IllegalArgumentException if there were !=3 points
         *
         * @param points the corners of the triangle
         *
         * @throws NullPointerException     if null was given
         * @throws IllegalArgumentException if there were !=3 points
         */
        public Triangle(Point2D... points) {
            if (points == null)
                throw new NullPointerException("Poits can not be null");
            if (points.length != 3)
                throw new IllegalArgumentException("A triangle must have 3 points");
            moveTo(points[0].getX(), points[0].getY());
            lineTo(points[1].getX(), points[1].getY());
            lineTo(points[2].getX(), points[2].getY());
            closePath();
        }
    }
}
