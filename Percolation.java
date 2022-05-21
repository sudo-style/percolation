import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private boolean[] open;
    private final WeightedQuickUnionUF uf;
    private int numberOfOpenSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        this.n = n;
        this.open = new boolean[n * n];
        this.uf = new WeightedQuickUnionUF(n * n);
        this.numberOfOpenSites = 0;

        // connect the virtual sites at the top
        for (int i = 0; i < n; i++) {
            uf.union(i, n);
        }

        // connect the virtual sites at the bottom
        int bottomLeft = n * (n - 1);
        for (int i = 0; i < n; i++) {
            uf.union(bottomLeft + i, n * n - 1);
        }
    }

    private void validateCell(int row, int col) {
        if (!isOnGrid(row, col)) {
            throw new IllegalArgumentException("Out of bounds");
        }
    }

    private boolean isOnGrid(int row, int col) {
        return row >= 0 && row < n && col >= 0 && col < n;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateCell(row, col);
        if (!isOpen(row, col)) {
            numberOfOpenSites += 1;
        }
        open[rowColToIndex(row, col)] = true;
        // Check top, right, bottom left
        // make sure each is open
        // if a side is open, then union the this spot with that
        if (isOnGrid(row - 1, col)) { // top
            uf.union(rowColToIndex(row, col), rowColToIndex(row - 1, col));
        }
        if (isOnGrid(row, col + 1)) { // right
            uf.union(rowColToIndex(row, col), rowColToIndex(row, col + 1));
        }
        if (isOnGrid(row + 1, col)) { // bottom
            uf.union(rowColToIndex(row, col), rowColToIndex(row + 1, col));
        }
        if (isOnGrid(row, col - 1)) { // left
            uf.union(rowColToIndex(row, col), rowColToIndex(row, col - 1));
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateCell(row, col);
        return open[rowColToIndex(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validateCell(row, col);
        if (!isOpen(row,col)) return false;
        return uf.find(rowColToIndex(row, col)) == uf.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(0) == uf.find(n * n - 1);
    }

    // converts the 2d row/col to a 1d index
    private int rowColToIndex(int row, int col) {
        return col + row * n;
    }

    // outputs to the console a model of the system
    private void viewOpen() {
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (isFull(row, col)) {
                    StdOut.print("P,");
                }
                else if (isOpen(row,col)) {
                    StdOut.print("#,");
                }
                else {
                    StdOut.print(".,");
                }
            }
            StdOut.print("\n");
        }
        StdOut.println("\n");
    }

    // test client (optional)
    public static void main(String[] args) {
        StdOut.print("Grid Size: ");
        int n = StdIn.readInt();
        Percolation p = new Percolation(n);
        boolean keepGoing = true;
        while (keepGoing) {
            int r = StdIn.readInt();
            int c = StdIn.readInt();
            if (p.isOpen(r, c)) {
                keepGoing = false;
            }
            p.open(r, c);
            p.viewOpen();
            if (p.percolates()) {
                keepGoing = false;
            }
        }
    }
}

