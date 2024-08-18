import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class MinMovesByKnight {

    // Function to find out minimum steps Knight needs to reach target position.
    // Function to find out minimum steps Knight needs to reach target position.
    public int minStepToReachTarget(int[] KnightPos, int[] TargetPos, int N) {
        return bfs(KnightPos, TargetPos, N);
    }

    private int bfs(int[] source, int[] target, int n) {

        Queue<List<Integer>> currQueue = new ArrayDeque<>();

        boolean[][] visited = new boolean[n + 1][n + 1];

        // [currRow, currCol, steps]
        currQueue.offer(List.of(source[0], source[1], 0));

        visited[source[0]][source[1]] = true;

        int[] offsetX = new int[] { 2, 2, 1, 1, -2, -2, -1, -1 };
        int[] offsetY = new int[] { 1, -1, 2, -2, 1, -1, 2, -2 };

        while (!currQueue.isEmpty()) {

            List<Integer> pos = currQueue.poll();

            if (pos.get(0) == target[0] && pos.get(1) == target[1]) {
                return pos.get(2);
            }

            // Now, there are 8 choices after this position
            for (int ii = 0; ii < 8; ii++) {

                if (!(pos.get(0) + offsetX[ii] <= 0 || pos.get(0) + offsetX[ii] >= n + 1
                        || pos.get(1) + offsetY[ii] <= 0 || pos.get(1) + offsetY[ii] >= n + 1)) {

                    if (!visited[pos.get(0) + offsetX[ii]][pos.get(1) + offsetY[ii]]) {

                        currQueue.offer(List.of(pos.get(0) + offsetX[ii], pos.get(1) + offsetY[ii], pos.get(2) + 1));

                        visited[pos.get(0) + offsetX[ii]][pos.get(1) + offsetY[ii]] = true;

                    }
                }
            }

        }

        return -1;
    }
}
