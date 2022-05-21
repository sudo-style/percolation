import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    private static final double CONFIDENCE_LEVEL = 1.96;
    private final int trials;
    private final double[] sample;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        this.trials = trials;
        this.sample = new double[trials];
        // go though all of the trials
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            // randomly chose a row, col
            while (!p.percolates()) {
                int r = StdRandom.uniform(n);
                int c = StdRandom.uniform(n);
                while (p.isOpen(r, c)) {
                    r = StdRandom.uniform(n);
                    c = StdRandom.uniform(n);
                }
                p.open(r, c);
            }
            sample[i] = (double) p.numberOfOpenSites() / (double) (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(sample);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(sample);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONFIDENCE_LEVEL * stddev() / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONFIDENCE_LEVEL * stddev() / Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        //int n = StdIn.readInt();
        //int trials = StdIn.readInt();
        //time how long it takes to run the code

        int[] sizes = {100, 200, 300, 400, 500};
        int[] trials = {62,125,250,500,1000,2000};

        double[][] times = new double[sizes.length][trials.length];
        for (int i = 0; i < sizes.length; i++) {
            for (int j = 0; j < trials.length; j++) {
                Stopwatch timer = new Stopwatch();
                PercolationStats ps = new PercolationStats(sizes[i], trials[j]);
                times[i][j] = timer.elapsedTime();
            }
            StdOut.println("Done with n = " + sizes[i]);
        }
        //print the times
        for (int i = 0; i < sizes.length; i++) {
            for (int j = 0; j < trials.length; j++) {
                StdOut.print(times[i][j] + " ");
            }
            StdOut.println();
        }
 
        Stopwatch timer = new Stopwatch();
        int n = StdIn.readInt();
        int trial = StdIn.readInt();
        PercolationStats stats = new PercolationStats(n, trial);
        double time = timer.elapsedTime();
        StdOut.printf("%-24s = %3.6f%n", "mean", stats.mean());
        StdOut.printf("%-24s = %3.18f%n", "stddev", stats.stddev());
        StdOut.printf("%-24s = [%17s, %3.16f]", "95% confidence interval",
                        stats.confidenceLo(), stats.confidenceHi());
        StdOut.println("\n" + time+ " n: "+n + " trials: "+stats.trials);
        StdOut.println("estimated time completion = ((3n*n)-(300*n)+(20*trials))/60000");
    }
}
