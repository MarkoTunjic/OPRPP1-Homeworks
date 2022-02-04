package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.EmptyStackException;
import hr.fer.oprpp1.custom.collections.ObjectStack;

/**
 * A class that tests the implementation of the class ObjectStack by evaluating
 * a postfix expression
 *
 * @author Marko Tunjić
 */
public class StackDemo {
	/**
	 * Main method that one argument in the command line if more or less are given a
	 * message will be displayed. The argument represents a mathematical expression
	 * we want to solve. If the expression is invalid or does illegal operations a
	 * message will be displayed
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// check if only 1 argument was given
		if (args.length < 1) {
			System.out.println("Too few arguments");
		} else if (args.length > 1)
			System.out.println("Too many arguments");
		else {
			// try to evaluate the expression
			try {
				evaluate(args[0]);
			} // invalid expression if not enough elements on stack or not supported variables
				// or operators given
			catch (EmptyStackException | IllegalArgumentException ex) {
				System.out.println("Invalid expression");
			} // division by zero
			catch (ArithmeticException e) {
				System.out.println("Division by zero is not allowed");
			}
		}

	}

	/**
	 * A method that evaluates a postfix expression using a stack
	 *
	 * @param expression the expression to be evaluated
	 */
	private static void evaluate(String expression) {
		// split the expression
		String[] splittedExpression = expression.split(" ");

		// create a stack
		ObjectStack stack = new ObjectStack();

		// go through every element of the expression
		for (String element : splittedExpression) {
			// check if not a space
			if (!element.equals("")) {
				// try pushing a number on the stack
				try {
					stack.push(Integer.parseInt(element));
				} catch (NumberFormatException ex) {
					// if its not a number check if it is a operator and calculate if sto
					calculate(stack, element);
				}
			}
		}

		// stack doesn0t contain only the solution then the expression was invalid
		if (stack.size() != 1) {
			System.out.println("Invalid Expression");
		} else {
			System.out.println(stack.pop());
		}
	}

	/**
	 * A method that calculates a simple operation by taking the operands from the
	 * stack and applying the operator from the element variable. if the element is
	 * a invalid operator a Illegal Argument exception is thrown.
	 *
	 * @param stack   the stack from which we take the operands and push the
	 *                solution
	 * @param element the operator which describes the operation to be completed
	 * @throws IllegalArgumentException when the element is not a supported operator
	 */
	private static void calculate(ObjectStack stack, String element) {
		// define operands
		int first;
		int second;

		// check which operator is to be executed
		switch (element) {
		case "+":
			// get the operands from the stack
			second = (int) stack.pop();
			first = (int) stack.pop();

			// execute the command
			stack.push(first + second);
			break;
		case "-":
			// get the operands from the stack
			second = (int) stack.pop();
			first = (int) stack.pop();

			// execute the command
			stack.push(first - second);
			break;
		case "*":
			// get the operands from the stack
			second = (int) stack.pop();
			first = (int) stack.pop();

			// execute the command
			stack.push(first * second);
			break;
		case "/":
			// get the operands from the stack
			second = (int) stack.pop();
			first = (int) stack.pop();

			// execute the command
			stack.push(first / second);
			break;
		case "%":
			// get the operands from the stack
			second = (int) stack.pop();
			first = (int) stack.pop();

			// execute the command
			stack.push(first % second);
			break;
		default:
			// not supported operator
			throw new IllegalArgumentException();
		}
	}
}
