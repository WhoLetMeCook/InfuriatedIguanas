public class Egg implements Actor{
    private int row;
    private int col;
    public Egg(int r, int c) {
        row = r;
        col = c;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
