package hr.fer.zemris.java.gui.charts;

import java.awt.Container;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * A class that draws a {@link BarChartComponent} loaded from a file
 *
 * @author Marko Tunjić
 */
public class BarChartDemo extends JFrame {
    /**
     * A constructor that initializes the drawing. Throws
     * {@link NullPointerException} if null was given
     *
     * @param barChart the bar chart to be drawn
     */
    public BarChartDemo(BarChart barChart, String path) {
        if (barChart == null)
            throw new NullPointerException("Bar chart can not be null");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        initGUI(barChart, path);
        setSize(500, 500);
    }

    /**
     * A method that draws the given bar chart.
     *
     * @param barChart the bar chart to be drawn
     */
    private void initGUI(BarChart barChart, String path) {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(new JLabel(path, SwingConstants.CENTER), BorderLayout.PAGE_START);
        cp.add(new BarChartComponent(barChart));
    }

    /**
     * A main method that loads the data from the file given in the command line
     * arguments and creates a frame for drawing.
     *
     * @param args te command line arguments there should be only one and that is
     *             the path to the file
     */
    public static void main(String[] args) {
        // try reading the lines from the file
        String[] lines = new String[6];
        try (BufferedReader br = Files.newBufferedReader(Path.of(args[0]))) {
            for (int i = 0; i < 6; i++)
                lines[i] = br.readLine();

        }
        // could not open file
        catch (IOException e) {
            System.out.println("Could not read given file");
            return;
        }
        // not enough lines or no command line arguments
        catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Either a argument was not given or not enough lines to read in the file");
            return;
        }
        // create chart data from the file
        String xName = lines[0];
        String yName = lines[1];

        // expected fromat is [(x,y),(x,y)]
        List<XYValue> values = parse(lines[2]).stream().map(element -> {
            String[] splittedElement = element.split(",");
            return new XYValue(Integer.parseInt(splittedElement[0]), Integer.parseInt(splittedElement[1]));
        }).toList();
        int minY = Integer.parseInt(lines[3]);
        int maxY = Integer.parseInt(lines[4]);
        int yDistance = Integer.parseInt(lines[5]);

        // create the frame
        SwingUtilities.invokeLater(() -> {
            new BarChartDemo(new BarChart(values, xName, yName, minY, maxY, yDistance), args[0]).setVisible(true);
        });
    }

    /**
     * A method that parses the given string into tokens dvided with whitespaces
     *
     * @param line the line to be parsed
     *
     * @return the tokens from the line
     */
    private static List<String> parse(String line) {
        // get the characters of the string
        char[] chars = line.toCharArray();

        int counter = 0;
        List<String> solution = new ArrayList<>();

        // go throught each character and if not whitespace create new token
        while (counter < chars.length) {
            StringBuilder sb = new StringBuilder();

            // skip whitespaces
            while (counter < chars.length && Character.isWhitespace(chars[counter]))
                counter++;

            // add token characters
            while (counter < chars.length && !Character.isWhitespace(chars[counter]))
                sb.append(chars[counter++]);

            // add new token to solution
            if (sb.length() > 0)
                solution.add(sb.toString());
        }
        return solution;
    }

}
