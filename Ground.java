import java.util.*;
import TunnelMaker;

/**
 * @author Vincent Qin
 * @version 1.0
 */
public class Ground extends Board{
    private int[][] grid;

    /**
     * instantiates a 2d array of numbers from 0-3
     * 0 represents dirt, 1 represents air, 2 represents cannonballs, and 3 represents eggs
     * @param r number of rows
     * @param c number of columns
     */
    public Ground(int r, int c) {
        super(r, c);
    }

    /**
     * returns the item at r, c
     */
    public int getItem(int r, int c) {
        return grid[r][c];
    }

    /**
     * remakes the array using tunnelmaker to generate random tunnels, will add eggs later
     */
    public void reset() {
        Arrays.fill(grid, 0);
        TunnelMaker t = new TunnelMaker();
        for (int i = 0; i < 5; i++) { //num tunnels is arbitrary rn
            t.generateTunnel(grid);
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
}
