/**
 * @author I Chen Chou
 * @version 5/9/24
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class Grid extends JPanel {

    private MouseListener mouse;
    private JFrame f;
    public Grid(int r, int c) {
        setLayout(new GridLayout(r, c));
        f = new JFrame("Infuriated Iguanas");
        f.setSize(r * 10, c * 10);
    }

    public Component add(Component comp, int r, int c) {
        super.add(comp, r, c);
        return comp;
    }
}