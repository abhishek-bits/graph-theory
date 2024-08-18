import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 * Checks whether a graph is Bipartite or not.
 * A bipartite graph is also called a 2-colorable graph.
 */
public class BipartiteGraph {

    public boolean isBipartite(int V, ArrayList<ArrayList<Integer>> graph) {
        /*
         * The problem of 2-Colorable graph is solved using BFS.
         */

        int[] color = new int[V];

        Arrays.fill(color, -1);

        Queue<Integer> queue = new ArrayDeque<>();

        // NOTE:
        // In such problems the graph may be disconnected.
        // Hence, the problem must be solved for all disconnected components.

        for (int i = 0; i < V; i++) {

            if (color[i] != -1) {
                continue;
            }

            queue.offer(i);

            color[i] = 0;

            while (!queue.isEmpty()) {

                int u = queue.poll();

                List<Integer> vList = graph.get(u);

                for (Integer v : vList) {

                    if (color[v] == -1) {

                        color[v] = (color[u] + 1) % 2;

                        queue.offer(v);

                    } else {

                        // We already visited this neighbour node,
                        // if the color of the neighbour node is same as the
                        // color of the current node, then,
                        // this is not a Bipartitie Graph

                        if (color[v] == color[u]) {
                            return false;
                        }

                    }
                }
            }
        }

        return true;

    }
}