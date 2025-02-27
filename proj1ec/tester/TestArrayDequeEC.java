package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {

    private final int FIRST = 0;

    @Test
    public void testStudent() {
        var deque = new RecordDeque<String>();
        var a = new StudentArrayDeque<Integer>();
        var b = new ArrayDequeSolution<Integer>();

        for (int i = 0; i < 100; i++) {
            int rand = StdRandom.uniform(0, 13);
            if (rand > 9) {
                a.addLast(i);
                b.addLast(i);

                deque.addLast("addLast("+i+")\n");
                assertEquals(deque.toString(), b.get(b.size()-1), a.get(a.size()-1));
            } else if (rand > 6) {
                deque.addLast("addFirst("+i+")\n");
                a.addFirst(i);
                b.addFirst(i);
                assertEquals(deque.toString(), b.get(FIRST), a.get(FIRST));
            } else if (rand > 1 && b.size() > 0) {
                deque.addLast("removeFirst()\n");
                var a1 = a.removeFirst();
                var b1 = b.removeFirst();
                assertEquals(deque.toString(),b1, a1);
            } else if (b.size() > 0) {
                deque.addLast("removeLast()\n");
                var a1 = a.removeLast();
                var b1 = b.removeLast();
                assertEquals(deque.toString(), b1, a1);
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
            int n = size();
            for (int i=0; i < n; i++) {
                str += get(i).toString();
            }
            return str;
        }
    }
}
