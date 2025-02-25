package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> {

    private T[] array;

    private int len;

    private int first;

    private int last;

    private final int MIN_LEN = 8;

    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        array = (T[]) new Object[MIN_LEN];
        len = MIN_LEN;
        first = 0;
        last = 0;
        comparator = c;
    }

    private void addFirst(T item) {
        if (isEmpty()) {
            addLast(item);
            return;
        }

        if (isFull()) {
            reSize(len < 128 ? len << 1 : (int) (len * 1.1));
        }

        first = (first - 1 + len) % len;
        array[first] = item;
    }

    public void addLast(T item) {
        if (isFull()) {
            reSize(len < 128 ? len << 1 : (int) (len * 1.1));
        }
        array[last] = item;
        last = (last + 1) % len;
    }

    private boolean isEmpty() {
        return first == last;
    }

    private int size() {
        return (last + len - first) % len;
    }

    private void printDeque() {
        if (!isEmpty()) {
            int idx = 0;
            int size = size();
            while (idx < size) {
                System.out.printf("%s ", array[(first + idx++) % len]);
            }
        }
        System.out.println();
    }

    private T removeFirst() {
        if (isEmpty()) {
            return null;
        }

        T t = array[first];
        first = (first - 1 + len) % len;
        int cap = size();
        if (cap >= 16 && cap < len / 4) {
            reSize(Math.max(MIN_LEN, (int) (cap * 1.1)));
        }
        return t;
    }

    private T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T t = array[(len + (--last)) % len];
        int cap = size();
        if (cap >= 16 && cap < len / 4) {
            reSize(Math.max(MIN_LEN, (int) (cap * 1.1)));
        }
        return t;
    }

    private T get(int index) {
        return index >= size() ? null : array[(first + index) % len];
    }

    private boolean isFull() {
        return (last + 1) % len == first;
    }

    private void reSize(int cap) {
        T[] newArr = (T[]) new Object[cap];
        int idx = 0;
        int size = size();
        while (idx < size) {
            newArr[idx] = array[(first + idx) % len];
            idx++;
        }
        first = 0;
        last = idx;
        len = cap;
    }

    public T max() {
        if (isEmpty()) {
            return null;
        }

        T ans = array[first];
        int cap = size();
        for (int i = 1; i < cap; i++) {
            T t = array[(first + i) % len];
            if (comparator.compare(ans, t) < 0) {
                ans = t;
            }
        }
        return ans;
    }

    public T max(Comparator<T> c){
        if (isEmpty()) {
            return null;
        }

        T ans = array[first];
        int size = size();
        for (int i = 1; i < size; i++) {
            T t = array[(first + i) % len];
            if (c.compare(ans, t) < 0) {
                ans = t;
            }
        }
        return ans;
    }
}
