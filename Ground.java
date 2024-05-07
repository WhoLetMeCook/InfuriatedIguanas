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

    public void setItem(int r, int c, int v) {
        grid[r][c] = v;
    }

    public int countItem(int item) {
        int ans = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == item) {
                    ans++;
                }
            }
        }
        return ans;
    }
}
