import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Ground extends JPanel {
    private int[][] grid;
    private int row, sr, col;
    private JFrame f;
    private Component[][] objects;
    private TunnelMaker t;
    private Image iguana, egg, ball, explosion;
    private int sqSize;
    private final int EGG_MULT = 60;
    private final int BALL_MULT = 12;
    private final int IGUANA_MULT = 15;

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
        explosion = new ImageIcon("z_explosion.png").getImage();

        setLayout(null);
        f = new JFrame("Infuriated Iguanas");
        f.setSize(c * sqSize, r * sqSize);
        f.add(this);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int xc = (e.getX() / sqSize) - (BALL_MULT / 2);
                int yc = (e.getY() / sqSize) - (BALL_MULT / 2);
                if (xc >= 0 && yc >= 0) {
                    // add more bounds checking later!
                    dropBall(xc);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < sr; i++) {
            for (int j = 0; j < col; j++) {
                g.setColor(Color.BLUE);
                g.fillRect(j * sqSize, i * sqSize, sqSize, sqSize);
            }
        }
        for (int i = sr; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (grid[i][j] == 0 || grid[i][j] == 3 || grid[i][j] == 4) {
                    g.setColor(new Color(139, 69, 19)); // brown
                    g.fillRect(j * sqSize, i * sqSize, sqSize, sqSize);
                }
            }
        }
        g.drawImage(iguana, 0, (sr - IGUANA_MULT) * sqSize, sqSize * IGUANA_MULT, sqSize * IGUANA_MULT, this);
        for (int i = sr; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (grid[i][j] == 3) {  // Egg
                    g.drawImage(egg, j * sqSize, i * sqSize, sqSize * EGG_MULT, sqSize * EGG_MULT, this);
                } else if (grid[i][j] == 4) {  // Cannonball
                    g.drawImage(ball, j * sqSize, i * sqSize, sqSize * BALL_MULT, sqSize * BALL_MULT, this);
                } else if (grid[i][j] == 5) {  // Explosion
                    g.drawImage(explosion, j * sqSize - BALL_MULT, i * sqSize, sqSize * BALL_MULT * 2, sqSize * BALL_MULT * 2, this);
                }
            }
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void reset() {
        Random r = new Random();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                double item = r.nextDouble();
                if (item < 0.995) {
                    grid[i][j] = 0;
                } else {
                    grid[i][j] = 2;
                }
                grid[i][j] = 0;
            }
        }
        for (int i = 0; i < (int) (row + col) / 10; i++) {
            t.generateTunnel(this);
        }
        for (int i = 0; i < 6; i++) {
            t.placeEgg(this);
        }
        repaint();
    }

    public void setItem(int r, int c, int v) {
        grid[r][c] = v;
    }

    public int getItem(int r, int c) {
        return grid[r][c];
    }

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

    public Component getItemComponent(int r, int c) {
        return objects[r][c];
    }

    public void dropBall(int c) {
        new Thread(() -> {
            Cannonball cannonball = new Cannonball(sr, c, this);
            for (int i = sr; i < row - BALL_MULT; i++) {
                repaint();
                try {
                    Thread.sleep(10); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cannonball.damage(grid[i][c + 1]);
                cannonball.move(i, c, this);
                System.out.println(cannonball.getDurability()); 
                if (cannonball.getDurability() <= 0 || cannonball.getRow() >= row) {
                    t.explode(this, i, c + (BALL_MULT / 2), 12);
                    displayExplosion(i, c + (BALL_MULT / 2));
                    return;
                }
                cannonball.move(i + 1, c, this);
            }
            t.explode(this, row - BALL_MULT, c + (BALL_MULT / 2), 12);
            displayExplosion(row - BALL_MULT, c + (BALL_MULT / 2));
        }).start();
    }

    private void displayExplosion(int row, int col) {
        new Thread(() -> {
            grid[row][col] = 5; 
            repaint();
            try {
                Thread.sleep(1000);  
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            grid[row][col] = 1; 
            repaint();
        }).start();
    }
}
