package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {

    @Test
    public void testStudent() {
        var deque = new RecordDeque<String>();
        var a = new StudentArrayDeque<Integer>();
        var b = new ArrayDequeSolution<Integer>();

        for (int i = 0; i < 5000; i++) {
            int rand = (int) (StdRandom.random() * 10);
            if (rand > 8) {
                deque.addLast("addLast("+i+")\n");
                a.addLast(i);
                b.addLast(i);
                assertEquals(deque.toString(),a.size(), b.size());
            } else if (rand > 6) {
                deque.addLast("addFirst("+i+")\n");
                a.addFirst(i);
                b.addFirst(i);
                assertEquals(deque.toString(),a.size(), b.size());
            } else if (rand > 4 && a.size() > 0 && b.size() > 0) {
                deque.addLast("removeFirst()\n");
                var a1 = a.removeFirst();
                var b1 = b.removeFirst();
                assertEquals(deque.toString(),a1, b1);
            } else if (a.size() > 0 && b.size() > 0) {
                deque.addLast("removeLast()\n");
                var a1 = a.removeLast();
                var b1 = b.removeLast();
                assertEquals(deque.toString(), a1, b1);
            }
        }
    }

    private class RecordDeque<T> extends ArrayDequeSolution<T> {
        private final int LEN = 4;

        @Override
        public void addLast(T t){
            while (size() > LEN) {
                removeFirst();
            }
            super.addLast(t);
        }

        @Override
        public String toString(){
            String str = "";
            int n = size(), i = 0;
            for (; i < n; i++) {
                str += get(i).toString();
            }
            return str;
        }
    }
}
