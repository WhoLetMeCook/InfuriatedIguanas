/**
 * @author Vincent Qin
 * @version 5/8/24
 * Skeleton for a board class to hold all your essentials
 */
public abstract class Board {
    private int row;
    private int col;
    private int[][] board;

    /**
     * Constructs a board with length r and width c
     * @param r num rows
     * @param c num cols
     */
    public Board(int r, int c) {
        row = r;
        col = c;
        board = new int[row][col];
    }

    /**
     * returns the row count
     * @return the row count
     */
    public int getRow() {
        return row;
    }

    /**
     * returns the col count
     * @return the col count
     */
    public int getCol() {
        return col;
    }

    /**
     * returns item at r, c
     * @param r row to check
     * @param c col to check
     * @return the item at r, c
     */
    public abstract int getItem(int r, int c);
}
