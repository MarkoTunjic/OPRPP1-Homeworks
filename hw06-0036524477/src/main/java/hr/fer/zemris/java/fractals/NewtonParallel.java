package hr.fer.zemris.java.fractals;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.utils.Loader;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * A class that calculates (parallel(multithreading)) and display the Newtons
 * fractal
 *
 * @author Marko Tunjić
 */
public class NewtonParallel {

    /**
     * A main method that gets the number of threads and number of lines on the
     * screen from cammand line arguments then loads user input from command line
     * and calls the calculator and drawer.
     *
     * @param args command line arguments --worker=N (or -w N) --tracks=N (or -t N)
     */
    public static void main(String[] args) {

        // check if legal number of arguments was given
        if (args.length > 4) {
            System.out.println("Maximum 4 arguments are allowed");
            return;
        }

        // initialize the number of workers and tracks
        int workers = -1;
        int tracks = -1;
        int i = 0;

        // try getting all the arguments
        try {
            // for each command line argument
            for (i = 0; i < args.length; i++) {
                // if shorter form for workers parse the next argument
                if (args[i].equals("-w")) {
                    if (workers != -1)
                        throw new IllegalArgumentException("Workers can not be defined more than once");
                    workers = Integer.parseInt(args[++i]);
                }

                // if shorter form for tracks parse the next argument
                else if (args[i].equals("-t")) {
                    if (tracks != -1)
                        throw new IllegalArgumentException("Tracks can not be defined more than once");
                    tracks = Integer.parseInt(args[++i]);
                }

                // if normal form for workers parse the number after "="
                else if (args[i].startsWith("--workers=")) {
                    if (workers != -1)
                        throw new IllegalArgumentException("Workers can not be defined more than once");
                    workers = Integer.parseInt(args[i].substring(args[i].indexOf("=") + 1));
                }

                // if normal form for tracks parse the number after "="
                else if (args[i].startsWith("--tracks=")) {
                    if (tracks != -1)
                        throw new IllegalArgumentException("Tracks can not be defined more than once");
                    tracks = Integer.parseInt(args[i].substring(args[i].indexOf("=") + 1));
                }

                // if not supported argument was given
                else {
                    System.out.println("Unsupported argument: " + args[i]);
                    return;
                }
            }
        }

        // if some of the arguments were NaN
        catch (NumberFormatException ex) {
            System.out.println(String.format("Invalid format for %s", args[i]));
        }
        // if shorter version was given but no number after it
        catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println(String.format("Missing argument for %s", args[i - 1]));
        }
        System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
        // if some of the arguments were not given initialize the to their default
        // values
        if (workers == -1)
            workers = Runtime.getRuntime().availableProcessors();
        if (tracks == -1)
            tracks = Runtime.getRuntime().availableProcessors() * 4;

        // get user input for roots
        List<Complex> numbers = Loader.loadRoots();
        Complex[] roots = new Complex[numbers.size()];
        roots = numbers.toArray(roots);

        // call the calcutator and drawer
        FractalViewer.show(new MyProducer(workers, tracks, roots));
    }

    /**
     * A class that implements the interface {@link Runnable} and represents a job
     * for a thread. The job is calculating a part of the picture for the newtons
     * fractal
     *
     * @author Marko Tunjić
     */
    public static class PosaoIzracuna implements Runnable {
        /**
         * An attribute that represents the minimal real value of a complex
         * number
         */
        double reMin;

        /**
         * An attribute that represents the maximal real value of a complex
         * number
         */
        double reMax;

        /**
         * An attribute that represents the minimal imaginary value of a complex
         * number
         */
        double imMin;

        /**
         * An attribute that represents the maximal imaginary value of a complex
         * number
         */
        double imMax;

        /** An attribute that represents the width of the drawing screen */
        int width;

        /** An attribute that represents the height of the drawing screen */
        int height;

        /**
         * An attribute that represents the starting row of this jobs picture
         * part
         */
        int yMin;

        /**
         * An attribute that represents the ending row of this jobs picture part
         */
        int yMax;

        /**
         * An attribute that represents the maximal number of iterations for calculating
         * the X[n] value
         */
        int m;

        /** An attribute that represents the drawing data for each pixel */
        short[] data;

        /** An attribute that indicates if the thread shoudl be stopped */
        AtomicBoolean cancel;

        /** An attribute that contains all the roots of a polynomal */
        Complex[] roots;

        /**
         * A constant that contains an empty job used for checking if a thread is done
         */
        public static PosaoIzracuna NO_JOB = new PosaoIzracuna();

        /** A constructor that creates an empty job */
        private PosaoIzracuna() {
        }

        /**
         * A constructor that creates a job from the given parameters. Throws
         * {@link NullPointerException} if cancel or roots are null.
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
        public PosaoIzracuna(double reMin, double reMax, double imMin,
                double imMax, int width, int height, int yMin, int yMax,
                int m, short[] data, AtomicBoolean cancel, Complex[] roots) {
            super();
            this.reMin = reMin;
            this.reMax = reMax;
            this.imMin = imMin;
            this.imMax = imMax;
            this.width = width;
            this.height = height;
            this.yMin = yMin;
            this.yMax = yMax;
            this.m = m;
            this.data = data;
            this.cancel = cancel;
            this.roots = roots;
        }

        /**
         * A method that starts the job of the thread and the job is to calculate its
         * part of the Newtons fractal picture
         */
        @Override
        public void run() {
            Newton.calculate(reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data,
                    cancel, roots);
        }
    }

    /**
     * A class that implements the interface {@link IFractalProducer} and it
     * produces the Newtons fractal X[n]=X[n-1]-f(X[n])/f'(X[n]) for each complex
     * dot on the screen with more threads.
     *
     * @author Marko Tunjić
     */
    public static class MyProducer implements IFractalProducer {
        /**
         * An private attribute that contains all the roots of the polynomal used for
         * calculating
         */
        private Complex[] roots;

        /** An private attribute that represents the number of threads */
        private int workers;

        /** An private attribute that represents the number of jobs */
        private int tracks;

        /**
         * A constructor that creates a producer from the given parameters
         *
         * @param workers the number of threads used for calculating
         * @param tracks  the number of jobs aka horizontal tracks on the picture
         * @param roots   all the roots of the polynomal used for
         *                calculating
         */
        public MyProducer(int workers, int tracks, Complex... roots) {
            this.roots = roots;
            this.workers = workers;
            this.tracks = tracks;
        }

        /**
         * A method that produces coloring data for all pixels determined with the
         * Newtons fractal. The calculations are done parrallel (multithreading). Throws
         * {@link NullPointerException} if observer or cancel are null
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
            // message
            System.out.println("Zapocinjem izracun...");

            // maximum number of iterations for calculating the formula
            // X[n]=X[n-1]-f(X[n])/f'(X[n])
            int m = 16 * 16 * 16;

            // initialize the polynomal
            ComplexRootedPolynomial crp = new ComplexRootedPolynomial(
                    new Complex(0, 0), roots);
            ComplexPolynomial polynomial = crp.toComplexPolynom();

            // initialize the drawing data
            short[] data = new short[width * height];

            // if the given number of tracks is greater than the possible number of tracks
            // take the largest possible track number
            final int brojTraka = tracks < height ? tracks : height;

            // print workers and tracks
            System.out.println(String.format("Number of workers=%d, number of tracks=%d", workers, brojTraka));

            // calculate number of rows per track/job
            int brojYPoTraci = height / brojTraka;

            // a container for all the jobs
            final BlockingQueue<PosaoIzracuna> queue = new LinkedBlockingQueue<>();

            // an array of threads aka workers
            Thread[] radnici = new Thread[workers];

            // declare each thread
            for (int i = 0; i < radnici.length; i++) {
                radnici[i] = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        // while true beacouse we don't know how much jobs this thread will do could be
                        // 1 could be more. And becouse the thread could be woken up and not have a job
                        while (true) {

                            PosaoIzracuna p = null;
                            // try taking a job from the container. If empty then wait
                            try {
                                p = queue.take();
                                if (p == PosaoIzracuna.NO_JOB)
                                    break;
                            }
                            // if sleeping interruped
                            catch (InterruptedException e) {
                                continue;
                            }
                            p.run();
                        }
                    }
                });
            }

            // start each thread this can be done without any jobs in the container becouse
            // all of the threads will wait for a job
            for (int i = 0; i < radnici.length; i++)
                radnici[i].start();

            // for each track initialize a job
            for (int i = 0; i < brojTraka; i++) {

                // calculate the starting row and the ending row
                int yMin = i * brojYPoTraci;
                int yMax = (i + 1) * brojYPoTraci - 1;
                if (i == brojTraka - 1) {
                    yMax = height - 1;
                }

                // create new job
                PosaoIzracuna posao = new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data,
                        cancel, roots);

                // while true becouse the container could be full or someone could be in the
                // container already so the thread would have to
                // wait and could be woken up from sleep and the container would still be full
                while (true) {
                    try {
                        queue.put(posao);
                        break;
                    } catch (InterruptedException e) {
                    }
                }
            }

            // for each worker add a empty job so it knows when to stop
            for (int i = 0; i < radnici.length; i++) {

                // while true becouse the container could be full or someone could be in the
                // container already so the thread would have to
                // wait and could be woken up from sleep and the container would still be full
                while (true) {
                    try {
                        queue.put(PosaoIzracuna.NO_JOB);
                        break;
                    } catch (InterruptedException e) {
                    }
                }
            }

            // wait for each worker to finish their worker before passing data to the
            // observer
            for (int i = 0; i < radnici.length; i++) {
                // while true becouse a thread could be interruped in it's job before finish so
                // it would have to continue the job after interruption
                while (true) {
                    try {
                        radnici[i].join();
                        break;
                    } catch (InterruptedException e) {
                    }
                }
            }

            // message
            System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");

            // inform the observer that the data is done and pass it to the drawer
            observer.acceptResult(data, (short) (polynomial.order() + 1), requestNo);
        }
    }
}
