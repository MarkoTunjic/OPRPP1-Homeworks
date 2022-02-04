package hr.fer.zemris.java.gui.prim;

import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * A class that is a GUI that creates Prim numbers
 *
 * @author Marko Tunjić
 */
public class PrimDemo extends JFrame {
    /** A deafult constructors that initializes the GUI */
    public PrimDemo() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        initGui();
    }

    /** A private method that initializes the GUI components */
    private void initGui() {
        // get content pane
        Container cp = this.getContentPane();
        cp.setLayout(new BorderLayout());

        // create the two text areas
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        PrimListModel lm = new PrimListModel();
        centerPanel.add(new JScrollPane(new JList<>(lm)));
        centerPanel.add(new JScrollPane(new JList<>(lm)));
        cp.add(centerPanel, BorderLayout.CENTER);

        // add a button and the action listener
        JButton next = new JButton("next");
        next.addActionListener(e -> lm.next());
        cp.add(next, BorderLayout.PAGE_END);
    }

    /**
     * A main method that starts the GUI
     *
     * @param should be empty or ignored
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PrimDemo().setVisible(true);
        });
    }
}
