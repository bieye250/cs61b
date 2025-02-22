package deque;

public class ArrayDeque<T> implements Deque<T> {

    private T[] array;

    private int first;

    private int last;

    private int len;

    public ArrayDeque() {
        array = (T[]) new Object[8];
        first = 0;
        last = 0;
        len = 8;
    }

    @Override
    public void addFirst(T item) {
        if (isEmpty()) {
            addLast(item);
            return ;
        }
        if (isFull())
            reSize(len < 128 ? len << 1 : (int) Math.round(len * 1.1));
        first = (first + len - 1) % len;
        array[first] = item;

    }

    @Override
    public void addLast(T item) {
        if (isFull())
            reSize(len < 128 ? len << 1 : (int) Math.round(len * 1.1));
        array[last] = item;
        last = (last + 1) % len;
    }

    @Override
    public boolean isEmpty() {
        return first == last;
    }

    @Override
    public int size() {
        return (last + len - first) % len;
    }

    @Override
    public void printDeque() {
        if (!isEmpty()) {
            int s = size();
            int i = 0;
            for (; i < s - 1; i++) {
                System.out.printf("%s ", array[(first + i) % len]);
            }
            System.out.println(array[(first + i) % len]);
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) return null;
        T t = array[first++];
        first %= len;
        int s = size();
        if (len >= 16 && s < len / 4) {
            reSize((Math.max((int) Math.round(s * 1.1), 8)));
        }
        return t;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) return null;

        last = (len + (--last)) % len;
        T t = array[last];
        int s = size();
        if (len >= 16 && s < len / 4) {
            reSize((int) Math.round(s * 1.1));
        }
        return t;
    }

    @Override
    public T get(int index) {
        return index >= size() ? null : array[(first + index) % len];
    }

    private boolean isFull() {
        return (last + 1) % len == first;
    }

    private void reSize(int cap) {
        T[] newArray = (T[]) new Object[cap];
        int i = 0;
        for (; i < size(); i++) {
            newArray[i] = array[(first + i) % len];
        }
        array = newArray;
        first = 0;
        last = i;
        len = cap;
    }
}
