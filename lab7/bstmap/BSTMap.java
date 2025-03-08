package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private BSTNode<K, V> root;

    private int size;

    public BSTMap() {
        size = 0;
        root = null;
    }
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

//    private void clearHelp(BSTNode<K, V> h) {
//        if (h == null) {
//            return;
//        }
//        clearHelp(h.left);
//        clearHelp(h.right);
//        h = null;
//    }

    @Override
    public boolean containsKey(K key) {
       return containsKey(root, key);
    }

    private boolean containsKey(BSTNode<K,V> h, K key) {
        if (h == null) {
            return false;
        }
        if (h.key.equals(key)) {
            return true;
        } else if (h.key.compareTo(key) > 0) {
            return containsKey(h.left, key);
        } else {
            return containsKey(h.right, key);
        }
    }

    @Override
    public V get(K key) {
        return get(root, key);
    }

    private V get(BSTNode<K, V> h, K key) {
        if (h == null) {
            return null;
        }
        if (h.key.equals(key)) {
            return h.val;
        } else if (h.key.compareTo(key) > 0) {
            return get(h.left, key);
        } else {
            return get(h.right, key);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        root = put(root, key, value);
        size++;
    }

    private BSTNode put(BSTNode<K, V> h, K key, V val) {
        if (h == null) {
            return new BSTNode<>(key, val);
        } else if (h.key.compareTo(key) > 0) {
            h.left = put(h.left, key, val);
        } else {
            h.right = put(h.right, key, val);
        }
        return h;
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();

//        return Set.of();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();

//        return null;
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();

//        return null;
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
//        return null;
    }

    private class BSTNode<K extends Comparable<K>, V> {
        private K key;
        private V val;

        private BSTNode<K, V> left;
        private BSTNode<K, V> right;

        public BSTNode(K k, V v) {
            key = k;
            val = v;
        }
    }

    public void printOrder() {
        print(root);
    }

    private void print(BSTNode<K, V> h){
        if (h == null) {
            return;
        }
        print(h.left);
        System.out.println(h.key+" "+h.val);
        print(h.right);
    }
}
