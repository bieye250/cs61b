package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        AList<Integer> ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> op = new AList<>();

        ns.addLast(1000);
        ns.addLast(2000);
        ns.addLast(4000);
        ns.addLast(8000);
        ns.addLast(16000);
        ns.addLast(32000);
        ns.addLast(64000);

        int m = 10000;
        for (int i = 0; i < 7; i++) {
            op.addLast(m);
        }

        for (int i = 0; i < 7; i++) {
            SLList<Integer> aList = new SLList<>();
            for (int j = 0; j < ns.get(i); j++) {
                aList.addLast(j);
            }
            Stopwatch stopwatch = new Stopwatch();
            for (int j = 0; j < m; j++) {
                aList.getLast();
            }
            double elapsedTime = stopwatch.elapsedTime();
            times.addLast(elapsedTime);
        }

        printTimingTable(ns, times, op);
    }

}
