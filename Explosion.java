/**
 * @author I Chen Chou
 * @version 5/7/24
 * Represents an explosive (the target of the player)
 */
import java.awt.*;
public class Explosion extends Actor {

    /**
     * constructs an egg class with its position on the grid
     * @param r row on grid
     * @param c col on grid
     * @param grid grid to display egg
     * @param s dilation factor of egg on display
     * @param sqSize size of square
     * @param i image of an egg
     */
    public Explosion(int r, int c, Ground grid, Image i, int s) {
        super(r, c, grid, i, s);
    }

}
