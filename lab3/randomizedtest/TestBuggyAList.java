package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE

//    @Test
//    public void testThreeAddThreeRemove(){
//        AListNoResizing<Integer> aList = new AListNoResizing<>();
//        BuggyAList<Integer> bList = new BuggyAList<>();
//
//        aList.addLast(4);aList.addLast(5);aList.addLast(6);
//        bList.addLast(4);bList.addLast(5);bList.addLast(6);
//
//        assertEquals(aList.size(), bList.size());
////        aList.addLast(6); bList.addLast(6);
//        assertEquals(aList.removeLast(), bList.removeLast());
//        assertEquals(aList.removeLast(), bList.removeLast());
//        assertEquals(aList.removeLast(), bList.removeLast());
//        assertEquals(aList.getLast(), bList.getLast());
//    }
//
//    @Test
//    public void randomizedTest(){
//        AListNoResizing<Integer> L = new AListNoResizing<>();
//
//        int N = 500;
//        for (int i = 0; i < N; i += 1) {
//            int operationNumber = StdRandom.uniform(0, 2);
//            if (operationNumber == 0) {
//                // addLast
//                int randVal = StdRandom.uniform(0, 100);
//                L.addLast(randVal);
//                System.out.println("addLast(" + randVal + ")");
//            } else if (operationNumber == 1) {
//                // size
//                int size = L.size();
//                System.out.println("size: " + size);
//            }
//        }
//    }

//    @Test
//    public void randomTestCall(){
//        AListNoResizing<Integer> l = new AListNoResizing<>();
//        int N = 100;
//        for (int i = 0; i < N; i++) {
//            int n = StdRandom.uniform(0, 2);
//            if(n > 0){
//                if(l.size() > 0)
//                    System.out.println("removeLast(" + l.removeLast() + ")");
//            }
//            else {
//                int r = StdRandom.uniform(0, 4);
//                l.addLast(r);
//            }
//        }
//    }

    @Test
    public void randomTestCom(){
        AListNoResizing<Integer> aList = new AListNoResizing<>();
        BuggyAList<Integer> bList = new BuggyAList<>();

        int N = 10000;
        for (int i = 0; i < N; i++) {
            int op = StdRandom.uniform(0,2);
            if(op > 0){
                if(aList.size() > 0 && bList.size() > 0)
                    assertEquals(aList.removeLast(), bList.removeLast());
            }
            else {
                int r = StdRandom.uniform(0, 100);
                aList.addLast(r);bList.addLast(r);
                assertEquals(aList.getLast(), bList.getLast());
            }
        }
    }
}
