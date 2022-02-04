package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a complex number
 *
 * @author Marko Tunjić
 */
public class Complex {
    /** A constant that contains a complex number with re=0 and im=0 */
    public static final Complex ZERO = new Complex(0, 0);

    /** A constant that contains a complex number with re=1 and im=0 */
    public static final Complex ONE = new Complex(1, 0);

    /** A constant that contains a complex number with re=-1 and im=0 */
    public static final Complex ONE_NEG = new Complex(-1, 0);

    /** A constant that contains a complex number with re=0 and im=1 */
    public static final Complex IM = new Complex(0, 1);

    /** A constant that contains a complex number with re=0 and im=-1 */
    public static final Complex IM_NEG = new Complex(0, -1);

    /** An private attribute that represents the real part of a complex number */
    private double re;

    /**
     * An private attribute that represents the imaginary part of a complex number
     */
    private double im;

    /** A constructor that initializes a complex number with re=0 and im=0 */
    public Complex() {
        this.re = 0;
        this.im = 0;
    }

    /**
     * A constructor that initializes a complex number with the given parameters
     *
     * @param re the real part
     * @param im the imaginary part
     */
    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    /**
     * A method that returns the module of this complex number
     *
     * @return the module of this complex number
     */
    public double module() {
        return Math.sqrt(re * re + im * im);
    }

    /**
     * A method that returns a new complex number which represents the result of
     * multiplying this complex number and the given complex number. Throws
     * {@link NullPointerException} if nullwas given
     *
     * @param c the complex number with which this should be multiplied
     *
     * @return a new complex number that is the result of multiplication
     *
     * @throws NullPointerException if null was given
     */
    public Complex multiply(Complex c) {
        if (c == null)
            throw new NullPointerException("Cannot multiply with null");
        double resRe = this.re * c.re - this.im * c.im;
        double resIm = this.re * c.im + this.im * c.re;
        return new Complex(resRe, resIm);
    }

    /**
     * A method that returns a new complex number which represents the result of
     * dividing this complex number and the given complex number. Throws
     * {@link NullPointerException} if nullwas given
     *
     * @param c the complex number with which this should be divided
     *
     * @return a new complex number that is the result of division
     *
     * @throws NullPointerException if null was given
     */
    public Complex divide(Complex c) {
        if (c == null)
            throw new NullPointerException("Cannot divide with null");
        double denominator = c.re * c.re + c.im * c.im;
        double resRe = (this.re * c.re + this.im * c.im) / denominator;
        double resIm = (this.im * c.re - this.re * c.im) / denominator;
        return new Complex(resRe, resIm);
    }

    /**
     * A method that returns a new complex number which represents the result of
     * adding this complex number and the given complex number. Throws
     * {@link NullPointerException} if nullwas given
     *
     * @param c the complex number with which this should be divided
     *
     * @return a new complex number that is the result of division
     *
     * @throws NullPointerException if null was given
     */
    public Complex add(Complex c) {
        if (c == null)
            throw new NullPointerException("Cannot add null");
        return new Complex(this.re + c.re, this.im + c.im);
    }

    /**
     * A method that returns a new complex number which represents the result of
     * subtracting this complex number and the given complex number. Throws
     * {@link NullPointerException} if nullwas given
     *
     * @param c the complex number with which this should be divided
     *
     * @return a new complex number that is the result of division
     *
     * @throws NullPointerException if null was given
     */
    public Complex sub(Complex c) {
        if (c == null)
            throw new NullPointerException("Cannot substract null");
        return new Complex(this.re - c.re, this.im - c.im);
    }

    /**
     * A method that returns a new complex number which represents the result of
     * negating this complex number.
     *
     * @return a new complex number that is the result of negating
     *
     * @throws NullPointerException if null was given
     */
    public Complex negate() {
        return new Complex(-this.re, -this.im);
    }

    /**
     * A method that returns a new complex number which represents the result of
     * powering this complex number to the given number. Throws
     * {@link IllegalArgumentException} given power is less than 0
     *
     * @param n the power
     *
     * @return a new complex number that is the result of powering
     *
     * @throws IllegalArgumentException if number less than zero was given
     */
    public Complex power(int n) {
        // cehck if legal power
        if (n < 0)
            throw new IllegalArgumentException("Power can not be negative");

        // zero return one
        if (n == 0)
            return Complex.ONE;

        // the theta of this complex number
        double theta = this.getTheta();

        // the radius of the powered complex number
        double r = Math.pow(this.module(), n);

        // de moivrev formula
        return new Complex(r * Math.cos(theta * n), r * Math.sin(theta * n));
    }

    /**
     * A method that returns a list of new complex numbers which represent the
     * result of
     * rooting this complex number to the given number. Throws
     * {@link IllegalArgumentException} given root is less than 1
     *
     * @param n the power
     *
     * @return a list of new complex numbers which are the result of rooting
     *
     * @throws IllegalArgumentException if number less than one was given
     */
    public List<Complex> root(int n) {
        // must be positive integer
        if (n < 1)
            throw new IllegalArgumentException("N must be a positive integer");

        // calculate radius for polar coordinates
        double r = this.module();

        // theta of polar coordinates
        double theta = this.getTheta();

        // allocate the result list
        List<Complex> result = new ArrayList<>();

        // prepare the square root of the radius becouse it is a slow operation
        double sqrtR = Math.sqrt(r);

        // for each k in 0....n
        for (int k = 0; k < n; k++) {
            // calculate the argument of cos() and sin()
            double arg = (theta + 2 * Math.PI * k) / n;

            // add the result
            result.add(new Complex(sqrtR * Math.cos(arg), sqrtR * Math.sin(arg)));
        }

        // return
        return result;
    }

    /**
     * A private method that return the theta angle of this complex number
     *
     * @return the theta of this complex number
     */
    private double getTheta() {
        double theta;

        // first or fourth quadrant
        if (re > 0)
            theta = Math.atan(im / re);

        // second or third quadrant
        else if (re < 0)
            theta = Math.atan(im / re) + Math.PI;

        // positive y axis
        else if (re == 0 && im > 0)
            theta = Math.PI / 2;

        // negative y axis
        else if (re == 0 && im < 0)
            theta = Math.PI * 3 / 2;

        // center
        else
            theta = 0;

        return theta;
    }

    /**
     * A method that return a {@link String} representation of this complex number
     * in fomrat <number> <+/-> <[i]number>
     *
     * @return the {@link String} representation of this complex number
     */
    @Override
    public String toString() {
        return String.format("%.1f %s i%.1f", this.re, this.im > 0 ? "+" : "-", Math.abs(this.im));
    }
}
