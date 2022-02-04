package hr.fer.oprpp1.custom.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A class that simulates a hash table with a linked list overflow
 *
 * @author Marko Tunjić
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {
    /** A constant that holds the size of the default hash table size */
    public static final int DEFAULT_HASHTABLE_SIZE = 16;

    /** A constant that defines the critical occupancy of the hashtable */
    public static final double CRITICAL_OCCUPANCY = 0.75;

    /**
     * A private nested static class that defines one HashTable element
     *
     * @author Marko Tunjić
     */
    public static class TableEntry<K, V> {
        /** A private attribute that contains the key of the hash table element */
        private K key;

        /**
         * A private attribute that contains the coresponding value of the hash table
         * element
         */
        private V value;

        /**
         * A private attribute that contains the pojnter to the next hash table element
         * in case of overflow
         */
        private TableEntry<K, V> next;

        /**
         * A constructor that creates a hash table element from the given values and
         * throws a NullPointerException when the given key is null
         *
         * @param key   the key of the table element
         * @param value the corespondng value of the table element
         * @param next  the pointer to the next table element in case of overflow
         *
         * @throws NullPointerException when the given key is null
         */
        public TableEntry(K key, V value, TableEntry<K, V> next) {
            if (key == null)
                throw new NullPointerException("Key can not be null!");
            this.key = key;
            this.value = value;
            this.next = next;

        }

        /**
         * a getter function for the key
         *
         * @return the key of the table element
         */
        public K getKey() {
            return key;
        }

        /**
         * a getter for the coresponding value of the table element
         *
         * @return the value of the coresponding table element
         */
        public V getValue() {
            return value;
        }

        /**
         * A setter for the coresponding element
         *
         * @param value the value to which the value of the table element has to be set
         */
        public void setValue(V value) {
            this.value = value;
        }

    }

    /** A private attribute that simulates a hash table */
    private TableEntry<K, V>[] entries;

    /**
     * A private attribute that contains the number of elements inside the hash
     * table
     */
    private int size;

    /**
     * A variable that contains the number of internal structure modifications in
     * the hash table
     */
    private int modificationCount;

    /** A constructor that creates a hash table with the default capacity */
    @SuppressWarnings("unchecked")
    public SimpleHashtable() {
        this.entries = (TableEntry<K, V>[]) new TableEntry[SimpleHashtable.DEFAULT_HASHTABLE_SIZE];
        this.modificationCount = 0;
    }

    /**
     * A constructor that creates a hash table with the given capacity so that the
     * capacity is the closest greater power of 2
     */
    @SuppressWarnings("unchecked")
    public SimpleHashtable(int capacity) {
        if (capacity < 1)
            throw new IllegalArgumentException();

        capacity = (int) Math.pow(2, Math.ceil((int) Math.sqrt(capacity)));
        this.entries = (TableEntry<K, V>[]) new TableEntry[capacity];
        this.modificationCount = 0;
    }

    /**
     * A method that changes and returns the old value of the given key to the given
     * value or adds the new key-value pair if the key did not already exist in the
     * tableand throws a NullPointerException if the given key was null.
     *
     * @param key   the key whos value has to be changed/added
     * @param value the value to be changed/added
     *
     * @return old value if key existed or null otherwise
     *
     * @throws NullPointerException if given key is null
     */
    public V put(K key, V value) {
        if (key == null)
            throw new NullPointerException();

        if (this.size / this.entries.length >= SimpleHashtable.CRITICAL_OCCUPANCY)
            increaseLength();

        // loop through all the indexes of the table
        for (int i = 0; i < this.entries.length; i++) {

            // loop through the overflow
            for (TableEntry<K, V> entry = this.entries[i]; entry != null; entry = entry.next) {

                // if we find a corresponding key change the value and return the old value
                if (entry.getKey().equals(key)) {
                    V oldValue = entry.getValue();
                    entry.setValue(value);
                    return oldValue;
                }
            }
        }

        // if no key was found create a new key value pair
        TableEntry<K, V> newEntry = new TableEntry<>(key, value, null);

        // calculate hash
        int hash = Math.abs(key.hashCode() % this.entries.length);

        // check for overflow
        if (this.entries[hash] == null) {
            this.entries[hash] = newEntry;
        } else {

            // loop to the last element
            TableEntry<K, V> entry;
            for (entry = this.entries[hash]; entry.next != null; entry = entry.next)
                ;
            entry.next = newEntry;
        }

        // increase the size, modification count and return null
        this.size++;
        this.modificationCount++;
        return null;
    }

    /**
     * A method that doubles the size of the hash table and rearranges the elements
     * in the new bigger table
     */
    @SuppressWarnings("unchecked")
    private void increaseLength() {
        // save all the elements
        TableEntry<K, V>[] helper = this.toArray();

        // reallocate the table
        this.entries = (TableEntry<K, V>[]) new TableEntry[this.entries.length * 2];

        this.size = 0;
        for (int i = 0; i < helper.length; i++) {
            // rearrange elements
            this.put(helper[i].getKey(), helper[i].getValue());
        }
    }

    /**
     * A method that returns the value of the coresponding key if existent if not
     * the returns null
     *
     * @param key the key whos value has to be returned
     *
     * @return the coresponding value or null if the key did not exist
     */
    public V get(Object key) {
        for (int i = 0; i < this.entries.length; i++) {
            for (TableEntry<K, V> entry = this.entries[i]; entry != null; entry = entry.next) {
                if (entry.getKey().equals(key))
                    return entry.getValue();
            }
        }
        return null;
    }

    /** A method that returns the number of elements inside the hash table */
    public int size() {
        return this.size;
    }

    /**
     * A method that return true if the given key is inside the hash table and false
     * otherwise
     *
     * @param key the key which is searched for
     *
     * @return true if found false otherwise
     */
    public boolean containsKey(Object key) {
        for (int i = 0; i < this.entries.length; i++) {
            for (TableEntry<K, V> entry = this.entries[i]; entry != null; entry = entry.next) {
                if (entry.getKey().equals(key))
                    return true;
            }
        }
        return false;
    }

    /**
     * A method that serches for the given value and return true if found and false
     * otherwise
     *
     * @param value the value that is searched for
     *
     * @return true if found false otherwise
     */
    public boolean containsValue(Object value) {
        for (int i = 0; i < this.entries.length; i++) {
            for (TableEntry<K, V> entry = this.entries[i]; entry != null; entry = entry.next) {
                if (entry.getValue() == null && value == null)
                    return true;
                if (entry.getValue().equals(value))
                    return true;
            }
        }
        return false;
    }

    /**
     * A method that removes the pair of the coresponding key from the hashtable and
     * returns the coresponding value of the removed key or null if not found
     *
     * @param key the key of the pair to be removed
     *
     * @return the value of the removed pair or null if not found
     */
    public V remove(Object key) {
        // loop through the hash table
        for (int i = 0; i < this.entries.length; i++) {

            // loop through the overflow
            for (TableEntry<K, V> entry = this.entries[i],
                    trailing = this.entries[i]; entry != null; entry = entry.next) {

                // adjust the trailing pointer so we can change the next attribute of the
                // TableEntry
                if (entry != this.entries[i].next && entry != this.entries[i]) {
                    trailing = trailing.next;
                }

                // if the key is found change the next attribute of the trailing pointer,
                // reduce size and increase modification count
                if (entry.getKey().equals(key)) {
                    V oldValue = entry.getValue();
                    if (entry == this.entries[i]) {
                        this.entries[i] = entry.next;
                    } else {
                        trailing.next = entry.next;
                    }
                    this.size--;
                    this.modificationCount++;
                    return oldValue;
                }

            }
        }
        return null;
    }

    /** A method that return true if the hash table is empty */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * A method that creates a string representation of this hashtable
     * "[key1=value1, key2=value2, key3=value3]"
     *
     * @return the string representation
     */
    public String toString() {
        // new string builder for building the solution
        StringBuilder sb = new StringBuilder("[");

        // loop through all the containers
        for (int i = 0; i < this.entries.length; i++) {

            // loop through the overflow
            for (TableEntry<K, V> entry = this.entries[i]; entry != null; entry = entry.next)
                // add "key=value, "
                sb.append(entry.key).append("=").append(entry.getValue()).append(", ");
        }
        // if we added one element remove the ", "
        if (sb.length() > 1)
            sb.replace(sb.length() - 2, sb.length(), "");

        // add the finishing ]
        sb.append("]");
        return sb.toString();
    }

    /**
     * A methof that returns the array representation of this hash table
     *
     * @return the array representation of this hashtable
     */
    @SuppressWarnings("unchecked")
    public TableEntry<K, V>[] toArray() {
        TableEntry<K, V>[] result = (TableEntry<K, V>[]) new TableEntry[this.size];
        int counter = 0;
        for (int i = 0; i < this.entries.length; i++) {
            for (TableEntry<K, V> entry = this.entries[i]; entry != null; entry = entry.next) {
                result[counter++] = entry;
            }
        }
        return result;
    }

    /**
     * A method that clears the hash table by setting all the pointer to null and
     * reseting size
     */
    public void clear() {
        for (int i = 0; i < this.entries.length; i++)
            this.entries[i] = null;
        this.size = 0;
        this.modificationCount++;
    }

    /** A method that returns an iterator for this hash table */
    @Override
    public Iterator<TableEntry<K, V>> iterator() {
        return new IteratorImpl();
    }

    /**
     * A private nested class that implements the iterator for the SimpleHashtable
     *
     * @author Marko Tunjić
     */
    private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K, V>> {
        /**
         * A private attribute that contains the pointer to the last returned TableEntry
         */
        private TableEntry<K, V> lastEntry;

        /**
         * A private attribute that contains the index of the slot that contains the
         * lastEntry
         */
        private int lastSlot = -1;

        /**
         * A private attribute that indicates if the method next() was called before
         * ccaling the remove() method
         */
        private boolean calledNext = false;

        /**
         * A private attribute that contains the number of modifications that happened
         * in the hash table at the time of creating the iterator (may increase when
         * calling the iterator.remove() method)
         */
        private int modCount = SimpleHashtable.this.modificationCount;

        /**
         * A method that return true if there is a next element in the hash table and
         * false otherwise. Thros IllegalStateException if illegal modifications
         * happened during iterating
         *
         * @return true if there are more elements and false otherwise
         *
         * @throws IllegalStateException in case of illegal modifications on the hash
         *                               table during iterating
         */
        @Override
        public boolean hasNext() {
            // check if mod count is legal
            if (this.modCount != SimpleHashtable.this.modificationCount)
                throw new IllegalStateException();

            // check if there is another element in the overflow list
            if (lastEntry != null && lastEntry.next != null)
                return true;
            // check if there are more opened slots in the hash table
            for (int i = lastSlot + 1; i < SimpleHashtable.this.entries.length; i++)
                if (SimpleHashtable.this.entries[i] != null)
                    return true;
            return false;
        }

        /**
         * A method that returns the next TableEntry and throws A NoSuchElementException
         * if there are no more elements
         *
         * @return the next TableEntry
         *
         * @throws NoSuchElementException if there are no more elements
         */
        @Override
        public TableEntry<K, V> next() {
            // check if there are more elements
            if (!this.hasNext())
                throw new NoSuchElementException();

            // check if the next element is in the overflow
            if (lastEntry != null && lastEntry.next != null) {
                lastEntry = lastEntry.next;
                calledNext = true;
                return lastEntry;
            }

            // if not in the overflow then in the next slot
            lastSlot++;
            for (int i = lastSlot; i < SimpleHashtable.this.entries.length; i++) {
                if (SimpleHashtable.this.entries[i] != null) {
                    lastEntry = SimpleHashtable.this.entries[i];
                    lastSlot = i;
                    break;
                }
            }
            calledNext = true;
            return lastEntry;
        }

        /**
         * A method that removes the last element returned from the next method and
         * throws a IllegalStateException if remove is called twice before calling the
         * next method or the hash table has been modified on a illegal way in the
         * meantime
         *
         * @throws IllegalArgumentException if called twice in arow without calling the
         *                                  next() method or in case of illegal
         *                                  hashtable modification
         */
        @Override
        public void remove() {
            // check if illegal modifications happened or if the remove method was called
            // twice in a row
            if (calledNext && this.modCount == SimpleHashtable.this.modificationCount) {
                SimpleHashtable.this.remove(this.lastEntry.key);
                calledNext = false;
                this.modCount = SimpleHashtable.this.modificationCount;
            } else {
                throw new IllegalStateException();
            }
        }
    }
}