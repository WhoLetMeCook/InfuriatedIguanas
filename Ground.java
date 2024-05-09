import java.util.*;

/**
 * @author Vincent Qin
 * @version 1.0
 */
public class Ground extends Board{
    private int[][] grid;

    /**
     * instantiates a 2d array of numbers from 0-4
     * 0 represents dirt, 1 represents air, 2 stone, 3 eggs, and 4 cannonballs
     * @param r number of rows
     * @param c number of columns
     */
    public Ground(int r, int c) {
        super(r, c);
    }

    /**
     * returns the item at r, c
     * @return an int 0-3
     */
    public int getItem(int r, int c) {
        return grid[r][c];
    }

    /**
     * remakes the array using tunnelmaker to generate random tunnels, will add eggs later
     */
    public void reset() {
        for (int i = 0; i < getRow(); i++) {
            for (int j = 0; j < getCol(); j++) {
                grid[i][j] = 0;
            }
        }
        
        TunnelMaker t = new TunnelMaker(100, 0.6);
        for (int i = 0; i < 5; i++) { //num tunnels is arbitrary rn
            t.generateTunnel(this);
        }
        Random r = new Random();
        for (int i = 0; i < 3; i++) {
            grid[r.nextInt(getRow()/2, getRow())][r.nextInt(getCol())] = 3;
        }
    }

    /**
     * set item at r, c to int v (meanings in constructor)
     * @param r row to set
     * @param c col to set
     * @param v the item to set it to
     */
    public void setItem(int r, int c, int v) {
        grid[r][c] = v;
    }

    /**
     * counts a specfic item, will usually be for eggs, sometimes for cannonballs
     * @param item the item to count
     * @return returns the number of items in grid
     */
    public int countItem(int item) {
        int ans = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == item) {
                    ans++;
                }
            }
        }
        return ans;
    }

    /**
     * displays the ground as a 2d array
     */
    public void display() {
        for (int i = 0; i < getRow(); i++) {
            for (int j = 0; j < getCol(); j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
}
