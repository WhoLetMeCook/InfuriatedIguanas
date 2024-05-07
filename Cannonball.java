public class Cannonball implements Actor{
    private int row;
    private int col;
    private int durability = 3000; //arbitrary

    public Cannonball(int r, int c) {
        row = r;
        col = c;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getDurability() {
        return durability;
    }

    public void damage(int amount) {
        durability -= amount;
    }

    public void move(int r, int c) {
        row = r;
        col = c;
    }
}
