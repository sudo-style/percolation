import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private boolean[] open;
    private final WeightedQuickUnionUF uf;
    private int numberOfOpenSites;
    private final int top;
    private final int bottom;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        this.n = n;
        this.top = n * n;
        this.bottom = n * n + 1;
        this.open = new boolean[n * n + 2];
        this.uf = new WeightedQuickUnionUF(n * n + 2);
        this.numberOfOpenSites = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkRange(row, col);
        if (isOpen(row, col)) return;
        int cell = flattenGrid(row, col);
        open[cell] = true;
        numberOfOpenSites++;


        // top
        if (row == 1) {
            union(cell, top);
        }
        else if (isOpen(row - 1, col)) {
            union(flattenGrid(row - 1, col), cell);
        }

        // right
        if (col != n && isOpen(row, col + 1)) {
            union(flattenGrid(row, col + 1), cell);
        }

        // bottom
        if (row == n) {
            union(cell, bottom);
        }
        else if (isOpen(row + 1, col)) {
            union(flattenGrid(row + 1, col), cell);
        }

        // left
        if (col != 1 && isOpen(row, col - 1)) {
            union(flattenGrid(row, col - 1), cell);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkRange(row, col);
        return open[flattenGrid(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkRange(row, col);
        return uf.find(top) == uf.find(flattenGrid(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(top) == uf.find(bottom);
    }

    private boolean isConnectedToBottom(int row, int col) {
        checkRange(row, col);
        return uf.find(bottom) == uf.find(flattenGrid(row, col));
    }

    private void union(int a, int b) {
        uf.union(a, b);
    }

    private void checkRange(int row, int col) {
        if (row <= 0 || col <= 0 || row > n || col > n) throw new IndexOutOfBoundsException();
    }

    private int flattenGrid(int row, int column) {
        return (n * (row - 1)) + column - 1;
    }

    // outputs to the console a model of the system
    private void view() {
        for (int row = 1; row <= n; row++) {
            for (int col = 1; col <= n; col++) {
                // if the cell is empty
                if (!open[flattenGrid(row, col)]) {
                    StdOut.print(". ");
                }
                // the cell is open if it is not connected to the top
                else if (isFull(row, col) && isConnectedToBottom(row, col)) {
                    StdOut.print("P ");
                }
                else if (isFull(row, col)) {
                    StdOut.print("F ");
                }

                else if (isOpen(row, col)) {
                    StdOut.print("O ");
                }

                // the cell percolates if it is connected to the top and bottom
                else {
                    StdOut.print("P ");
                }
            }
            StdOut.print("\n");
        }
        StdOut.println("\n");
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation perc = new Percolation(10);
        while (!perc.percolates()) {
            System.out.println();
            int row = StdRandom.uniform(perc.n) + 1;
            int col = StdRandom.uniform(perc.n) + 1;
            System.out.println("Open: " + row + ", " + col);
            perc.open(row, col);
            perc.view();
        }
        System.out.println(perc.numberOfOpenSites());


    }
}
