package hr.fer.zemris.java.gui.layouts;

import java.awt.Container;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * A class that creates a {@link CalcLayout} and puts it in a frame
 *
 * @author Marko Tunjić
 */
public class DemoFrame1 extends JFrame {

    /** A constructor that initializes the drawing */
    public DemoFrame1() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        initGUI();
        pack();
    }

    /** A method that initializes all components and adds them into the frame */
    private void initGUI() {
        // get the content pane
        Container cp = getContentPane();
        // set the layout type
        cp.setLayout(new CalcLayout(3));

        // add the components to the layout
        cp.add(l("tekst 1"), new RCPosition(1, 1));
        cp.add(l("tekst 2"), new RCPosition(2, 3));
        cp.add(l("tekst stvarno najdulji"), new RCPosition(2, 7));
        cp.add(l("tekst kraći"), new RCPosition(4, 2));
        cp.add(l("tekst srednji"), new RCPosition(4, 5));
        cp.add(l("tekst"), new RCPosition(4, 7));
    }

    /**
     * A method that creates a JLabel from the given text
     *
     * @param text the text to be put into the JLabel
     * @return the JLabel with the given text
     */
    private JLabel l(String text) {
        JLabel l = new JLabel(text);
        l.setBackground(Color.YELLOW);
        l.setOpaque(true);
        return l;
    }

    /** A main method that starts the application */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DemoFrame1().setVisible(true);
        });
    }
}
