import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

class Edge {
    int v;
    int wt;

    public Edge(int v, int wt) {
        this.v = v;
        this.wt = wt;
    }

    @Override
    public String toString() {
        return "[v:" + this.v + ", wt:" + this.wt + "]";
    }
}

class Path {
    int v;
    int dist;

    public Path(int v) {
        this(v, Integer.MAX_VALUE);
    }

    public Path(int v, int dist) {
        this.v = v;
        this.dist = dist;
    }

    @Override
    public String toString() {
        return "[v:" + this.v + ", wt:" + this.dist + "]";
    }
}

/**
 * <a href=
 * "https://www.geeksforgeeks.org/problems/snake-and-ladder-problem4816/1">GFG:
 * Snakes and Ladders</a>
 */
public class SnakesAndLadders {

    private static final Comparator<Path> MIN_HEAP = new Comparator<Path>() {
        @Override
        public int compare(final Path a, final Path b) {
            return a.dist - b.dist;
        }
    };

    int minThrow(int N, int arr[]) {

        Map<Integer, List<Edge>> graph = new HashMap<>();

        /*
         * These are the edges those are reachable by dice.
         * So, weight of every such edge with each possibility
         * from the i'th node will be = 1.
         */
        for (int i = 1; i <= 30; i++) {
            graph.put(i, new ArrayList<>());
            for (int j = i + 1; j <= i + 6 && j <= 30; j++) {
                graph.get(i).add(new Edge(j, 1));
            }
        }

        /*
         * These are the edges those are reachable by either ladder or snake
         * Hence, we do not need any dice moves to go up or down.
         * Thus, the weight of these edges will be zero.
         */
        for (int i = 0; i < 2 * N; i += 2) {

            int start = arr[i];
            int end = arr[i + 1];

            List<Edge> neighbours = graph.get(start);

            boolean found = false;

            for (Edge neighbour : neighbours) {
                if (neighbour.v == end) {
                    neighbour.wt = 0;
                    found = true;
                    break;
                }
            }

            if (!found) {
                graph.get(start).add(new Edge(end, 0)); // either snake or ladder
            }

        }

        /*
         * If the weight of all edges would have been the same,
         * then, we would have applied either BFS or DFS here.
         * But, now we need to find the minimum cost path between
         * the two vertices '1' and '30'.
         */

        return dijkstra(graph, 1, 30);
    }

    /**
     * Application of Dijkstra Algorithm to solve Snakes and Ladders problem.
     * 
     * @param graph
     * @param srcVertex
     * @param destVertex
     * @return
     */
    int dijkstra(Map<Integer, List<Edge>> graph, int srcVertex, int destVertex) {

        Queue<Path> minHeap = new PriorityQueue<>(MIN_HEAP);

        boolean[] visited = new boolean[31];

        minHeap.offer(new Path(srcVertex, 0));

        visited[srcVertex] = true;

        int diceThrows = 30;

        while (!minHeap.isEmpty()) {

            Path path = minHeap.poll();

            if (path.v == destVertex) {
                diceThrows = path.dist;
                break;
            }

            List<Edge> neighbours = graph.get(path.v);

            if (neighbours.get(neighbours.size() - 1).v < path.v) {
                // If this is the cell where snake bites
                // we skip this cell.
                continue;
            }

            for (Edge neighbour : neighbours) {

                // We'll consider this node in 2 cases:
                // 1. Either we have not yet visited this node as of now
                // 2. OR, we can reach this node with a ladder (without dice move).
                if (!visited[neighbour.v] || neighbour.wt == 0) {

                    minHeap.offer(new Path(neighbour.v, path.dist + neighbour.wt));
                    visited[neighbour.v] = true;

                }

            }

        }

        return diceThrows;

    }
}