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

    /**
     * @param r the row of the new location
     * @param c the col of the new location
     * @param grid the ground board we are working with
     */
    public void move(int r, int c, Ground grid) {
        grid.setItem(getRow(), getCol(), 1);
        setRow(r);
        setCol(c);
        grid.setItem(r, c, 6);
    }
}
