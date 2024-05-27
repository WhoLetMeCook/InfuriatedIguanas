/**
 * @author Justin Ji
 * @version 1.0
 */

import java.util.*;

public class TunnelMaker {
    private final int MX;
    private final double PROB;
    /**
     * The constructor.
     * @param _MX max # of cells deleted in a "bubble"
     * @param _PROB probability of propagating.
     */
    public TunnelMaker(int _MX, double _PROB) {
        MX = _MX;
        PROB = _PROB;
    }
    /**
     * Adds a hole at a random location on the grid.
     * @param grid the grid we are editing.
     */
    public void generateTunnel(Ground grid) {
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
            grid.setItem(loc[0], loc[1], 1);
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
     * Creates a "circular" egg...
     */
    public void placeEgg(Ground grid) {
        final int n = grid.getRow(), m = grid.getCol(),
                  EGG_DIST = n / 10;
        final int lowest = (int) ((2.0 / 3) * n);
        int sr = lowest + (int) (Math.random() * (n / 5)),
            sc = EGG_DIST + (int) (Math.random() * (m - EGG_DIST * 2));
        grid.setItem(sr, sc, 3);
    }
    public void explode(Ground grid, int row, int col) {
        Deque<int[]> queue = new LinkedList<>();
        queue.addLast(new int[]{row, col, 0});
        int[] dr = new int[]{1, -1, 0, 0};
        int[] dc = new int[]{0, 0, -1, 1};
        final int DIST = (row + col) / 15;
        final int n = grid.getRow(), m = grid.getCol();
        while (!queue.isEmpty()) {
            int[] cur = queue.removeFirst();
            grid.setItem(cur[0], cur[1], 0);
            if (cur[2] >= DIST) continue;
            for (int i = 0; i < 4; i++) {
                int r = cur[0] + dr[i], c = cur[1] + dc[i];
                if (r >= 0 && r < n && c >= 0 && c < m) {
                    queue.addLast(new int[]{r, c, cur[2] + 1});
                }
            }
        }
    }
}
