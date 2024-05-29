/**
 * @author I Chen Chou
 * @version 5/7/24
 * Represents an iguana class (the target of the player)
 */
import java.awt.*;
public class Iguana extends Actor {

    /**
     * constructs an iguana class with its position on the grid
     * @param r row on grid
     * @param c col on grid
     * @param grid grid to display egg
     * @param s dilation factor of egg on display
     * @param sqSize size of square
     * @param i image of an egg
     */
    public Iguana(int r, int c, Ground grid, Image i, int s) {
        super(r, c, grid, i, s);
    }

}
