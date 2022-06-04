import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation2{
    private final int n;
    private boolean[] open;
    private final WeightedQuickUnionUF ufTopToBottom;
    private final WeightedQuickUnionUF ufBottomToTop;
    // private final WeightedQuickUnionUF uf; // this will be used to see if the system percolates
    private int numberOfOpenSites;
    private final int top;
    private final int bottom;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation2(int n) {
        this.n = n;
        this.top = n * n;
        this.bottom = n * n + 1;
        this.open = new boolean[n * n];
        this.ufTopToBottom = new WeightedQuickUnionUF(n * n + 2);
        this.ufBottomToTop = new WeightedQuickUnionUF(n * n + 2);
        // this.uf = new WeightedQuickUnionUF(n * n + 2);
        this.numberOfOpenSites = 0;

        // connect the virtual sites at the top
        for (int i = 0; i < n; i++) {
            ufTopToBottom.union(i, top);
        }
        // connect the virtual sites at the bottom
        int bottomLeft = n * n - n;
        for (int i = bottomLeft; i < top; i++) {
            ufBottomToTop.union(i, bottom);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col)) {
            return;
        }
        if (row > n || col > n || row < 1 || col < 1) {
            throw new IndexOutOfBoundsException("row or column fuck you out of bounds");
        }
        open[flattenGrid(row, col)] = true;
        numberOfOpenSites += 1;

        int[] topDownLeftRight = {
                flattenGrid(row + 1, col),  // bottom
                flattenGrid(row - 1, col),  // top
                flattenGrid(row, col + 1),  // right
                flattenGrid(row, col - 1),  // left
        };

        for (int i = 0; i < topDownLeftRight.length; i++) {
            if (isValid(topDownLeftRight[i])) {
                if (open[topDownLeftRight[i]]) {
                    ufTopToBottom.union((row - 1) * n + (col - 1), topDownLeftRight[i]);
                    ufBottomToTop.union((row - 1) * n + (col - 1), topDownLeftRight[i]);
                }
            }
        }
    }

    private boolean isValid(int index) {
        return index > 0 && index < n * n;
    }

    // this will convert the row and column to a flattened index
    private int flattenGrid(int row, int col) {
        return (row - 1) * n + (col - 1);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row > n || col > n || row < 1 || col < 1) {
            return false;
        } // checks if the site is valid
        return open[(row - 1) * n + col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        // is it open and connected to the top?
        return isOpen(row, col) && ufTopToBottom.find((row - 1) * n + col - 1) == ufTopToBottom
                .find(top);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        for (int i = 0; i < n; i++) {
            if (ufTopToBottom.find(n * n - n + i) == ufTopToBottom.find(top)) {
                return true;
            }
        }
        return false;
    }

    private boolean isConnectedToBottom(int row, int col) {
        return isOpen(row, col) && ufBottomToTop.find((row - 1) * n + col - 1) == ufBottomToTop
                .find(bottom);
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
        Percolation2 perc = new Percolation2(10);
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
