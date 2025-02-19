package deque;

import org.junit.Test;

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
        assertTrue(arrayDeque.isEmpty());
        assertEquals(0, (int) i);

        arrayDeque.addLast(1);
        i = arrayDeque.removeFirst();
        assertEquals(0, arrayDeque.size());
        assertTrue(arrayDeque.isEmpty());
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
    }
}
