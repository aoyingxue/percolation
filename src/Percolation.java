import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // the row and column indices are integers between 1 and n,
    // where (1, 1) is the upper-left site

    private boolean[][] grid; //open or not

    private int gridSize; //count of the row/column
    private int gridCount; //count of the grid

    private int source; //virtual top grid
    private int sink; //virtual bottom grid

    private WeightedQuickUnionUF wGrid; //to test connected or not
    private WeightedQuickUnionUF wFull;

    private int openSites; //how many opened

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n){
        if (n<=0){
            throw new IllegalArgumentException("N must be > 0");
        }

        gridSize=n;
        gridCount=n*n;

        // whether open or blocked
        // index starts from 0
        grid=new boolean[gridSize][gridSize];

        source=0;
        sink=gridCount+1;

        // number of open sites
        openSites=0; //at first, no grid is opened

        wGrid=new WeightedQuickUnionUF(gridCount+2); //includes top and bottom
        wFull=new WeightedQuickUnionUF(gridCount+1); //includes top; avoid whitewash
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col){
        //if the site is outside its prescribed range
        validate(row,col);

        int upperRow=row-1;
        int leftCol=col-1;
        int index=encode(row,col);

        // if it's open already
        if (isOpen(row,col)){
            return;
        }

        // if it's not open, open the site
        grid[upperRow][leftCol]=true;

        // update the number of open sites
        openSites+=1;

        // update wGrid and wFull by union
        // first row: union the top with the open one
        if (row==1){
            wGrid.union(source,index);
            wFull.union(source,index);
        }
        // last row: union the bottom with the open one
        if (row==gridSize){
            wGrid.union(index,sink);
        }

        // check and union the left col if it's open
        if (isOnGrid(row,col-1)&&isOpen(row,col-1)){
            wGrid.union(index,encode(row,col-1));
            wFull.union(index,encode(row,col-1));
        }
        if (isOnGrid(row,col+1) && isOpen(row,col+1)){
            wGrid.union(index,encode(row,col+1));
            wFull.union(index,encode(row,col+1));
        }
        if (isOnGrid(row-1,col) && isOpen(row-1,col)){
            wGrid.union(index,encode(row-1,col));
            wFull.union(index,encode(row-1,col));
        }
        if (isOnGrid(row+1,col) && isOpen(row+1,col)){
            wGrid.union(index,encode(row+1,col));
            wFull.union(index,encode(row+1,col));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        //if the site is outside its prescribed range
        validate(row,col);
        return grid[row-1][col-1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        //if the site is outside its prescribed range
        validate(row,col);
        return (wFull.find(source)==wFull.find(encode(row,col)));
    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        return openSites;
    }

    // does the system percolate?
    public boolean percolates(){
        return (wGrid.find(source)==wGrid.find(sink));
    }

    // test client (optional)
    public static void main(String[] args){

    }

    // give every grid an index number in union-find algorithm
    private int encode(int row, int col){
        // row and col starts from 1
        // index number starts from 1
        return gridSize*(row-1)+col;
    }

    // to check if the site is outside the prescribed range and throw an IllegalArgumentException
    private boolean isOnGrid(int row, int col){
        return (row>=1 && col>=1 && row<=gridSize && col<=gridSize);
    }
    private void validate(int row, int col){
        if (!isOnGrid(row,col)){
            throw new IllegalArgumentException("The site is not valid.");
        }
    }

}
