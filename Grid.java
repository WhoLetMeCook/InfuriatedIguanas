/**
 * @author I Chen Chou
 * @version 5/9/24
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class Grid extends JPanel {
    private JFrame f;
    private Component[][] objects;
    private int row, startRow, col;
    /**
     * Creates a grid
     * sr must be > 0 and < r
     * @param r  # rows
     * @param sr starting row where ground pops up
     * @param c # cols
     * @param sqSize size of a square in the grid
     */
    public Grid(int r, int sr, int c, int sqSize) {
        row = r;
        startRow = sr;
        col = c;
        setLayout(new GridLayout(r, c));
        f = new JFrame("Infuriated Iguanas");
        f.setSize(r * sqSize, c * sqSize);
        objects = new Component[r][c];
        f.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // why is there so much math
                int xc = e.getX(), yc = e.getY();
                // add cannonball?
                xc /= sqSize;
                yc /= sqSize;
                add(new Cannonball(r, c), xc, yc); // cannonball needs 3rd comp 
            }
        });
    }
    public Component add(Component comp, int r, int c) {
        if (objects[r][c] != null) {
            // MAYBE ADD MESSAGE TO PLAYER THAT AN OBJECT EXISTS AT THIS LOCATION SO CAN'T ADD
            return comp;
        }
        objects[r][c] = comp;
        super.add(comp, r, c);
        return comp;
    }

    /**
     * returns the item at r, c
     * @return an int 0-3
     */
    public Component getItem(int r, int c) {
        return objects[r][c];
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
}