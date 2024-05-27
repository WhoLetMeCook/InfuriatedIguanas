import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Ground extends JPanel {
    private int[][] grid;
    private int row, sr, col;
    private JFrame f;
    private Component[][] objects;
    private TunnelMaker t;
    private Image iguana, egg, ball;
    private int sqSize;

    public Ground(int r, int c, int startRow, int sqSize) {
        row = r;
        sr = startRow;
        col = c;
        this.sqSize = sqSize;
        grid = new int[r][c];
        objects = new Component[r][c];
        t = new TunnelMaker((row * col) / 7, 0.425);

        iguana = new ImageIcon("z_infuriated_iguana.png").getImage();
        egg = new ImageIcon("z_snake_egg.png").getImage();
        ball = new ImageIcon("z_cannonball.png").getImage();

        setLayout(null);
        f = new JFrame("Infuriated Iguanas");
        f.setSize(c * sqSize, r * sqSize);
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
        /**
         * Could add seed generation?
         */
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = sr; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (grid[i][j] == 0) {
                    g.setColor(new Color(139, 69, 19)); // brown
                    g.fillRect(j * sqSize, i * sqSize, sqSize, sqSize);
                }
            }
        }
        for (int i = sr; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (grid[i][j] == 3) {  // Egg
                    g.drawImage(egg, j * sqSize, i * sqSize, sqSize * 15, sqSize * 10, this);
                } else if (grid[i][j] == 4) {  // Cannonball
                    g.drawImage(ball, j * sqSize, i * sqSize, sqSize, sqSize, this);
                }
            }
        }
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

    public void reset() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                grid[i][j] = 0;
            }
        }
        
        for (int i = 0; i < (int) (row + col) / 10; i++) { 
            t.generateTunnel(this);
        }
        for (int i = 0; i < 6; i++) {
            t.placeEgg(this);
        }
        repaint();  // Ensure the grid is repainted
    }

    public void setItem(int r, int c, int v) {
        grid[r][c] = v;
        repaint();  // Repaint after setting item
    }

    public int getItem(int r, int c) { return grid[r][c]; }

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

    public void displayGrid() {
        for (int i = 0; i < getRow(); i++) {
            for (int j = 0; j < getCol(); j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void showGrid() {
        repaint();
    }

    public Component add(Component comp, int r, int c) {
        if (objects[r][c] != null) {
            return comp;
        }
        objects[r][c] = comp;
        super.add(comp);
        revalidate();
        repaint();
        return comp;
    }

    public Component getItemComponent(int r, int c) {
        return objects[r][c];
    }

    public void dropBall(int c) {
        Cannonball cannonball = new Cannonball(0, c, this);
        for (int i = 0; i < row; i++) {
            cannonball.damage(grid[i][c] + 1);
            if (cannonball.getDurability() <= 0) {
                t.explode(this, i, c);
                return;
            }
            cannonball.move(i, c, this);
            repaint();
        }
    }
}