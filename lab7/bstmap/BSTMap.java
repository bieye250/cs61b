package bstmap;

import java.util.HashSet;
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

    @Override
    public boolean containsKey(K key) {
        return get(root, key) != null;
    }

    @Override
    public V get(K key) {
        var node = get(root, key);
        return node == null ? null : node.val;
    }

    private BSTNode<K, V> get(BSTNode<K, V> h, K key) {
        if (h == null) {
            return null;
        }
        if (h.key.equals(key)) {
            return h;
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
        var set = new HashSet<K>();
        keySet(set, root);
        return set;
    }

    private void keySet(Set<K> set, BSTNode<K, V> h) {
        if (h == null) {
            return;
        }
        keySet(set, h.left);
        set.add(h.key);
        keySet(set, h.right);
    }

    @Override
    public V remove(K key) {
        var node = get(root, key);
        remove(root, key, node.val);
        return node.val;
    }

    private BSTNode<K, V> remove(BSTNode<K, V> h, K key, V val) {
        if (h == null) {
            return null;
        } else if (h.key.equals(key) && h.val.equals(val)) {
            // h is the leaf
            if (h.left == null && h.right == null) {
                return null;
            } else if (h.left == null) { // node only has right child
                var newNode = h.right;
                var parent = h;
                while (newNode.left != null) {
                    parent = newNode;
                    newNode = newNode.left;
                }
                if (parent == h) {
                    return newNode;
                }
                parent.left = newNode.right;
                newNode.right = h.right;
                return newNode;
            } else {    // node has left child
                var newNode = h.left;
                var parent = h;
                while (newNode.right != null) {
                    parent = newNode;
                    newNode = newNode.right;
                }
                if (h == parent) {
                    return newNode;
                }
                parent.right = newNode.left;
                newNode.left = h.left;
                newNode.right = h.right;
                h = newNode;
            }
        } else if (h.key.compareTo(key) > 0) {
            h.left = remove(h.left, key, val);
        } else if (h.key.compareTo(key) < 0){
            h.right = remove(h.right, key, val);
        }
        return h;
    }

    @Override
    public V remove(K key, V value) {
        remove(root, key, value);
        return value;
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTMapIter<>();
    }

    private class BSTMapIter<K> implements Iterator<K> {
        private K[] kArray;

        private int idx;

        BSTMapIter() {
            kArray = (K[]) keySet().toArray();
            idx = 0;
        }
        @Override
        public boolean hasNext() {
            return kArray.length != idx+1;
        }

        @Override
        public K next() {
            return kArray[idx++];
        }
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

    public void printInOrder() {
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
