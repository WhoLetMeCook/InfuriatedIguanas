import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Represents the game ground where the game elements interact.
 */
public class Ground extends JPanel implements KeyListener {
    private Random r = new Random();
    private final int[][] grid;
    private final int row, sr, col;
    private final JFrame f;
    private final TunnelMaker t;
    private final Image iguana, egg, ball, explosion, nuke;
    private BufferedImage dirt;
    private final int sqSize;
    private final int EGG_MULT = 10;
    private final int BALL_MULT = 12;
    private final int IGUANA_MULT = 60;
    private final int NUKE_MULT = 20;
    private boolean droppingBall = false;
    private final Image scaledIguana, scaledEgg, scaledBall, scaledExplosion, scaledNuke;
    private int droppedBalls = 0, droppedNukes = 0, airstrikes = 0;
    private JTextField ballMessage, nukeMessage, airstrikeMessage;
    private final String gameMode;
    private int remainingShots;
    private Timer timer;

    /**
     * The constructor for the class.
     * @param r # of rows
     * @param c # of columns
     * @param startRow index of row where the "ground" starts
     * @param sqSize size of each square
     * @param gameMode selected game mode
     */
    public Ground(int r, int c, int startRow, int sqSize, String gameMode) {
        row = r;
        sr = startRow;
        col = c;
        this.sqSize = sqSize;
        this.gameMode = gameMode;
        grid = new int[r][c];
        t = new TunnelMaker((row * col) / 7, 0.425);

        iguana = new ImageIcon("z_infuriated_iguana.png").getImage();
        egg = new ImageIcon("z_snake_egg.png").getImage();
        ball = new ImageIcon("z_cannonball.png").getImage();
        explosion = new ImageIcon("z_explosion.png").getImage();
        nuke = new ImageIcon("z_nuke.png").getImage();
        dirt = null;
        try {
            dirt = ImageIO.read(new File("z_dirt.png"));
        } catch (Exception e) {
            System.out.println("Image not found!");
        }

        ballMessage = new JTextField("Dropped Balls: 0");
        nukeMessage = new JTextField("Nukes Used: 0");
        airstrikeMessage = new JTextField("Airstrikes: 0");

        scaledIguana = iguana.getScaledInstance(sqSize * IGUANA_MULT, sqSize * IGUANA_MULT, Image.SCALE_SMOOTH);
        scaledEgg = egg.getScaledInstance(sqSize * EGG_MULT, sqSize * EGG_MULT, Image.SCALE_SMOOTH);
        scaledBall = ball.getScaledInstance(sqSize * BALL_MULT, sqSize * BALL_MULT, Image.SCALE_SMOOTH);
        scaledExplosion = explosion.getScaledInstance(sqSize * BALL_MULT * 2, sqSize * BALL_MULT * 2, Image.SCALE_SMOOTH);
        scaledNuke = nuke.getScaledInstance(sqSize * NUKE_MULT, sqSize * NUKE_MULT, Image.SCALE_SMOOTH);

        setLayout(null);
        f = new JFrame("Infuriated Iguanas");
        f.setSize(c * sqSize, r * sqSize);
        f.add(this);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);

        f.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (gameMode.equals("Limited Shots")) {
                    if (remainingShots > 0) {
                        droppingBall = true;
                        int xc = e.getX() / sqSize;
                        if (xc >= 0) {
                            dropBall(xc, true);
                        }
                        remainingShots--;
                    } else {
                        endGame();
                    }
                } else {
                    droppingBall = true;
                    int xc = e.getX() / sqSize;
                    if (xc >= 0) {
                        dropBall(xc, true);
                    }
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
                        dropBall(xc, true);
                    }
                }
            }
        });

        f.addKeyListener(this);
        f.setFocusable(true);
        f.requestFocusInWindow();

        if (gameMode.equals("Limited Shots")) {
            remainingShots = 50; // Example value for limited shots
        } else {
            int timeLimit = 60 * 1000; // 1 minute time limit
            timer = new Timer(timeLimit, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    endGame();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }

        startGame();
    }


    /**
     * "Getter" method.
     * @return the # of rows
     */
    public int getRow() {
        return row;
    }

    /**
     * "Getter" method.
     * @return the # of columns
     */
    public int getCol() {
        return col;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (countItem(3) == 0) {
            endGame();
        }

        super.paintComponent(g);

        for (int i = 0; i < sr; i++) {
            for (int j = 0; j < col; j++) {
                g.setColor(new Color(118, 224, 255));
                g.fillRect(j * sqSize, i * sqSize, sqSize, sqSize);
            }
        }
        
        for (int i = sr; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (grid[i][j] == 0 || grid[i][j] == 3 || grid[i][j] == 4) {
                    int rgb = dirt.getRGB(i % 16, j % 16);
                    g.setColor(new Color(rgb));
                    g.fillRect(j * sqSize, i * sqSize, sqSize, sqSize);
                } else if (grid[i][j] == 2) {
                    g.setColor(Color.GRAY);
                    g.fillRect(j * sqSize, i * sqSize, sqSize, sqSize);
                }
            }
        }

        g.drawImage(scaledIguana, 0, (sr - IGUANA_MULT) * sqSize, this);

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                switch (grid[i][j]) {
                    case 3 -> g.drawImage(scaledEgg, (j - EGG_MULT / 2) * sqSize, (i - EGG_MULT / 2) * sqSize, this);
                    case 4 -> g.drawImage(scaledBall, (j - BALL_MULT / 2) * sqSize, (i - BALL_MULT / 2) * sqSize, this);
                    case 5 -> g.drawImage(scaledExplosion, j * sqSize - BALL_MULT, i * sqSize, this);
                    case 6 -> g.drawImage(scaledNuke, (j - NUKE_MULT / 2) * sqSize, (i - NUKE_MULT / 2) * sqSize, this);
                }
            }
        }

        removeMessage(airstrikeMessage);
        removeMessage(nukeMessage);
        removeMessage(ballMessage);

        airstrikeMessage = new JTextField("Airstrikes: " + airstrikes);
        nukeMessage = new JTextField("Nukes Used: " + droppedNukes);
        ballMessage = new JTextField("Dropped Cannonballs: " + droppedBalls);

        displayMessage(950, 50, airstrikeMessage);
        displayMessage(950, 75, nukeMessage);
        displayMessage(950, 100, ballMessage);
    }

    /**
     * Resets the grid for the next game.
     */
    public void reset() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                grid[i][j] = 0;
            }
        }

        int tunnels = (row + col) / 10;
        for (int i = 0; i < tunnels; i++) {
            t.generateTunnel(this, (i % 2 == 0 ? 1 : 2));
        }

        for (int i = 0; i < 100; i++) {
            placeEgg();
        }
    }

    /**
     * Starts the game by resetting the grid.
     */
    public void startGame() {
        reset();
        repaint();
    }

/**
     * Ends the game and shows a game over message.
     */
    public void endGame() {
        if (timer != null) {
            timer.stop();
        }
        JOptionPane.showMessageDialog(f, "Game Over!");
        f.dispose();
    }

    /**
     * Places an egg at a random, unoccupied position.
     */
    private void placeEgg() {
        Random r = new Random();
        int eggRow = -1, eggCol = -1;
        do {
            eggRow = sr + EGG_MULT + r.nextInt(row - sr - EGG_MULT * 4);
            eggCol = r.nextInt(col);
        } while (grid[eggRow][eggCol] != 0);
        grid[eggRow][eggCol] = 3;  // Place egg
    }

    /**
     * Sets the item at grid[r][c] to v. Later,
     * the grid will be re-rendered to correctly display this change.
     * @param r row indice
     * @param c column indice
     * @param v value
     */
    public void setItem(int r, int c, int v) {
        grid[r][c] = v;
    }

    /**
     * @param r row indice
     * @param c column indice
     * @return value at grid[r][c]
     */
    public int getItem(int r, int c) {
        return grid[r][c];
    }

    /**
     * @param item the item we are looking for, in terms of # of occurences
     * @return number of times this item shows up.
     */
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

    /**
     * Testing method. Used to display the internal grid.
     */
    public void displayGrid() {
        for (int[] row : grid) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    /**
     * Displays a message onto the JPanel
     * @param message the message we want to place
     */
    public void displayMessage(JTextField message) {
        displayMessage(150, 100, message);
    }

    /**
     * 
     * @param message
     */

    public void displayMessage(int xLoc, int yLoc, JTextField message) {
        message.setBounds(xLoc, yLoc, 200, 30);
        this.add(message);
        this.revalidate();
        this.repaint();
    }
    
    /**
     * Removes the message given.
     * @param message item we want to remove
     */
    public void removeMessage(JTextField message) {
        this.remove(message);
        this.revalidate();
        this.repaint();
    }

    /**
     * Plays the sound indicated by the file name.
     * @param fileName location of file
     */
    Clip playSound(String fileName) {
        final float volume = 0.3f;
        try {
            File file = new File(fileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);

            clip.start();
            return clip;
        } catch (Exception e) {
            System.out.println("Sound not found!");
            return null;
        }
    }

    /**
     * Simulates a series of explosions on the surface of the ground.
     */
    public void airstrike() {
        new Thread(() -> {
            JTextField message = new JTextField("Airstrike Inbound!");
            displayMessage(message);
            Clip ref = playSound("z_sfx_siren.wav");
    
            for (int i = 0; i < col; i += 2) {
                if (r.nextDouble() > 0.25) dropBall(i, (i % 20 == 0));
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
            ref.stop();
            ++airstrikes;
        }).start();
    }

    public void dropNuke() {
        new Thread(() -> {
            final int c = col / 2;
            Nuke nuke = new Nuke(sr - BALL_MULT, c, this);
            JTextField message = new JTextField("Prepare For Fallout!");
            displayMessage(message);
            for (int i = 1; i < row - BALL_MULT / 4; i++) {
                repaint();
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    return;
                }

                if (i >= sr && c >= 0 && c + 1 < col) {
                    nuke.damage(grid[i][c + 1]);
                }

                nuke.move(i, c, this);
                if (nuke.getDurability() <= 0 || nuke.getRow() >= row - 30) {
                    t.explode(this, i, c, 100);
                    playSound("z_sfx_explosion.wav");
                    if ((i - BALL_MULT + 2 >= 0 && i - BALL_MULT + 2 < row) &&
                        (c - BALL_MULT / 2 - 1 >= 0 && c - BALL_MULT / 2 - 1 < col)) {
                        displayExplosion(i - BALL_MULT + 2, c - BALL_MULT / 2 - 1);
                    }
                    break;
                }
                nuke.move(i + 1, c, this);
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                return;
            }
            removeMessage(message);
        }).start();
        ++droppedNukes;
    }

    /**
     * Drops a cannonball onto the grid, wit this object detonating later.
     * @param c the column indice of where we drop it (the row is fixed)
     */
    public void dropBall(int c, boolean play) {
        new Thread(() -> {
            Cannonball cannonball = new Cannonball(sr - BALL_MULT, c, this);
            JTextField message = new JTextField("Fire In The Hole!");
            displayMessage(message);
            for (int i = sr - BALL_MULT; i < row - BALL_MULT / 4; i++) {
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
                    t.explode(this, i, c, r.nextInt(4)+3);
                    if (play) playSound("z_sfx_explosion.wav");
                    if ((i - BALL_MULT + 2 >= 0 && i - BALL_MULT + 2 < row) &&
                        (c - BALL_MULT / 2 - 1 >= 0 && c - BALL_MULT / 2 - 1 < col)) {
                        displayExplosion(i - BALL_MULT + 2, c - BALL_MULT / 2 - 1);
                    }
                    break;
                }
                cannonball.move(i + 1, c, this);
            }

            /*
            if ((row - BALL_MULT >= 0 && row - BALL_MULT < this.row) && (c >= 0 && c < this.col)) {
                t.explode(this, row - BALL_MULT, c, 7);
                displayExplosion(row - BALL_MULT, c);
            }
            */
            
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                return;
            }
            removeMessage(message);
        }).start();
        ++droppedBalls;
    }

    /**
     * Renders an explosion at location (row, col)
     * @param row the given row
     * @param col the given column
     */
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
        } else if (e.getKeyCode() == KeyEvent.VK_N) {
            dropNuke();
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