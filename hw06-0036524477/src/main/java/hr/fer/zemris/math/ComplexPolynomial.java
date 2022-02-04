package hr.fer.zemris.math;

/**
 * A class that represents a complex polynomal in format
 * (Z[n]*Z^n)+(Z[n-1]*Z^n-1)+...+(Z[0]*Z^0)
 *
 * @author Marko Tunjić
 */
public class ComplexPolynomial {
    /**
     * An private attribute that contains all the factors of this complex polynomal
     */
    private Complex[] factors;

    /**
     * A constructor that creates a {@link ComplexPolynomial} from the given
     * parameters.
     *
     * @param factors all the facors of this poynomal
     */
    public ComplexPolynomial(Complex... factors) {
        this.factors = factors;
    }

    /**
     * A method that returns the order of this polynom; eg. For (7+2i)z^3+2z^2+5z+1
     * returns 3
     *
     * @return the order of this polynom
     */
    public short order() {
        return (short) (factors.length - 1);
    }

    /**
     * A method that computes and returns a new polynomial that is a result of
     * multiplying this polynomal and the given polynomal. Throws
     * {@link NullPointerException} if null was given
     *
     * @param p the polynomal with which this should be multiplicated
     *
     * @return the result of multiplying this and the given polynmal
     *
     * @throws NullPointerException if null was given
     */
    public ComplexPolynomial multiply(ComplexPolynomial p) {

        // check if legal
        if (p == null)
            throw new NullPointerException("Polynomal can not be null");

        // new lenght will be m+n-1
        Complex[] prod = new Complex[this.factors.length + p.factors.length - 1];

        // Initialize each on zero
        for (int i = 0; i < prod.length; i++)
            prod[i] = Complex.ZERO;

        // multiply each of the first polynomal with each of the second polynomal and
        // add to corresponding index
        for (int i = 0; i < this.factors.length; i++)
            for (int j = 0; j < p.factors.length; j++)
                prod[i + j] = prod[i + j].add(this.factors[i].multiply(p.factors[j]));

        return new ComplexPolynomial(prod);
    }

    /**
     * A method that computes the first derivative of this polynomial; for example,
     * for
     * (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5
     *
     * @return a new Polynomal that is the result of deriving this polynomal
     */
    public ComplexPolynomial derive() {
        // initialize new factors
        Complex[] prod = new Complex[factors.length - 1];

        // for each factor in this except the last one multiply them with their power
        for (int i = 0; i < factors.length - 1; i++)
            prod[i] = factors[i].multiply(new Complex(factors.length - i - 1, 0));

        return new ComplexPolynomial(prod);
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
        // check if legal
        if (z == null)
            throw new NullPointerException("Complex number can not be null");

        // power the given polynomal to the adequate power and multiply it with the
        // corresponding factor
        Complex res = Complex.ZERO;
        for (int i = 0; i < factors.length; i++) {
            Complex pow = z.power(factors.length - i - 1);
            res = res.add(factors[i].multiply(pow));
        }
        return res;
    }

    /**
     * A method that returns the {@link String} representation of this polynomal in
     * format
     * (c4)*z^4+(c3)*z^3+(c2)*z^2+(c1)*z^1+(c0)
     *
     * @return the {@link String} representaiton of this polynomal
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < factors.length; i++)
            sb.append(String.format("(%s)*z^%d+", factors[i].toString(), factors.length - i - 1));
        sb.delete(sb.length() - 5, sb.length());
        return sb.toString();
    }
}
