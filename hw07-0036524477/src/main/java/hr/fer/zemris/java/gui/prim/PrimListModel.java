package hr.fer.zemris.java.gui.prim;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * A class that implements the {@link ListModel} interface and represents a list
 * model that diynamically populates a list of prime numbers
 *
 * @author Marko Tunjić
 */
public class PrimListModel implements ListModel<Integer> {
    /** An private attribute that contains the prime numbers */
    private List<Integer> primeNumbers = new ArrayList<>();

    /** An private attribute that contains the interested listeners in this model */
    private List<ListDataListener> listeners = new ArrayList<>();

    {
        primeNumbers.add(1);
    }

    /**
     * A method that returns the number of prime numbers counted at a certain moment
     *
     * @return the number of prime numbers counted at a certain moment
     */
    @Override
    public int getSize() {
        return primeNumbers.size();
    }

    /**
     * A method that returns the prime number at the specified index
     *
     * @return the prime number at the specified index
     */
    @Override
    public Integer getElementAt(int index) {
        return primeNumbers.get(index);
    }

    /**
     * A method that adds a interested {@link ListDataListener} to the collection.
     * Throws {@link NullPointerException} if null was given
     *
     * @param l the interested listener
     *
     * @throws NullPointerException if null was given
     */
    @Override
    public void addListDataListener(ListDataListener l) {
        if (l == null)
            throw new NullPointerException("A lstener interested in the prime numbers can not be null");
        listeners.add(l);
    }

    /**
     * A method that removes the given listener from the interested listener list
     *
     * @param l the listener that was intersted
     */
    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

    /** A method calculates the next prime number and adds it to the colection */
    public void next() {
        // get the last prime number
        int start = primeNumbers.get(primeNumbers.size() - 1);

        // get the next prime number
        while (true) {
            start++;

            // check if the current number is a prime number
            if (isPrime(start)) {
                // add the number
                int index = primeNumbers.size();
                primeNumbers.add(start);

                // inform listeners
                fireIntervalAdded(index, index);
                break;
            }
        }
    }

    /**
     * A private method that checks returns true if the given number is a prime
     * number or false otherwise
     *
     * @return true if prime false otherwise
     */
    private boolean isPrime(int number) {
        for (int i = 2; i * i <= number; i++) {
            if (number % i == 0)
                return false;
        }
        return true;
    }

    /**
     * A private method that informs all interested listeners that a prime number
     * has been added to the collection of prime numbers
     */
    private void fireIntervalAdded(int start, int end) {
        ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, start, end);
        for (var listener : listeners)
            listener.intervalAdded(event);
    }
}
