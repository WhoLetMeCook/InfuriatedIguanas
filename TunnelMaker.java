/**
 * @author Justin Ji
 * @version 1.0
 */
public class Tunnel {
    private int MX = 0;
    private double PROB = 0;
    /**
     * The constructor.
     * @param _MX max # of cells deleted.
     * @param _PROB probability of propagating.
     */
    public Tunnel(int _MX, int _PROB) {
        MX = _MX;
        PROB = _PROB;
    }
    /**
     * Adds a hole at a random location on the grid.
     * @param grid the grid we are editing.
     */
    void generateTunnel(Ground grid) {
        int n = grid.getRow(), m = grid.getCol();
        ArrayList<int[]> ok = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid.getItem(i, j) == 0) {
                    ok.add(new int[]{i, j});
                }
            }
        }
        int start = (int) (Math.random() * ok.size());
        Deque<int[]> queue = new Deque<>();
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
                if ((r < 0 || r >= n) || (c < 0 || c >= m)) {
                    continue;
                }
                double chance = Math.rand();
                if (chance < PROB) {
                    queue.addLast(new int[]{r, c});
                }
            }
        }
    }
}