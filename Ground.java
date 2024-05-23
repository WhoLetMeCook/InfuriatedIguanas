import java.util.*;
/**
 * @author Vincent Qin
 * @version 1.0
 */
public class Ground {
    private int[][] grid;
    private int row, col;
    private Grid display;
    private TunnelMaker t;

    /**
     * instantiates a 2d array of numbers from 0-4
     * 0 represents dirt, 1 represents air, 2 stone, 3 eggs, and 4 cannonballs
     * @param r number of rows
     * @param c number of columns
     */
    public Ground(int r, int c, int gridr) {
        row = r;
        col = c;
        grid = new int[r][c];
        display = new Grid(gridr, c, 10, 10);
        t = new TunnelMaker((row * col) / 7, 0.45);
    }

    /**
     * returns the item at r, c
     * @return an int 0-3
     */
    public int getItem(int r, int c) {
        return grid[r][c];
    }

    /**
     * @return # of rows
     */
    public int getRow() {
        return row;
    }

    /**
     * @return # of columns
     */
    public int getCol() {
        return col;
    }

    /**
     * remakes the array using tunnelmaker to generate random tunnels, will add eggs later
     */
    public void reset() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                grid[i][j] = 0;
            }
        }
        
        for (int i = 0; i < (int) (row + col) / 10; i++) { //num tunnels is arbitrary rn
            t.generateTunnel(this);
        }
        for (int i = 0; i < 6; i++) {
            t.placeEgg(this);
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
     * for testing purposes
     */
    public void display() {
        for (int i = 0; i < getRow(); i++) {
            for (int j = 0; j < getCol(); j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * displays the ground on the GUI
     */
    public void show() { // temporary method name until display() deleted
        int startRow = (int)(display.getSize().getHeight() - row);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (grid[i][j] == 0) {
                    // display dirt
                }
                else if (grid[i][j] == 1) {
                    // air --> basically do nothing?
                }
                else if (grid[i][j] == 2) {
                    // stone
                }
                else if (grid[i][j] == 3) {
                    // egg
                }
                else {
                    // cannonball
                    display.add(new Cannonball(i + startRow, j, this), i + startRow, j);
                }
            }
        }
    }

    /**
     * simulates dropping a cannonball at col c
     * @param c col to drop at
     */
    public void dropBall(int c) {
        Cannonball cannonball = new Cannonball(0, c, this);
        for (int i = 0; i < row; i++) {
            cannonball.damage(grid[i][c] + 1);
            if (cannonball.getDurability() <= 0) {
                t.explode(this, i, c);
                return;
            }
            cannonball.move(i, c, this);

        }
    }
}
