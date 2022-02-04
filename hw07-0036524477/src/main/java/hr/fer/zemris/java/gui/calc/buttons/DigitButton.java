package hr.fer.zemris.java.gui.calc.buttons;

import javax.swing.JButton;

import hr.fer.zemris.java.gui.calc.CalcModel;

/**
 * A class that represents a button that inserts digits and dots
 *
 * @author Marko Tunjić
 */
public class DigitButton extends JButton {
    /**
     * A constructor that creates a {@link DigitButton} from the given paramas.
     * Throws NullPointerException if null was given
     *
     * @param text the text on the button
     * @param calc the calculator on which the operation will be done
     *
     * @throws NullPointerException if null was given
     */
    public DigitButton(String text, CalcModel calc) {
        super(text);
        if (text == null || calc == null)
            throw new NullPointerException("Text and calc can not be null");
        if (!text.equals("."))
            this.addActionListener((e) -> calc.insertDigit(Integer.parseInt(text)));
        else
            this.addActionListener((e) -> calc.insertDecimalPoint());
    }
}
