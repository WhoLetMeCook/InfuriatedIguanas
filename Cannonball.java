/**
 * @author Vincent Qin, I Chen Chou
 * @author 5/7/24
 * Represents the ammunition of the player to destroy the eggs (GRR)
 */
import java.awt.*;
public class Cannonball extends Actor {
    private int durability = 1; //arbitrary

    /**
     * constructs a cannonball with its position on the grid
     * @param r row on grid
     * @param c col on grid
     * @param grid grid to display actor
     * @param s dilation factor of actor on display
     * @param sqSize size of square
     * @param i image of an actor
     */
    public Cannonball(int r, int c, Ground grid, Image i, int s) {
        super(r, c, grid, i, s);
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
}
