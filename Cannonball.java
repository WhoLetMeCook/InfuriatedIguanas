
/**
 * @author Vincent Qin
 * @author 5/7/24
 * Represents the ammunition of the player to destroy the eggs (GRR)
 */
import java.awt.*;
public class Cannonball extends Component implements Actor {
    private int row;
    private int col;
    private int durability = 50; //arbitrary

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
     * returns the row that the cannonball is on
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * returns the col that the cannonball is on
     * @return the col
     */
    public int getCol() {
        return col;
    }

    /**
     * returns the durability of the cannonball
     * @return durability
     */
    public int getDurability() {
        return durability;
    }

    /**
     * decreases durability by amount
     * @param amount to decrease durability by
     */
    public void damage(int amount) {
        durability -= amount;
    }

    /**
     * moves the cannonball on the grid, leaves air in its trail
     */
    public void move(int r, int c, Ground grid) {
        grid.setItem(row, col, 1);
        row = r;
        col = c;
        grid.setItem(row, col, 4);
    }

    /**
     * I'M DOING THIS FOR A REASON
     */
    public void drop() {
        //
    }

    public void display() {
        //
    }
}
