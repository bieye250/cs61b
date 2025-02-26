package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {


    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        comparator = c;
    }

    public T max() {
        if (isEmpty()) {
            return null;
        }

        T ans = get(0);
        int cap = size();
        for (int i = 1; i < cap; i++) {
            T t = get(i);
            if (comparator.compare(ans, t) < 0) {
                ans = t;
            }
        }
        return ans;
    }

    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }

        T ans = get(0);
        int size = size();
        for (int i = 1; i < size; i++) {
            T t = get(i);
            if (c.compare(ans, t) < 0) {
                ans = t;
            }
        }
        return ans;
    }
}
