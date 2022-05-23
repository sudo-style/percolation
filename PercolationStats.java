import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

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
                int row = StdRandom.uniform(n);
                int col = StdRandom.uniform(n);
                while (p.isOpen(row, col)) {
                    row = StdRandom.uniform(n);
                    col = StdRandom.uniform(n);
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
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, trials);
        StdOut.printf("%-24s = %3.6f%n", "mean", stats.mean());
        StdOut.printf("%-24s = %3.18f%n", "stddev", stats.stddev());
        StdOut.printf("%-24s = [%17s, %3.16f]", "95% confidence interval",
                      stats.confidenceLo(), stats.confidenceHi());
    }
}
