package hr.fer.zemris.java.gui.calc.buttons;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.util.function.DoubleUnaryOperator;

import javax.swing.JButton;

import hr.fer.zemris.java.gui.calc.CalcModel;
import hr.fer.zemris.java.gui.calc.CalcState;

/**
 * A class that represents a button that does unary operations
 *
 * @author Marko Tunjić
 */
public class UnaryButton extends JButton {
    private String text1;
    private String text2;

    /**
     * A constructor that creates a {@link UnaryButton} from the given paramas.
     * Throws NullPointerException if null was given params
     *
     * @param text1 the normal state text
     * @param text2 the inverse state text
     * @param op1   the normal state operation
     * @param op2   the inverse state operation
     * @param state the current calculator state
     * @param calc  the calculator on which the operation will be done
     *
     * @throws NullPointerException if null was given
     */
    public UnaryButton(String text1, String text2, DoubleUnaryOperator op1, DoubleUnaryOperator op2, CalcState state,
            CalcModel calc) {
        super(state.equals(CalcState.INVERSE) ? text2 : text1);
        this.text1 = text1;
        this.text2 = text2;

        if (text1 == null || text2 == null || op1 == null || op2 == null || state == null || calc == null)
            throw new NullPointerException("Texts operations state and calc can not be null!");

        this.addActionListener((e) -> {
            // if we started a new binary operation before clicking equals calculate the
            // result of the last operation
            if (calc.isActiveOperandSet()) {
                double result = calc.getPendingBinaryOperation().applyAsDouble(calc.getActiveOperand(),
                        calc.getValue());
                calc.setValue(result);
                calc.clearActiveOperand();
                calc.setPendingBinaryOperation(null);
            }

            switch (state) {
                case INVERSE:
                    calc.setValue(op2.applyAsDouble(calc.getValue()));
                    break;
                case NORMAL:
                    calc.setValue(op1.applyAsDouble(calc.getValue()));
                    break;
                default:
                    break;

            }
        });
    }

    /**
     * A method that returns the prefered size for this button dependet on the
     * longer text that it displays.
     *
     * @return the prefered size for this button dependet on the longer text that it
     *         displays.
     */
    @Override
    public Dimension getPreferredSize() {
        Graphics2D g2d = (Graphics2D) getGraphics();
        Insets insets = getInsets();
        FontMetrics fm = g2d.getFontMetrics();
        Dimension d1 = new Dimension(fm.stringWidth(text1) + insets.left + insets.right,
                fm.getHeight() + insets.top + insets.bottom);
        Dimension d2 = new Dimension(fm.stringWidth(text2) + insets.left + insets.right,
                fm.getHeight() + insets.top + insets.bottom);
        return d1.getWidth() > d2.getWidth() ? d1 : d2;
    }

    /**
     * A method that returns the minimum size for this button which is the prefered
     * size.
     *
     * @return the prefered size for this button dependet on the longer text that it
     *         displays.
     */
    @Override
    public Dimension getMinimumSize() {
        return this.getPreferredSize();
    }
}
