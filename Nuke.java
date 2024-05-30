/**
 * @author Justin Ji
 * @version 1.0
 * 
 * Represents a nuke on the board, to make 
 * the implementation of the dropNuke() method 
 * simpler.
 */
public class Nuke extends Cannonball {
    public Nuke(int row, int col, Ground grid) {
        super(row, col, grid);
    }

    public void move(int r, int c, Ground grid) {
        grid.setItem(getRow(), getCol(), 1);
        setRow(r);
        setCol(c);
        grid.setItem(r, c, 6);
    }
}
