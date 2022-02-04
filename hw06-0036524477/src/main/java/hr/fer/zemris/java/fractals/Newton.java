package hr.fer.zemris.java.fractals;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.utils.Loader;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * A class that calculates (sequentially) and display the Newtons fractal
 *
 * @author Marko Tunjić
 */
public class Newton {

    /**
     * A main method that loads user input from command line and calls the
     * calculator and drawer
     *
     * @param args command line arguments(not used here)
     */
    public static void main(String[] args) {
        // load user input
        List<Complex> numbers = Loader.loadRoots();
        Complex[] roots = new Complex[numbers.size()];
        roots = numbers.toArray(roots);

        // call the producer
        FractalViewer.show(new MyProducer(roots));
    }

    /**
     * A class that implements the interface {@link IFractalProducer} and it
     * produces the Newtons fractal X[n]=X[n-1]-f(X[n])/f'(X[n]) for each complex
     * dot on the screen sequentially
     *
     * @author Marko Tunjić
     */
    public static class MyProducer implements IFractalProducer {
        /** An private attribute that contains he roots of the polynomal */
        private Complex[] roots;

        /**
         * A construcotrs that creates a {@link MyProducer} from the given parameters.
         * Throws null pointer exception if null was given.
         *
         * @param roots the roots of the polynomal
         *
         * @throws NullPointerException if null was given
         */
        public MyProducer(Complex... roots) {
            if (roots == null)
                throw new NullPointerException("Roots can not be null");
            this.roots = roots;
        }

        /**
         * A method that produces coloring data for all pixels determined with the
         * Newtons fractal. The calculations are done sequentially. Throws
         * {@link NullPointerException} if observer or cancel
         * are null
         *
         * @param reMin     the minimal real part of a complex number that can be drawn
         *                  on
         *                  the screen
         * @param reMax     the maximal real part of a complex number that can be drawn
         *                  on
         *                  tthe screen
         * @param imMin     the minimal imaginary part of a complex number that can be
         *                  drawn
         *                  on
         *                  the screen
         * @param imMax     the maximal real part of a complex number that can be drawn
         *                  on
         *                  the screen
         * @param width     the width of the drawing screen
         * @param height    the height of the drawing screen
         * @param requestNo the id of the drawing request
         * @param observer  a observer that recevies data from the produce method and
         *                  calls the drawer
         * @param cancel    a parameter that indicates if the drawing should be canceled
         *
         * @throws NullPointerException if observer or cancel are null
         */
        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {

            if (observer == null || cancel == null)
                throw new NullPointerException("Observer and cancelel can not be null");

            // message
            System.out.println("Zapocinjem izracun...");

            // the maximum index of the X[n] that will be calculated
            int m = 16 * 16 * 16;

            // generate polynomal from roots
            ComplexRootedPolynomial crp = new ComplexRootedPolynomial(
                    new Complex(0, 0), roots);
            ComplexPolynomial polynomial = crp.toComplexPolynom();

            // initialize drawing data
            short[] data = new short[width * height];

            // fill drawing data
            Newton.calculate(reMin, reMax, imMin, imMax, width, height, 0, height - 1, m, data, cancel, roots);

            // message
            System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");

            // inform the obserever that it's done
            observer.acceptResult(data, (short) (polynomial.order() + 1), requestNo);
        }
    }

    /**
     * A method that calculates all the complex numbers for a newtons fractal for a
     * given screen and fills up the drawing data.
     *
     * @param reMin  the minimal real part of a complex number that can be drawn
     *               on
     *               the screen
     * @param reMax  the maximal real part of a complex number that can be drawn
     *               on
     *               tthe screen
     * @param imMin  the minimal imaginary part of a complex number that can be
     *               drawn
     *               on
     *               the screen
     * @param imMax  the maximal real part of a complex number that can be drawn
     *               on
     *               the screen
     * @param width  the width of the drawing screen
     * @param height the height of the drawing screen
     * @param ymin   the first row to draw
     * @param ymax   the last row to draw
     * @param data   the drawing data to be filled
     * @param cancel a parameter that indicates if the calculating shoudl stop
     * @param roots  the roots of the polynomal
     *
     * @throws NullPointerException if cancel or roots are null
     */
    public static void calculate(double reMin, double reMax, double imMin, double imMax,
            int width, int height, int ymin, int ymax, int m, short[] data, AtomicBoolean cancel, Complex[] roots) {

        // check if null
        if (roots == null || cancel == null)
            throw new NullPointerException("Roots and cancelel can not be null");

        // the offset of the drawing pixel
        int offset = 0;

        // for each row in the given interval
        for (int y = ymin; y <= ymax; y++) {
            // check if calculating shoudl stop
            if (cancel.get())
                break;

            // for each column on screen
            for (int x = 0; x < width; x++) {
                // transform infinite complex numbers to discrete complex numbers
                double cre = x / (width - 1.0) * (reMax - reMin) + reMin;
                double cim = (height - 1.0 - y) / (height - 1) * (imMax - imMin) + imMin;

                // initialize the starting point the current constant and the polynomals
                Complex zn = new Complex(cre, cim);
                ComplexRootedPolynomial crp = new ComplexRootedPolynomial(
                        new Complex(cre, cim), roots);
                ComplexPolynomial polynomial = crp.toComplexPolynom();
                ComplexPolynomial derived = polynomial.derive();

                // iterator count
                int iters = 0;

                // the X[n-1] variable
                Complex znold;

                // the distance between X[n] and X[n-1]
                double module = 0;

                // dow hile iterator less then max iterations or distance greater than treshold
                do {
                    // calculate X[n]=X[n-1]-f(X[n])/f'(X[n])
                    Complex numerator = polynomial.apply(zn);
                    Complex denominator = derived.apply(zn);
                    znold = zn;
                    Complex fraction = numerator.divide(denominator);
                    zn = zn.sub(fraction);

                    // calculate distance
                    module = znold.sub(zn).module();
                } while (module > 0.001 && iters < m);
                // determine coloring
                int index = crp.indexOfClosestRootFor(zn, 0.002);
                data[ymin * width + offset] = (short) (index + 1);

                // go to next pixel
                offset++;
            }
        }
    }
}
