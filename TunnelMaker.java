/**
 * @author Justin Ji, Vincent Qin
 * @version 1.0
 */

import java.util.*;

public class TunnelMaker {
    private final int MX;
    private final double PROB;
    private Ground grid;
    /**
     * The constructor.
    * @param _MX max # of cells deleted in a "bubble"
    * @param _PROB probability of propagating.
    */
    public TunnelMaker(int _MX, double _PROB, Ground _grid) {
        MX = _MX;
        PROB = _PROB;
        grid = _grid;
    }
    /**
     * Adds a hole at a random location on the grid.
     * @param grid the grid we are editing.
    */
    public void generateTunnel(int val) {
        final int n = grid.getRow(), m = grid.getCol();
        final int lowest = (int) ((2.0 / 3) * n);
        ArrayList<int[]> ok = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid.getItem(i, j) == 0 && i < lowest) {
                    ok.add(new int[]{i, j});
                }
            }
        }
        int start = (int) (Math.random() * ok.size());
        Deque<int[]> queue = new LinkedList<>();
        queue.push(ok.get(start));
        int[] dr = new int[]{1, -1, 0, 0};
        int[] dc = new int[]{0, 0, -1, 1};
        int count = 0;
        while (!queue.isEmpty()) {
            int[] loc = queue.removeFirst();
            if (grid.getItem(loc[0], loc[1]) != 0) {
                continue;
            } else if (count == MX) {
                break;
            }
            grid.setItem(loc[0], loc[1], val);
            ++count;
            for (int i = 0; i < 4; i++) {
                int r = loc[0] + dr[i], c = loc[1] + dc[i];
                if ((r < 0 || r >= lowest) || (c < 0 || c >= m)) {
                    continue;
                }
                double chance = Math.random();
                if (chance < PROB) {
                    queue.addLast(new int[]{r, c});
                }
            }
        }
    }
    
    /**
     * The more specific version of explode, for cannonballs.
     */
    public void explode(int row, int col, int RADIUS) {
        explode(row, col, RADIUS, false);
    } 

    /**
     * The more generic version of explode. Works for nukes.
     * @param row the row of the explosion.
     * @param col the column of the explosion.
     * @param RADIUS the radius of the explosion.
     * @param isNuke checking if this explosion is a nuke (for stone collision handling)
     */
    public void explode(int row, int col, int RADIUS, boolean isNuke) {
        Deque<int[]> queue = new LinkedList<>();
        queue.addLast(new int[]{row, col});
        int[] dr = new int[]{1, -1, 0, 0};
        int[] dc = new int[]{0, 0, -1, 1};
        final int rad = RADIUS;
        final int n = grid.getRow(), m = grid.getCol();
        Set<String> visited = new HashSet<>();
        visited.add(row + "," + col);
        
        int stoneExploded = 0;
        final int MAX_STONE = (int) (Math.random() * 6);
        while (!queue.isEmpty()) {
            int[] cur = queue.removeFirst();
            int curRow = cur[0], curCol = cur[1];
            if (!isNuke && stoneExploded >= MAX_STONE) {
                continue;
            }
            if (!isNuke && grid.getItem(cur[0], cur[1]) == 2) {
                ++stoneExploded;
            }
            grid.setItem(curRow, curCol, 1);
            for (int i = 0; i < 4; i++) {
                int r = curRow + dr[i], c = curCol + dc[i];
                double distance = Math.sqrt((r - row) * (r - row) + (c - col) * (c - col));
                
                if (r >= 0 && r < n && c >= 0 && c < m && distance <= rad && !visited.contains(r + "," + c)) {
                    queue.addLast(new int[]{r, c});
                    visited.add(r + "," + c);
                }
            }
        }
    }
}