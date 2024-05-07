import java.util.*;
import TunnelMaker;

public class Ground extends Board{
    private int[][] grid;

    public Ground(int r, int c) {
        super(r, c);
    }

    public int getItem(int r, int c) {
        return grid[r][c];
    }

    public void reset() {
        Arrays.fill(grid, 0);
        TunnelMaker t = new TunnelMaker();
        for (int i = 0; i < 5; i++) { //num tunnels is arbitrary rn
            t.generateTunnel(grid);
        }
    }
}
