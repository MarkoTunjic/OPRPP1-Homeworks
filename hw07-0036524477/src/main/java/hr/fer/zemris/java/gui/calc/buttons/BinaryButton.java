package hr.fer.zemris.java.gui.calc.buttons;

import java.util.function.DoubleBinaryOperator;

import javax.swing.JButton;

import hr.fer.zemris.java.gui.calc.CalcModel;

/**
 * A class that represents a button that does binary operations
 *
 * @author Marko Tunjić
 */
public class BinaryButton extends JButton {
    /**
     * A constructor that creates a {@link BinaryButton} from the given paramas.
     * Throws NullPointerException if null was given
     *
     * @param text the text on the button
     * @param op   the binary operation
     * @param calc the calculator on which the operation will be done
     *
     * @throws NullPointerException if null was given
     */
    public BinaryButton(String text, DoubleBinaryOperator op, CalcModel calc) {
        super(text);

        if (text == null || op == null || calc == null)
            throw new NullPointerException("Text operation and calc can not be null");

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
            calc.setActiveOperand(calc.getValue());
            if (calc.getValue() - (int) calc.getValue() == 0)
                calc.freezeValue(String.valueOf((int) calc.getValue()));
            else
                calc.freezeValue(String.valueOf(calc.getValue()));
            calc.setPendingBinaryOperation(op);
            calc.clear();
        });
    }
}
