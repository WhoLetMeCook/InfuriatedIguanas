/**
 * @author I Chen Chou
 * @version 5/9/24
 */
public class Iguana {
    
    private int row;
    private int col;

    /**
     * Basic iguana constructor
     * @param r row
     * @param c col
     */
    public Iguana(int r, int c) {
        row = r;
        col = c;
    }

    /**
     * @return row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return col
     */
    public int getCol() {
        return col;
    }

    /**
     * Moves the iguana at specified coordinate
     * @param r row
     * @param c col
     */
    public void move(int r, int c) {
        row = r;
        col = c;
    }

    /**
     * Drops a cannonball at specified column
     * @param c col
     */
    public void dropCannonball(int loc) {
        Cannonball drop = new Cannonball(row, loc, new Ground(row, loc));
        // FIGURE OUT MATH IG
    }
}
