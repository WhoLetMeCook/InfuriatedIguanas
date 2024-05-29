/**
 * @author Vincent Qin
 * @version 5/7/24
 * Skeleton for stuff to put in the ground
 */
public interface Actor {
    /**
     * returns the row that the item is on
     * @return the row 
     */
    public abstract int getRow();

    /**
     * returns the col that the item si on
     * @return the col
     */
    public abstract int getCol();

    /**
     * moves the item to another place on grid and fills its original spot in
     * @param r row
     * @param c col
     * @param grid grid to move stuff to
     */
    public abstract void move(int r, int c, Ground grid);
}
