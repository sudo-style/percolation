import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private boolean[] open;
    private final WeightedQuickUnionUF ufTopToBottom;
    private final WeightedQuickUnionUF ufBottomToTop;
    private int numberOfOpenSites;
    private final int top;
    private final int bottom;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        this.n = n;
        this.top = n * n;
        this.bottom = n * n + 1;
        this.open = new boolean[n * n];
        this.ufTopToBottom = new WeightedQuickUnionUF(n * n + 2);
        this.ufBottomToTop = new WeightedQuickUnionUF(n * n + 2);
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
        if (!isOpen(row, col)) {
            numberOfOpenSites += 1;
        }
        open[flattenGrid(row, col)] = true;
        if (isOnGrid(row - 1, col) && isOpen(row - 1, col)) { // top
            ufTopToBottom.union(flattenGrid(row, col), flattenGrid(row - 1, col));
            ufBottomToTop.union(flattenGrid(row, col), flattenGrid(row - 1, col));
        }
        if (isOnGrid(row, col + 1) && isOpen(row, col + 1)) { // right
            ufTopToBottom.union(flattenGrid(row, col), flattenGrid(row, col + 1));
            ufBottomToTop.union(flattenGrid(row, col), flattenGrid(row, col + 1));
        }
        if (isOnGrid(row + 1, col) && isOpen(row + 1, col)) { // bottom
            ufTopToBottom.union(flattenGrid(row, col), flattenGrid(row + 1, col));
            ufBottomToTop.union(flattenGrid(row, col), flattenGrid(row + 1, col));
        }
        if (isOnGrid(row, col - 1) && isOpen(row, col - 1)) { // left
            ufTopToBottom.union(flattenGrid(row, col), flattenGrid(row, col - 1));
            ufBottomToTop.union(flattenGrid(row, col), flattenGrid(row, col - 1));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return open[flattenGrid(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        // is it open and connected to the top?
        return isOpen(row, col) && ufTopToBottom.find(flattenGrid(row, col)) == ufTopToBottom
                .find(top);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        // is any site on the bottom connected to the top
        for (int i = 0; i < n; i++) {
            if (ufTopToBottom.find(n * n - n + i) == ufTopToBottom.find(top)) {
                return true;
            }
        }
        return false;
    }

    private int flattenGrid(int row, int col) {
        return row * n + col;
    }

    private boolean isOnGrid(int row, int col) {
        return row >= 0 && row < n && col >= 0 && col < n;
    }

    private boolean isConnectedToBottom(int row, int col) {
        return isOpen(row, col) && ufBottomToTop.find(flattenGrid(row, col)) == ufBottomToTop
                .find(bottom);
    }

    // outputs to the console a model of the system
    private void viewOpen() {
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
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
        int n = 10;
        Percolation p = new Percolation(n);
        while (!p.percolates()) {
            // randomly chose between 0 to n-1
            int row = StdRandom.uniform(n);
            int col = StdRandom.uniform(n);
            if (!p.isOpen(row, col)) {
                p.open(row, col);
                p.viewOpen();
            }
        }
        System.out.println(p.numberOfOpenSites());

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (p.isFull(i, j)) {
                    System.out.print("T ");
                }
                else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }

        System.out.println(" ");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (p.isConnectedToBottom(i, j)) {
                    System.out.print("B ");
                }
                else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }
}
