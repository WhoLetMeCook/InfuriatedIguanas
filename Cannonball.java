/**
 * @author Vincent Qin
 * @version 1.0
 */
import java.awt.*;
public class Cannonball extends Component implements Actor {
    private int row;
    private int col;
    private int durability = 1;

    /**
     * Constructs a cannonball with intial position (most likely will be 0, 0 at the cannon)
     * @param r row of init position
     * @param c col of init position
     */
    public Cannonball(int r, int c, Ground grid) {
        row = r;
        col = c;
        grid.setItem(r, c, 4);
    }

    /**
     * @return the row the object is at
     */
    public int getRow() {
        return row;
    }

    /**
     * @return the column the object is at
     */
    public int getCol() {
        return col;
    }

    public void setRow(int nr) {
        row = nr;
    }

    public void setCol(int nc) {
        col = nc;
    }

    /**
     * @return durability of the cannonball
     */
    public int getDurability() {
        return durability;
    }

    /**
     * @param amount to decrease durability by
     */
    public void damage(int amount) {
        int dx = -1;
        if (amount == 0) dx = 1;
        else if (amount == 1) dx = 0;
        else if (amount == 2) dx = durability;
        durability -= dx;
    }

    /**
     * Moves the cannonball to (r, c)
     * @param r the row
     * @param c the column
     * @param grid the grid we are using
     */
    public void move(int r, int c, Ground grid) {
        grid.setItem(row, col, 1);
        row = r;
        col = c;
        grid.setItem(row, col, 4);
    }

    /**
     * Removes the cannonball from the grid.
     */
    public void remove(Ground grid) {
        grid.setItem(row, col, 1);
    }
}