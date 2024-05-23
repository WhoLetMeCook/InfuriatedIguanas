import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Vincent Qin, I Chen Chou
 * @version 5/23/24
 * crazy ground class frfr
 */
public class Ground extends JPanel {
    private int[][] grid;
    private int row, sr, col;
    private JFrame f;
    private Component[][] objects;
    private TunnelMaker t;
    private Image iguana, egg, ball;
    /**
     * instantiates a 2d array of numbers from 0-4
     * 0 represents dirt, 1 represents air, 2 stone, 3 eggs, and 4 cannonballs
     * @param r number of rows
     * @oaram startRow row where ground appears
     * @param c number of columns
     * @param gridr starting row where ground pops up
     * @param sqSize size of a square in the grid
     */
    public Ground(int r, int c, int startRow, int sqSize) {
        row = r;
        startRow = sr;
        col = c;
        grid = new int[r][c];
        objects = new Component[r][c];
        t = new TunnelMaker((row * col) / 7, 0.45);

        iguana = new ImageIcon("z_infuriated_iguana.png").getImage();
        egg = new ImageIcon("z_snake_egg.png").getImage();
        ball = new ImageIcon("z_cannonball.png").getImage();

        setLayout(new GridLayout(r, c));
        f = new JFrame("Infuriated Iguanas");
        f.setSize(r * sqSize, c * sqSize);
        f.add(this);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int xc = e.getX() / sqSize;
                int yc = e.getY() / sqSize;
                add(new Cannonball(0, 0, Ground.this), yc, xc); // Adjust as needed
            }
        });
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

    /**
     * remakes the array using tunnelmaker to generate random tunnels, will add eggs later
     */
    public void reset() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                grid[i][j] = 0;
            }
        }
        
        for (int i = 0; i < (int) (row + col) / 10; i++) { // num tunnels is arbitrary rn
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

    public int getItem(int r, int c) { return grid[r][c]; }

    /**
     * counts a specific item, will usually be for eggs, sometimes for cannonballs
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
    public void displayGrid() {
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
    public void showGrid() {
        int startRow = (int)(f.getSize().getHeight() / getRow());
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (grid[i][j] == 0) {
                    // display dirt
                } else if (grid[i][j] == 1) {
                    // air --> basically do nothing?
                } else if (grid[i][j] == 2) {
                    // stone
                } else if (grid[i][j] == 3) {
                    // egg
                } else {
                    // cannonball
                    add(new Cannonball(i + startRow, j, this), i + startRow, j);
                }
            }
        }
    }

    /**
     * adds a component to the grid at specified row and column
     * @param comp the component to add
     * @param r row index
     * @param c column index
     * @return the component added
     */
    public Component add(Component comp, int r, int c) {
        if (objects[r][c] != null) {
            // MAYBE ADD MESSAGE TO PLAYER THAT AN OBJECT EXISTS AT THIS LOCATION SO CAN'T ADD
            return comp;
        }
        objects[r][c] = comp;
        super.add(comp);
        revalidate();
        repaint();
        return comp;
    }

    /**
     * returns the item at r, c
     * @return the component at the specified location
     */
    public Component getItemComponent(int r, int c) {
        return objects[r][c];
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
