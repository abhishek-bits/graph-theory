import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class PrerequisiteTasks {

    public boolean isPossible(int N, int P, int[][] prerequisites) {

        // This problem can be solved using Topological Sort in a Directed Graph.
        // Topological Sort in a Graph can be implemented using Kahn's Algorithm

        Map<Integer, List<Integer>> graph = new HashMap<>();

        int[] inDegree = new int[N];

        for (int i = 0; i < N; i++) {
            graph.put(i, new ArrayList<>());
        }

        for (int i = 0; i < P; i++) {

            int u = prerequisites[i][0];
            int v = prerequisites[i][1];

            graph.get(v).add(u);

            inDegree[u]++;
        }

        // System.out.println(graph);

        Queue<Integer> queue = new ArrayDeque<>();

        for (int i = 0; i < N; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        int count = 0;

        while (!queue.isEmpty()) {

            // Here, one by one we'll remove those nodes from the graph
            // who have the indegree = 0.

            count++;

            int at = queue.poll();

            // System.out.println("Node removed: " + at);

            List<Integer> neighbours = graph.get(at);

            for (Integer neighbour : neighbours) {

                inDegree[neighbour]--;

                if (inDegree[neighbour] == 0) {

                    queue.offer(neighbour);

                }

            }

        }

        // Have we removed all the nodes from the graph ?
        return count == N;

    }

}