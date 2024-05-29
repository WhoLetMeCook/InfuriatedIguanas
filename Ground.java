import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Ground extends JPanel implements KeyListener {
    private final int[][] grid;
    private final int row, sr, col;
    private final JFrame f;
    private final Component[][] objects;
    private final TunnelMaker t;
    private final Image iguana, egg, ball, explosion;
    private final int sqSize;
    private final int EGG_MULT = 10;
    private final int BALL_MULT = 12;
    private final int IGUANA_MULT = 60;
    private boolean droppingBall = false;
    private final Image scaledIguana, scaledEgg, scaledBall, scaledExplosion;
    int dropped = 0;

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

        scaledIguana = iguana.getScaledInstance(sqSize * IGUANA_MULT, sqSize * IGUANA_MULT, Image.SCALE_SMOOTH);
        scaledEgg = egg.getScaledInstance(sqSize * EGG_MULT, sqSize * EGG_MULT, Image.SCALE_SMOOTH);
        scaledBall = ball.getScaledInstance(sqSize * BALL_MULT, sqSize * BALL_MULT, Image.SCALE_SMOOTH);
        scaledExplosion = explosion.getScaledInstance(sqSize * BALL_MULT * 2, sqSize * BALL_MULT * 2, Image.SCALE_SMOOTH);

        setLayout(null);
        f = new JFrame("Infuriated Iguanas");
        f.setSize(c * sqSize, r * sqSize);
        f.add(this);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);

        f.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                droppingBall = true;
                int xc = e.getX() / sqSize;
                if (xc >= 0) {
                    dropBall(xc);
                }
            }

            public void mouseReleased(MouseEvent e) {
                droppingBall = false;
            }
        });

        f.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (droppingBall) {
                    int xc = e.getX() / sqSize;
                    if (xc >= 0) {
                        dropBall(xc);
                    }
                }
            }
        });

        f.addKeyListener(this);
        f.setFocusable(true);
        f.requestFocusInWindow();
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

        g.setColor(new Color(139, 69, 19)); // brown
        for (int i = sr; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (grid[i][j] == 0 || grid[i][j] == 3 || grid[i][j] == 4) {
                    g.fillRect(j * sqSize, i * sqSize, sqSize, sqSize);
                }
            }
        }

        g.drawImage(scaledIguana, 0, (sr - IGUANA_MULT) * sqSize, this);

        for (int i = sr; i < row; i++) {
            for (int j = 0; j < col; j++) {
                switch (grid[i][j]) {
                    case 3 -> g.drawImage(scaledEgg, (j - EGG_MULT / 2) * sqSize, (i - EGG_MULT / 2) * sqSize, this);
                    case 4 -> g.drawImage(scaledBall, (j - BALL_MULT / 2) * sqSize, (i - BALL_MULT / 2) * sqSize, this);
                    case 5 -> g.drawImage(scaledExplosion, j * sqSize - BALL_MULT, i * sqSize, this);
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
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                grid[i][j] = 0;  // Initialize grid with 0 (clear cell)
            }
        }

        int tunnels = (row + col) / 10;
        for (int i = 0; i < tunnels; i++) {
            t.generateTunnel(this);
        }

        for (int i = 0; i < 100; i++) {
            placeEgg();
        }
    }

    public void startGame() {
        reset();
        repaint();
    }

    public void endGame() {
        displayMessage(new JTextField("Game Over! " + dropped + " cannonballs used."));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            return;
        }
        f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
    }

    private void placeEgg() {
        Random r = new Random();
        int eggRow, eggCol;
        do {
            eggRow = sr + r.nextInt(row - sr - EGG_MULT * 3);
            eggCol = r.nextInt(col);
        } while (grid[eggRow][eggCol] != 0);
        grid[eggRow][eggCol] = 3;  // Place egg
    }

    public void setItem(int r, int c, int v) {
        grid[r][c] = v;
    }

    public int getItem(int r, int c) {
        return grid[r][c];
    }

    public int countItem(int item) {
        int count = 0;
        for (int[] row : grid) {
            for (int cell : row) {
                if (cell == item) {
                    count++;
                }
            }
        }
        return count;
    }

    public void displayGrid() {
        for (int[] row : grid) {
            for (int cell : row) {
                System.out.print(cell + " ");
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

    public void displayMessage(JTextField message) {
        message.setBounds(150, 100, 200, 30);
        this.add(message);
        this.revalidate();
        this.repaint();
    }
    
    public void removeMessage(JTextField message) {
        this.remove(message);
        this.revalidate();
        this.repaint();
    }

    public void airstrike() {
        new Thread(() -> {
            JTextField message = new JTextField("Airstrike Inbound!");
            displayMessage(message);
    
            for (int i = 0; i < col; i++) {
                dropBall(i);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    return;
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return;
            }

            removeMessage(message);
        }).start();
    }

    public void dropBall(int c) {
        new Thread(() -> {
            Cannonball cannonball = new Cannonball(sr - BALL_MULT, c, this);
            JTextField message = new JTextField("Airstrike Inbound!");
            displayMessage(message);
            for (int i = sr - BALL_MULT; i < row - (BALL_MULT / 2); i++) {
                repaint();
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    return;
                }

                if (i >= sr && c >= 0 && c + 1 < col) {
                    cannonball.damage(grid[i][c + 1]);
                }

                cannonball.move(i, c, this);
                if (cannonball.getDurability() <= 0 || cannonball.getRow() >= row - 30) {
                    t.explode(this, i, c, 7);

                    if ((i - BALL_MULT + 2 >= 0 && i - BALL_MULT + 2 < row) &&
                        (c - BALL_MULT / 2 - 1 >= 0 && c - BALL_MULT / 2 - 1 < col)) {
                        displayExplosion(i - BALL_MULT + 2, c - BALL_MULT / 2 - 1);
                    }
                    return;
                }
                cannonball.move(i + 1, c, this);
            }

            if ((row - BALL_MULT >= 0 && row - BALL_MULT < this.row) && (c >= 0 && c < this.col)) {
                t.explode(this, row - BALL_MULT, c, 7);
                displayExplosion(row - BALL_MULT, c);
            }
            removeMessage(message);
        }).start();
        ++dropped;
        if (countItem(3) == 0) {
            endGame();
        }
    }

    private void displayExplosion(int row, int col) {
        new Thread(() -> {
            if (row >= 0 && row < this.row && col >= 0 && col < this.col) {
                grid[row][col] = 5;
                repaint();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    return;
                }
                grid[row][col] = 1;
                repaint();
            }
        }).start();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            airstrike();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // No action needed for key release
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // No action needed for key typing
    }
}
