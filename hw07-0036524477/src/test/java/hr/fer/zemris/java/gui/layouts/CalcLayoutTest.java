package hr.fer.zemris.java.gui.layouts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.Container;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.junit.jupiter.api.Test;

public class CalcLayoutTest {
    private static class DemoFrame1 extends JFrame {
        public DemoFrame1() {
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            setSize(500, 500);
        }
    }

    @Test
    void testAddToContainerWhenPassingTooBigRow() {
        assertThrows(CalcLayoutException.class, () -> {
            JFrame a = new DemoFrame1();
            Container pane = a.getContentPane();
            pane.setLayout(new CalcLayout(3));
            pane.add(l("1, 1"), new RCPosition(6, 1));
        });
    }

    @Test
    void testAddToContainerWhenPassingTooBigColumn() {
        assertThrows(CalcLayoutException.class, () -> {
            JFrame a = new DemoFrame1();
            Container pane = a.getContentPane();
            pane.setLayout(new CalcLayout(3));
            pane.add(l("1, 1"), new RCPosition(1, 8));
        });
    }

    @Test
    void testAddToContainerWhenPassingIllegalChild() {
        assertThrows(CalcLayoutException.class, () -> {
            JFrame a = new DemoFrame1();
            Container pane = a.getContentPane();
            pane.setLayout(new CalcLayout(3));
            pane.add(l("1, 1"), new RCPosition(1, 2));
        });
    }

    @Test
    void testAddToContainerWhenAdding2SamePositions() {
        assertThrows(CalcLayoutException.class, () -> {
            JFrame a = new DemoFrame1();
            Container pane = a.getContentPane();
            pane.setLayout(new CalcLayout(3));
            pane.add(l("1, 1"), new RCPosition(1, 1));
            pane.add(l("1, 1"), new RCPosition(1, 1));
        });
    }

    @Test
    void testWidthOfLayout() {
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel("");
        l1.setPreferredSize(new Dimension(10, 30));
        JLabel l2 = new JLabel("");
        l2.setPreferredSize(new Dimension(20, 15));
        p.add(l1, new RCPosition(2, 2));
        p.add(l2, new RCPosition(3, 3));
        Dimension dim = p.getPreferredSize();
        assertEquals(152, dim.getWidth());
    }

    @Test
    void testHeightOfLayout() {
        JPanel p = new JPanel(new CalcLayout(2));
        JLabel l1 = new JLabel("");
        l1.setPreferredSize(new Dimension(10, 30));
        JLabel l2 = new JLabel("");
        l2.setPreferredSize(new Dimension(20, 15));
        p.add(l1, new RCPosition(2, 2));
        p.add(l2, new RCPosition(3, 3));
        Dimension dim = p.getPreferredSize();
        assertEquals(158, dim.getHeight());
    }

    private static JLabel l(String text) {
        JLabel l = new JLabel(text);
        l.setBackground(Color.YELLOW);
        l.setOpaque(true);
        return l;
    }
}
