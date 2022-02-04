package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a complex polynomal in format with null points aka
 * roots
 * Z[0]*(Z-Z[1])*(Z-Z[2])*...*(Z-Z[3])
 *
 * @author Marko Tunjić
 */
public class ComplexRootedPolynomial {
    /**
     * An private attribute that represents the initial constant of this polynomal
     */
    private Complex constant;

    /**
     * An private attribute that contains all the roots of this polynomal
     */
    private Complex[] roots;

    /**
     * A constructor that creates a {@link ComplexRootedPolynomial} from the given
     * parameters. Thorws {@link NullPointerException} if null was given
     *
     * @param constant the initial constant
     * @param roots    all of the roots for this polynomal
     */
    public ComplexRootedPolynomial(Complex constant, Complex... roots) {
        if (constant == null || roots == null)
            throw new NullPointerException("Constant and roots can not be null");
        this.constant = constant;
        this.roots = roots;
    }

    /**
     * A method that computes polynomial value at given point z. Throws
     * {@link NullPointerException} if null was given
     *
     * @param z the point in which the value should be calculated
     *
     * @return the value of the polynomal at given point z
     *
     * @throws NullPointerException if null was given
     */
    public Complex apply(Complex z) {
        Complex result = Complex.ONE;
        result = constant.multiply(result);
        for (Complex root : roots)
            result = result.multiply(z.sub(root));
        return result;
    }

    /**
     * A method that converts this representation to {@link ComplexPolynomial} type
     *
     * @return {@link ComplexPolynomial} representation of this
     *         {@link ComplexRootedPolynomial}
     */
    public ComplexPolynomial toComplexPolynom() {
        // new array that represents the factors of the complex polynomal
        Complex[] newArray = new Complex[roots.length + 1];

        // first factor is the constant
        newArray[0] = constant;

        // for each empty factor calculate the factor
        for (int i = 1; i < newArray.length; i++) {
            // all of the possible multiplication combinations
            List<int[]> combinations = generate(roots.length, i);

            // the factor at index i will be a sum of all possbile multiplication
            // combinations
            newArray[i] = Complex.ZERO;

            // for each combination multiply the corresponding complex roots
            for (int[] combination : combinations) {
                // temp variable to store the multiplication result
                Complex help = Complex.ONE;
                for (int index : combination)
                    help = help.multiply(roots[index]);

                // multiply it to the constant
                help = help.multiply(constant);

                // add the combination to the result
                newArray[i] = newArray[i].add(help);
            }
        }

        // return solution
        return new ComplexPolynomial(newArray);
    }

    /**
     * A private method that returnsa list of all possible combinations of 0...n
     * numbers of size: [size]
     *
     * @param n    the possible numbers
     * @param size the size of the combination
     */
    private List<int[]> generate(int n, int size) {
        List<int[]> combinations = new ArrayList<>();

        // recursively calculate all combinations
        helper(combinations, new int[size], 0, n - 1, 0);
        return combinations;
    }

    /**
     * A method that recursively calculates all combinations and when one is
     * completed it is added to the combination list. Throws
     * {@link NullPointerException} if combinations or data are null
     *
     * @param combinations the list of all combinations that this method fills
     * @param data         the current combination that is being filled
     * @param start        the starting number to add to the combination
     * @param end          the number that helps to calculate the ending number to
     *                     add to the combination
     * @param the          index at which the numbers should be added
     */
    private void helper(List<int[]> combinations, int[] data, int start, int end, int index) {
        // check if legal
        if (combinations == null || data == null)
            throw new NullPointerException("Combinatnions and data can not be null");

        // check if combination is complete
        if (index == data.length) {
            int[] combination = data.clone();
            combinations.add(combination);
        }
        // add numbers to combination
        else {
            // determine the ending number
            int max = Math.min(end, end + 1 - data.length + index);

            // for each number from start to end add to the current index and call the next
            // recursion
            for (int i = start; i <= max; i++) {
                data[index] = i;
                helper(combinations, data, i + 1, end, index + 1);
            }
        }
    }

    /**
     * A method that returns the string representation of this
     * {@link ComplexRootedPolynomial} in format
     * Z[0]*(Z-Z[1])*(Z-Z[2])*...*(Z-Z[3]) where z[n] is a complex number
     *
     * @return the string representation of this polynomal
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("(%s)*", constant.toString()));
        for (Complex root : roots)
            sb.append(String.format("(z-(%s))*", root.toString()));
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * A method that finds index of closest root for given complex number z that is
     * within treshold; if there is no such root, returns -1
     * first root has index 0, second index 1, etc. Throws
     * {@link NullPointerException} if z was null;
     *
     * @return the index of the closest root to the given complex number which
     *         distance is within treshold. Or -1 if there is no such root
     *
     * @throws NullPointerException if z was null
     */
    public int indexOfClosestRootFor(Complex z, double treshold) {
        if (z == null)
            throw new NullPointerException("Complex number can not be null");

        // current closest index
        int index = -1;

        // current closest distance
        double distance = Double.MAX_VALUE;

        // for each root calculate distance
        for (int i = 0; i < roots.length; i++) {
            double potential = (roots[i].sub(z).module());

            // check if inside treshold
            boolean inside = potential < treshold;

            // check if distance less then old distance and within treshold
            if (inside && (potential < distance)) {
                index = i;
                distance = potential;
            }
        }
        return index;
    }
}
