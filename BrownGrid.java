import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class BrownGrid extends JPanel {
    private static final int GRID_SIZE = 5; // Number of rows and columns
    private static final int SQUARE_SIZE = 50; // Size of each square in pixels
    private final Map<Point, Boolean> redSquares = new HashMap<>(); // Tracks which squares are red
    private final Image eggImage;

    public BrownGrid() {
        setPreferredSize(new Dimension(GRID_SIZE * SQUARE_SIZE, GRID_SIZE * SQUARE_SIZE));

        // Load the egg image
        eggImage = new ImageIcon("path/to/egg.png").getImage();

        addMouseListener(new MouseAdapter() { 
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = e.getX() / SQUARE_SIZE;
                int row = e.getY() / SQUARE_SIZE;
                Point point = new Point(col, row);
                if (!redSquares.containsKey(point)) {
                    redSquares.put(point, true);
                    repaint();
                    Timer timer = new Timer(3000, evt -> {
                        redSquares.remove(point);
                        repaint();
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                Point point = new Point(col, row);
                if (redSquares.getOrDefault(point, false)) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(new Color(139, 69, 19)); // Set the color to brown
                }
                int x = col * SQUARE_SIZE;
                int y = row * SQUARE_SIZE;
                g.fillRect(x, y, SQUARE_SIZE, SQUARE_SIZE);
                g.setColor(Color.BLACK); // Set the color to black for the grid lines
                g.drawRect(x, y, SQUARE_SIZE, SQUARE_SIZE); // Draw the grid lines
                
                // Draw the egg image
                g.drawImage(eggImage, x, y, SQUARE_SIZE, SQUARE_SIZE, this);
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Brown Grid");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new BrownGrid());
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);
    }
}
