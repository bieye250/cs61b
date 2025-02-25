package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {

    private class Node<T> {
        T t;

        Node<T> next;

        Node<T> prev;
    }

    private Node<T> first;

    private int size;

    public LinkedListDeque() {
        first = new Node<>();
        size = 0;
        first.next = first;
        first.prev = first;
    }

    @Override
    public void addFirst(T t) {
        Node<T> node = new Node<>();
        node.t = t;
        if (size == 0) {
            first.next = node;
            node.next = first;
            node.prev = first;
            first.prev = node;
        } else {
            Node<T> temp = first.next;
            first.next = node;
            node.next = temp;
            temp.prev = node;
            node.prev = first;
        }
        size++;
    }

    @Override
    public void addLast(T t) {
        Node<T> node = new Node<>();
        node.t = t;
        if (size == 0) {
            first.next = node;
            node.next = first;
            node.prev = first;
            first.prev = node;
        } else {
            Node<T> temp = first.prev;
            temp.next = node;
            node.next = first;
            node.prev = temp;
            first.prev = node;
        }
        size++;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        if (!isEmpty()) {
            Node<T> node = first.next;
            for (int i = 0; i < size - 1; i++) {
                System.out.printf("%s ", node.t.toString());
                node = node.next;
            }
            System.out.println(node.t.toString());
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Node<T> node = first.next;
        if (size == 1) {
            first.next = first;
            first.prev = first;
        } else {
            first.next = node.next;
            node.next.prev = first;
        }
        size--;
        return node.t;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        Node<T> last = first.prev;
        if (size == 1) {
            first.next = first;
            first.prev = first;
        } else {
            first.prev = last.prev;
            last.prev.next = first;
        }
        size--;
        return last.t;
    }

    @Override
    public T get(int index) {
        if (size <= index) {
            return null;
        }
        Node<T> node = first.next;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node.t;
    }

    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LinkedListDeque)) {
            return false;
        }

        Iterator<T> i1 = iterator();
        Iterator<?> i2 = ((LinkedListDeque<?>) o).iterator();
        while (i1.hasNext() && i2.hasNext()) {
            T t1 = i1.next();
            Object t2 = i2.next();
            if (!(t1 == null || t2 == null || t1.equals(t2))) {
                return false;
            }
        }
        return !(i1.hasNext() || i2.hasNext());
    }

    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        return getRecursiveHelp(index, first.next);
    }

    private T getRecursiveHelp(int index, Node<T> node) {
        if (index == 0) {
            return node.t;
        }
        return getRecursiveHelp(--index, node.next);
    }

    private class Itr implements Iterator<T> {

        int index = 0;

        @Override
        public boolean hasNext() {
            return index != size;
        }

        @Override
        public T next() {
            if (index < size) {
                return get(index++);
            }
            else {
                return null;
            }
        }
    }
}
