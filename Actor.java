/**
 * @author Vincent Qin, I Chen Chou
 * @version 5/7/24
 * Superclass for all actors in iguana class
 */
import java.awt.*;
import javax.swing.*;
public class Actor extends Component {
    private int row;
    private int col;
    private Ground display;
    private Image image;
    private int scale;
    private int val;
    
    /**
     * constructs an actor with its position on the grid
     * @param r row on grid
     * @param c col on grid
     * @param grid grid to display actor
     * @param s dilation factor of actor on display
     * @param sqSize size of square
     * @param i image of an actor
     */
    public Actor(int r, int c, Ground grid, Image i, int s) {
        row = r;
        col = c;
        display = grid;
        scale = s;
        image = i.getScaledInstance(grid.getSqSize() * scale, 
            grid.getSqSize() * scale, Image.SCALE_SMOOTH);;
    }
    /**
     * returns the row that the item is on
     * @return the row 
     */
    public int getRow() {
        return row;
    }

    /**
     * returns the col that the item si on
     * @return the col
     */
    public int getCol() {
        return col;
    }

    /**
     * returns dilation factor of the egg
     * @return dilation factor
     */
    public int getMult() {
        return scale;
    }

    /**
     * returns image
     * @return image
     */
    public Image getImage() {
        return image;
    }

    /**
     * draws actor
     */
    public void draw(Graphics g, int sqSize) {
        g.drawImage(image, (col - scale / 2) * sqSize, (row - scale / 2) * sqSize, display);
    }

    /**
     * moves object on GUI
     */
    public void move(int r, int c) {
        display.setItem(row, col, null);
        row = r;
        col = c;
        display.setItem(r, c, this);
    }
}
