package hashmap;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private final static int INITIAL_SIZE = 16;

    private int size;

    private static double loadFactor = 0.75;
    /**
     * Removes all of the mappings from this map.
     */
    @Override
    public void clear() {
        for (var i : buckets) {
            i.clear();
        }
        size = 0;
    }

    /**
     * Returns true if this map contains a mapping for the specified key.
     *
     * @param key
     */
    @Override
    public boolean containsKey(K key) {
        var collect = getBucket(key);
        if (collect != null) {
            return findNodeStream(collect, key).findAny().isPresent();
        }
        return false;
    }

    private Collection<Node> getBucket(K key) {
        int hashCode = key.hashCode();
        int index = processHashcode(hashCode, buckets.length);
        return buckets[index];
    }
    /**
     * process the hashcode to the index
     * @param hashCode from java.hashCode()
     * @param mod buckets.length
     * @return index in buckets
     */
    private int processHashcode(int hashCode, int mod) {
        if (hashCode < 0) {
            hashCode = Math.floorMod(hashCode, mod);
        }
        return hashCode % mod;
    }

    private Stream<Node> findNodeStream(Collection<Node> c, K key) {
        return c.stream()
                .filter(node -> node.key.equals(key));
    }
    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     *
     * @param key
     */
    @Override
    public V get(K key) {
        var collect = getBucket(key);
        if (collect != null) {
            var optionN = findNodeStream(collect, key).findAny();
            if (optionN.isPresent()) {
                return optionN.get().value;
            }
        }
        return null;
    }

    /**
     * Returns the number of key-value mappings in this map.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     *
     * @param key
     * @param value
     */
    @Override
    public void put(K key, V value) {
        var collection = getBucket(key);
        var opNode = findNodeStream(collection, key).findAny();
        if (opNode.isPresent()) {   // update node
            opNode.get().value = value;
        } else {    // insert node
            var node = createNode(key, value);
            double loadFa = 1.0 * (size + 1) / buckets.length;
            if (loadFa >= loadFactor) { // resize
                resize();
                buckets[processHashcode(key.hashCode(), buckets.length)].add(node);
            } else {
                collection.add(node);
            }
            size++;
        }
    }

    /**
     * resize
     */
    private void resize() {
        int len = size <= 128 ? size << 1 : (int) Math.round(size * 1.5);
        Collection<Node>[] latestBuckets = createTable(len);
        //  reHash
        reHashing(latestBuckets);
    }

    private void reHashing(Collection<Node>[] latestCol) {
        int mod = latestCol.length;
        for (var col : buckets) {
            if (col != null) {
                for (var node : col) {
                    var idx = processHashcode(node.key.hashCode(), mod);
                    latestCol[idx].add(node);
                }
            }
        }
        buckets = latestCol;
    }

    /**
     * Returns a Set view of the keys contained in this map.
     */
    @Override
    public Set<K> keySet() {
        return Arrays.stream(buckets)
                .flatMap(i -> i.stream().map(j -> j.key))
                .collect(Collectors.toSet());
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException.
     *
     * @param key
     */
    @Override
    public V remove(K key) {
        var col = getBucket(key);
        var opNode = findNodeStream(col, key).findAny();
        if (opNode.isPresent()) {
            var node = opNode.get();
            col.remove(node);
            return node.value;
        }
        return null;
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.
     *
     * @param key
     * @param value
     */
    @Override
    public V remove(K key, V value) {
        var col = getBucket(key);
        var opNode = findNodeStream(col, key).findAny();
        Node node;
        if (opNode.isPresent() && (node = opNode.get()).value.equals(value)) {
            col.remove(node);
            return node.value;
        }
        return null;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<K> iterator() {
        return new MyHashMapIter<>();
    }

    private class MyHashMapIter<K> implements Iterator<K> {

        private int iSize;

        private K[] arrayK;

        protected MyHashMapIter() {
            iSize = 0;
            arrayK = (K[]) keySet().toArray();
        }
        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return iSize != size;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public K next() {
            return arrayK[iSize++];
        }
    }

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        this(INITIAL_SIZE);
    }

    public MyHashMap(int INITIALSIZE) {
        this(INITIALSIZE, loadFactor);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param INITIALSIZE initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int INITIALSIZE, double maxLoad) {
        loadFactor = maxLoad;
        buckets = createTable(INITIALSIZE);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new HashSet<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] ans = new Collection[tableSize];
        IntStream.range(0, tableSize)
                .forEach(i -> ans[i] = createBucket());
        return ans;
    }

    // Your code won't compile until you do so!

}
