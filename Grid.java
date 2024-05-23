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
    public Grid(int r, int c, int rsize, int csize) {
        setLayout(new GridLayout(r, c));
        f = new JFrame("Infuriated Iguanas");
        f.setSize(r * rsize, c * csize);
        objects = new Component[r][c];
        f.addMouseListener(new MouseAdapter() {
            // if we decide to draw a line for aim we need more complex mouse methods
            public void mouseClicked() {
                // why is there so much math
                int xc = getX(), yc = getY();
                // add cannonball?
                //
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
}