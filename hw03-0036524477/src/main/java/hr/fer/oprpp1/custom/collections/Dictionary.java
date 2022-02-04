package hr.fer.oprpp1.custom.collections;

/**
 * A class that contains a list of Pairs of unique keys and their values that
 * don't have to be unique values
 *
 * @author Marko Tunjić
 */
public class Dictionary<K, V> {

    /**
     * A nested class that stores a key and a value
     *
     * @author Marko Tunjić
     */
    private static class Pair<K, V> {
        /** A attribute that represents the ky of the pair */
        private K key;

        /** A attribute that represents the value of the corresponding key */
        private V value;

        /**
         * A constructor that creates a Pair from the given values if the key is null a
         * NullPointerException is thrown
         *
         * @param key   the key of the pair
         * @param value the value of the pair
         * @throws NullPointerException if the given key was null
         */
        public Pair(K key, V value) {
            if (key == null)
                throw new NullPointerException();
            this.key = key;
            this.value = value;
        }
    }

    /** A collection of pairs that simulates a dicitionary */
    private ArrayIndexedCollection<Pair<K, V>> dictionary;

    /**
     * A constructor that initializes the dictionary by creating a
     * ArrayIndexedCollection
     */
    public Dictionary() {
        this.dictionary = new ArrayIndexedCollection<>();
    }

    /**
     * A method that returns true if the dictionary is empty and false otherwise
     *
     * @return true if empty false otherwise
     */
    public boolean isEmpty() {
        return this.dictionary.isEmpty();
    }

    /**
     * A method that returns the number of elements inside the dictionary
     *
     * @return the number of elements inside the dictionary
     */
    public int size() {
        return this.dictionary.size();
    }

    /** A method that clears the dictionary */
    public void clear() {
        this.dictionary.clear();
    }

    /**
     * A method that adds a new Pair with the given key and value if the pair wasn't
     * present otherwise it changes the old value to the given value and return the
     * old value or null if there was no old value, throws a NullPointerException if
     * a null key was given
     *
     * @param key   the key to be inserted the corresponding value changed
     * @param value the value to be inserted or changed
     *
     * @return the old value if key was present null otherwise
     *
     * @throws NullPointerException if the given key was null
     */
    public V put(K key, V value) {
        // loop through all the elements and check if there is a matching key
        ElementsGetter<Pair<K, V>> eg = this.dictionary.createElementsGetter();
        while (eg.hasNextElement()) {
            Pair<K, V> element = eg.getNextElement();

            // if there is a matching key return the old value and change it to the given
            // value
            if (element.key.equals(key)) {
                V oldValue = element.value;
                element.value = value;
                return oldValue;
            }
        }

        // if there is no matching key add a new Pair and return null
        this.dictionary.add(new Pair<K, V>(key, value));
        return null;
    }

    /**
     * A method that returns the matching value of the given key if no key was found
     * null is returned
     *
     * @param key the key for whom the value should be returned
     *
     * @return the corresponding value or null if not present
     */
    public V get(Object key) {
        ElementsGetter<Pair<K, V>> eg = this.dictionary.createElementsGetter();
        while (eg.hasNextElement()) {
            Pair<K, V> element = eg.getNextElement();
            if (element.key.equals(key))
                return element.value;
        }
        return null;
    }

    /**
     * A method that removes the Pair with the coresponding key and returns the
     * matching value or null if key didn't exist
     *
     * @param the key of the Pair to be removed
     *
     * @return the value of the Pair to be removed or null if not present
     */
    public V remove(K key) {
        ElementsGetter<Pair<K, V>> eg = this.dictionary.createElementsGetter();
        Pair<K, V> toBeRemoved = null;
        while (eg.hasNextElement()) {
            Pair<K, V> element = eg.getNextElement();
            if (element.key.equals(key)) {
                toBeRemoved = element;
                break;
            }
        }
        this.dictionary.remove(toBeRemoved);
        if (toBeRemoved != null)
            return toBeRemoved.value;
        else
            return null;
    }

}
