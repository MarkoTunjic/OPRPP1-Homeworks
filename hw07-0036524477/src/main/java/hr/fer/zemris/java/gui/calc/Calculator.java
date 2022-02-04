package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Container;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.calc.buttons.BinaryButton;
import hr.fer.zemris.java.gui.calc.buttons.DigitButton;
import hr.fer.zemris.java.gui.calc.buttons.UnaryButton;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

/**
 * A class that draws the calculator onto the frame
 *
 * @author Marko Tunjić
 */
public class Calculator extends JFrame {
    /** an private attribute that contains the reference to the calculator */
    private CalcModel calc;

    /** an private attribute that contains the current state */
    private CalcState state;

    /** an private attribute that contains the reference to the stack */
    private Deque<Double> stack;

    /**
     * an private attribute that contains all of the inversible buttons
     */
    private JButton[] inversibleButtons;

    /** A constructor that initializes the drawing */
    public Calculator() {
        // create calc
        calc = new CalcImpl();

        // create stack
        stack = new ArrayDeque<>();

        // allocate space for inversible buttons
        inversibleButtons = new JButton[8];

        // initialize initial state
        this.state = CalcState.NORMAL;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        initGUI();
        pack();
    }

    private void initGUI() {
        // set the layout
        Container cp = getContentPane();
        cp.setLayout(new CalcLayout(3));

        // draw the display
        JLabel display = new JLabel(calc.toString(), SwingConstants.RIGHT);
        display.setFont(display.getFont().deriveFont(30f));
        display.setBackground(Color.YELLOW);
        display.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        display.setOpaque(true);

        // add the listener that changes the JLabel based on the calculator value
        calc.addCalcValueListener((c) -> display.setText(calc.toString()));

        // add the component to the frame
        cp.add(display, new RCPosition(1, 1));

        // add the equals button with the adequate operation for it
        addButton("=", () -> {
            double result = calc.getPendingBinaryOperation().applyAsDouble(calc.getActiveOperand(),
                    calc.getValue());
            calc.setValue(result);
            calc.clearActiveOperand();
            calc.setPendingBinaryOperation(null);
        }, new RCPosition(1, 6), cp);

        // add the clr button with the adequate operation for it
        addButton("clr", () -> calc.clear(), new RCPosition(1, 7), cp);

        // add the reset button with the adequate operation for it
        addButton("reset", () -> {
            calc.clearAll();
            stack.clear();
        }, new RCPosition(2, 7), cp);

        // add the push button with the adequate operation for it
        addButton("push", () -> stack.push(calc.getValue()), new RCPosition(3, 7), cp);

        // add the pop button with the adequate operation for it
        addButton("pop", () -> calc.setValue(stack.pop()), new RCPosition(4, 7), cp);

        // create the checkbox for changing state
        JCheckBox stateBox = new JCheckBox("Inv");
        stateBox.addActionListener(e -> {
            AbstractButton abstractButton = (AbstractButton) e.getSource();
            boolean selected = abstractButton.getModel().isSelected();
            if (selected)
                this.state = CalcState.INVERSE;
            else
                this.state = CalcState.NORMAL;
            setInversibleButtons(cp);
        });
        cp.add(stateBox, new RCPosition(5, 7));

        // add the inversible buttons
        setInversibleButtons(cp);

        // add the digit buttons
        setDigitButtons(cp);

        // add the binary operatrs
        setBinaryButtons(cp);

        // add the sign change button
        JButton sign = new JButton("+/-");
        sign.addActionListener(e -> calc.swapSign());

        cp.add(sign, new RCPosition(5, 4));
    }

    /**
     * A method that draws the binary buttons onto the given container
     *
     * @param cp the container that the components will be drawn on
     */
    private void setBinaryButtons(Container cp) {
        JButton buttons[] = new JButton[4];
        buttons[3] = new BinaryButton("+", Double::sum, calc);
        buttons[2] = new BinaryButton("-", (x, y) -> x - y, calc);
        buttons[1] = new BinaryButton("*", (x, y) -> x * y, calc);
        buttons[0] = new BinaryButton("/", (x, y) -> x / y, calc);
        for (int i = 0; i < 4; i++)
            cp.add(buttons[i], new RCPosition(i + 2, 6));
    }

    /**
     * A method that draws the digit buttons onto the given container
     *
     * @param cp the container that the components will be drawn on
     */
    private void setDigitButtons(Container cp) {
        // ad zero and dot
        JButton zero = new DigitButton("0", calc);
        JButton dot = new DigitButton(".", calc);
        zero.setFont(zero.getFont().deriveFont(30f));
        dot.setFont(dot.getFont().deriveFont(30f));
        cp.add(zero, new RCPosition(CalcLayout.MAX_ROW_COUNT, 3));
        cp.add(dot, new RCPosition(CalcLayout.MAX_ROW_COUNT, 5));

        // row add 3 numbers
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // calculate the current number
                int current = i * 3 + j + 1;

                // create and add the current digit button
                JButton digit = new DigitButton(String.valueOf(current), calc);
                digit.setFont(digit.getFont().deriveFont(30f));
                cp.add(digit, new RCPosition(CalcLayout.MAX_ROW_COUNT - i - 1, j + 3));
            }
        }
    }

    /**
     * A method that draws the inversible buttons onto the given container
     *
     * @param cp the container that the components will be drawn on
     */
    private void setInversibleButtons(Container cp) {
        for (JButton button : inversibleButtons)
            if (button != null)
                cp.remove(button);

        // create all of the inversible operations
        inversibleButtons[0] = new UnaryButton("1/x", "1/x", x -> 1 / x, x -> 1 / x, state, calc);
        inversibleButtons[1] = new UnaryButton("sin", "arcsin", x -> Math.sin(x), x -> Math.asin(x), state, calc);
        inversibleButtons[2] = new UnaryButton("log", "10^x", x -> Math.log10(x), x -> Math.pow(10, x), state, calc);
        inversibleButtons[3] = new UnaryButton("cos", "arccos", x -> Math.cos(x), x -> Math.acos(x), state, calc);
        inversibleButtons[4] = new UnaryButton("ln", "e^x", x -> Math.log(x), x -> Math.exp(x), state, calc);
        inversibleButtons[5] = new UnaryButton("tan", "arctan", x -> Math.tan(x), x -> Math.atan(x), state, calc);

        // btn 7 is special becouse it is a binary operation
        BinaryButton btn7 = null;
        switch (state) {
            case INVERSE:
                btn7 = new BinaryButton("x^(1/n)", (x, n) -> Math.pow(x, 1 / n), calc);
                break;
            case NORMAL:
                btn7 = new BinaryButton("x^n", (x, n) -> Math.pow(x, n), calc);
                break;
            default:
                break;
        }
        inversibleButtons[6] = btn7;

        inversibleButtons[7] = new UnaryButton("ctg", "arcctg", x -> 1 / Math.tan(x), x -> Math.PI / 2 - Math.atan(x),
                state,
                calc);

        // add the buttons to the container
        for (int i = 0; i < inversibleButtons.length / 2; i++) {
            int j = i * 2;
            cp.add(inversibleButtons[j], new RCPosition(i + 2, 1));
            cp.add(inversibleButtons[j + 1], new RCPosition(i + 2, 2));
        }

        // request repainting
        cp.revalidate();
        cp.repaint();
    }

    /**
     * A method that draws the special buttons onto the given container
     *
     * @param cp the container that the components will be drawn on
     */
    private void addButton(String text, Runnable task, RCPosition position, Container cp) {
        JButton button = new JButton(text);
        button.addActionListener(e -> task.run());
        cp.add(button, position);
    }

    /** A main method that initializes the GUI */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Calculator().setVisible(true);
        });
    }
}
