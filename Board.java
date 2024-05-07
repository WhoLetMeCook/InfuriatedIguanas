public abstract class Board {
    private int row;
    private int col;
    private int[][] board;

    public Board(int r, int c) {
        row = r;
        col = c;
        board = new int[row][col];
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public abstract int getItem(int r, int c);
}
