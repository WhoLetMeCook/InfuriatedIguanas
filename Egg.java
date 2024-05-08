/**
 * @author Vincent Qin
 * @versiosn 5/7/24
 * Represents an egg class (the target of the player)
 */
public class Egg implements Actor{
    private int row;
    private int col;

    /**
     * constructs an egg class with its position on the grid
     * @param r row on grid
     * @param c col on grid
     */
    public Egg(int r, int c, Ground grid) {
        row = r;
        col = c;
        grid.setItem(r, c, 3);
    }

    /**
     * returns the row egg is on
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * returns the col egg is on
     * @return the col
     */
    public int getCol() {
        return col;
    }

    /**
     * moves the egg to a new spot, leaves a trail of dirt
     */
    public void move(int r, int c, Ground grid) {
        grid.setItem(row, col, 0);
        row = r;
        col = c;
        grid.setItem(r, c,  3);
    }
}
