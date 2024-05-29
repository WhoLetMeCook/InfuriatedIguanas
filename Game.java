import javax.swing.*;
import java.awt.event.*;

public class Game extends JPanel {
    private JFrame frame;
    private Ground grid;
    final int rows = 400, cols = 400, startRow = 80, sqSize = 3;
    private volatile boolean skipTutorialClicked = false;

    /**
     * Displays a message onto the JPanel
     * @param message the message we want to place
     */
    public void displayMessage(JTextField message) {
        message.setBounds(600, 500, 300, 50);
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
     * Sets up the loading screen, to allow the player to launch the game.
     */
    public void setLoadingScreen() {
        frame = new JFrame("Infuriated Iguanas");
        frame.setSize(1000, 1000);
        frame.add(this);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        String[] messages = new String[]{
            "Hi there!", "Infuriated Iguanas is a game where you are an iguana trying to destroy eggs.",
            "To launch a standard in game bomb, click the screen.", "To launch an airstrike, press 'A' on your keyboard.",
            "To launch a thermonuclear explosion, press 'N'."
        };

        JButton skipTutorial = new JButton("Skip Tutorial");
        skipTutorial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                skipTutorialClicked = true;
                synchronized (Game.this) {
                    Game.this.notify();
                }
            }
        });
        this.add(skipTutorial);
        this.revalidate();
        this.repaint();

        for (String cur : messages) {
            if (skipTutorialClicked) break;

            JTextField msg = new JTextField(cur);
            displayMessage(msg);

            synchronized (this) {
                try {
                    this.wait(3000);
                } catch (InterruptedException e) {
                    return;
                }
            }

            removeMessage(msg);
        }

        this.remove(skipTutorial);
        this.revalidate();
        this.repaint();

        JButton start = new JButton("Start Game");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grid = new Ground(rows, cols, startRow, sqSize);
                grid.startGame();
            }
        });
        this.add(start);
        this.revalidate();
        this.repaint();
    }

    public static void main(String[] args) {
        Game curGame = new Game();
        curGame.setLoadingScreen();
    }
}
