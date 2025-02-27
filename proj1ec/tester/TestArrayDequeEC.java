package tester;

import static org.junit.Assert.*;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {

    @Test
    public void testStudent() {
        var deque = new RecordDeque<String>();
        var a = new StudentArrayDeque<Integer>();
        var b = new ArrayDequeSolution<Integer>();

        for (int i = 0; i < 500; i++) {
            int rand = (int) (Math.random()*10);
            if (rand > 6) {
                deque.addLast("addLast("+i+")\n");
                a.addLast(i);
                b.addLast(i);
            } else if (rand > 3) {
                deque.addLast("addFirst("+i+")\n");
                a.addFirst(i);
                b.addFirst(i);
            } else if (!a.isEmpty()) {
                deque.addLast("removeFirst()\n");
                var a1 = a.removeFirst();
                var b1 = b.removeFirst();
                assertEquals(deque.toString(),a1, b1);
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
