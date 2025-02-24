package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

public class MaxArrayDequeTest {

    @Test
    public void testConstructComparator(){
        Comparator<Integer> comparator = (i, j) -> i - j;
        MaxArrayDeque<Integer> maxArrayDeque = new MaxArrayDeque<>(comparator);

        maxArrayDeque.addLast(1);
        maxArrayDeque.addLast(2);
        maxArrayDeque.addLast(4);

        assertEquals(4, (int) maxArrayDeque.max());
    }

    @Test
    public void testCustomComparator(){
        Comparator<Integer> comparator = (i, j) -> i - j;
        MaxArrayDeque<Integer> maxArrayDeque = new MaxArrayDeque<>(comparator);

        maxArrayDeque.addLast(1);
        maxArrayDeque.addLast(2);
        maxArrayDeque.addLast(4);

        comparator = (i, j) -> j - i;
        assertEquals(1, (int) maxArrayDeque.max(comparator));
    }
}
