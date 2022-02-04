package hr.fer.zemris.java.fractals.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hr.fer.zemris.math.Complex;

/**
 * A class used for loading data from command line
 *
 * @author Marko Tunjić
 */
public class Loader {

    /**
     * A method that loads complex roots from the command line and returns a list of
     * all eneterd complex numbers. Loading stops when "done" is enered. Minimum of
     * roots is 2. The format is:
     * <number> <binary operator> <i><number>
     * example: i, 1, 1 + i10, i10,- i10, 1 + i
     *
     * @return list of all enetered complex numbers
     */
    public static List<Complex> loadRoots() {
        // list of all complex numbers
        List<Complex> numbers = new ArrayList<>();

        // counter to display which root is currently being eneterd
        int counter = 1;

        // scanner for reading data
        Scanner sc = new Scanner(System.in);

        // string to save loaded line
        String line;

        // message
        System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");

        // do while "done" is not enered or while less than 2 roots are enetered
        do {
            // message
            System.out.print(String.format("Root %d> ", counter));

            // read line
            line = sc.nextLine();

            // check if "done" before 2 roots
            if (line.equals("done")) {
                System.out.println("At least 2 arguments required");
                continue;
            }

            // get all the parts of the format
            String[] spliitedLine = line.split(" ");

            // check if legal format
            if (spliitedLine.length > 3 || spliitedLine.length <= 0) {
                System.out.println(
                        "Invalid format a complex number has only 3 or less parts: <number> <binary operator> <[i]number> ");
                continue;
            }

            // if one part of the complex number were enetered
            if (spliitedLine.length == 1) {
                // if only imaginary part was enetered
                if (spliitedLine[0].contains("i")) {

                    // check if i on first place
                    if (!(spliitedLine[0].charAt(0) == 'i')) {
                        System.out.println("Invalid fomrat. i can only be on the first place of imagnary part");
                        continue;
                    }

                    // if only i was enetered
                    if (spliitedLine[0].length() == 1)
                        numbers.add(new Complex(0, 1));

                    // if there is something behind i
                    else {

                        // check if there are not allowed chars after i
                        if (!Character.isDigit(spliitedLine[0].charAt(1))) {
                            System.out.println("Invalid fomrat. Part after i must contain only numbers ");
                            continue;
                        }

                        // try parsing
                        try {
                            numbers.add(new Complex(Double.parseDouble(spliitedLine[0].substring(1)), 0));
                        }
                        // if NaN
                        catch (NumberFormatException ex) {
                            System.out.println("Invalid format: the part after \"i\" must be a double");
                            continue;
                        }
                    }
                }
                // only real part was enetered
                else {
                    // try parsing
                    try {
                        numbers.add(new Complex(Double.parseDouble(spliitedLine[0]), 0));
                    }
                    // if NaN
                    catch (NumberFormatException ex) {
                        System.out.println("Invalid format: could not parse string to double");
                        continue;
                    }
                }

                // go to next root
                counter++;
            }
            // if two parts of the complex number were enetered
            else if (spliitedLine.length == 2) {

                // first part must be a binary operator aka +/-
                boolean positive;
                if (spliitedLine[0].equals("+"))
                    positive = true;
                else if (spliitedLine[0].equals("-"))
                    positive = false;
                else {
                    System.out.println("Invalid format before imaginary part only + and - are allowed.");
                    continue;
                }

                // check if imaganiry part was given
                if (!spliitedLine[1].contains("i")) {
                    System.out.println("Invalid fomrat. after + and - only imagniray part is allowed");
                    continue;
                }

                // check if i on first place
                if (!(spliitedLine[1].charAt(0) == 'i')) {
                    System.out.println("Invalid fomrat. i can only be on the first place of imagnary part");
                    continue;
                }

                // check if only "i" was given
                if (spliitedLine[1].length() == 1)
                    numbers.add(new Complex(0, positive ? 1 : -1));

                else {
                    // check if there are not allowed chars after i
                    if (!Character.isDigit(spliitedLine[1].charAt(1))) {
                        System.out.println("Invalid fomrat. Part after i must contain only numbers ");
                        continue;
                    }
                    // try parsing
                    try {
                        double number = Double.parseDouble(spliitedLine[1].substring(1));

                        numbers.add(new Complex(0, positive ? number : -number));
                    }
                    // if NaN
                    catch (NumberFormatException ex) {
                        System.out.println("Invalid format: could not parse string to double");
                        continue;
                    }
                }
                counter++;
            }
            // if all 3 parts were enetered
            else {
                // try parsing numbers
                try {
                    // first number
                    double first = Double.parseDouble(spliitedLine[0]);

                    // binary operator
                    boolean positive;
                    if (spliitedLine[1].equals("+"))
                        positive = true;
                    else if (spliitedLine[1].equals("-"))
                        positive = false;
                    else {
                        System.out.println("Invalid format: only + and - are allowed after real part");
                        continue;
                    }

                    // check if imaginary was given
                    if (!spliitedLine[2].contains("i")) {
                        System.out.println("Invalid format");
                        continue;
                    }

                    // check if imaganiry part was given
                    if (!spliitedLine[2].contains("i")) {
                        System.out.println("Invalid fomrat. after + and - only imagniray part is allowed");
                        continue;
                    }

                    // check if i on first place
                    if (!(spliitedLine[2].charAt(0) == 'i')) {
                        System.out.println("Invalid fomrat. i can only be on the first place of imagnary part");
                        continue;
                    }

                    // second number
                    double second;

                    // check if only "i" was given
                    if (spliitedLine[2].length() == 1)
                        second = 1;

                    // something after "i"
                    else {

                        // check if there are not allowed chars after i
                        if (!Character.isDigit(spliitedLine[2].charAt(1))) {
                            System.out.println("Invalid fomrat. Part after i must contain only numbers ");
                            continue;
                        }

                        second = Double.parseDouble(spliitedLine[2].substring(1));

                    }

                    // change positivity if needed
                    second = positive ? second : -second;

                    // add new root and go tothe next
                    numbers.add(new Complex(first, second));
                    counter++;
                }
                // if NaN
                catch (NumberFormatException | NullPointerException | ArrayIndexOutOfBoundsException ex) {
                    System.out.println("Invalid format");
                }
            }
        } while (!line.equals("done") || counter < 3);

        // close resources and return result
        sc.close();
        return numbers;
    }
}
