package deque;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    public void testAdd(){
        ArrayDeque<Integer> a = new ArrayDeque<>();

        a.addLast(1);
        a.addLast(2);
        a.addLast(3);
        assertEquals(3, a.size());
        a.printDeque();

        a.addFirst(0);
        a.addFirst(-1);
        assertEquals(5, a.size());

        a.printDeque();
    }


    @Test
    public void testRemove(){
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>();
        arrayDeque.addFirst(0);
        Integer i = arrayDeque.removeLast();
        assertEquals(0, arrayDeque.size());
        assertTrue(arrayDeque.size() == 0);
        assertEquals(0, (int) i);

        arrayDeque.addLast(1);
        i = arrayDeque.removeFirst();
        assertEquals(0, arrayDeque.size());
        assertTrue(arrayDeque.size() == 0);
        assertEquals(1, (int)i);
        assertNull(arrayDeque.removeLast());
        assertNull(arrayDeque.removeFirst());
    }

    @Test
    public void testResize(){
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>();
        for (int i = 500; i > 0; i--) {
            arrayDeque.addFirst(i);
        }
        assertEquals(500, arrayDeque.size());
        for (int i = 501; i <= 1000; i++) {
            arrayDeque.addLast(i);
        }
        assertEquals(1000, arrayDeque.size());
        for (int i = 0; i < 1000; i++) {
            assertEquals(i+1, (int) arrayDeque.get(i));
        }

        for (int i = 1000; i > 500; i--) {
            Integer t = arrayDeque.removeLast();
            assertEquals(i, (int) t);
        }
        for (int i = 1; i <= 500; i++){
            Integer t = arrayDeque.removeFirst();
            assertEquals(i, (int) t);
        }
        assertEquals(8, arrayDeque.size());
    }

    @Test
    public void testSame(){
        ArrayDeque<Integer> a = new ArrayDeque<>();
        a.addFirst(0);a.addLast(1);

        ArrayDeque<Integer> b = new ArrayDeque<>();
        b.addLast(0);b.addLast(1);

        assertTrue(a.equals(b));
    }

    @Test
    public void testIterator(){
        ArrayDeque<Integer> a = new ArrayDeque<>();
        Iterator<Integer> iterator = a.iterator();
        a.addLast(0);
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testSameWithLinkedDeque(){
        Deque<Integer> a = new ArrayDeque<>();
        Deque<Integer> b = new LinkedListDeque<>();

        a.addLast(0);b.addLast(0);
        assertTrue(a.equals(b));
    }
}
