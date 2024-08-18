import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Finding a Hamiltonian Cycle or a Hamiltonian Path is an NP-Complete problem.
 * But here our task is simple, we just need to find whether Hamiltonian path
 * exists in the given graph or not.
 * A Hamiltonian path is a path in an undirected graph that visits each vertex
 * exactly once.
 */
public class HamiltonianPath {
    boolean hamiltonianPathExists;

    boolean[] visited;

    boolean check(int N, int M, ArrayList<ArrayList<Integer>> Edges) {
        hamiltonianPathExists = false;

        visited = new boolean[N + 1];

        // Given the edges, we'll first create our graph.
        Map<Integer, List<Integer>> graph = new HashMap<>();

        for (int i = 1; i <= N; i++) {
            graph.put(i, new ArrayList<>());
        }

        for (int i = 0; i < M; i++) {

            int u = Edges.get(i).get(0);
            int v = Edges.get(i).get(1);

            graph.get(u).add(v);
            graph.get(v).add(u);
        }

        for (int i = 1; i <= N; i++) {

            visited = new boolean[N + 1];

            dfs(graph, N, 1, i);

            if (hamiltonianPathExists) {
                break;
            }
        }

        return hamiltonianPathExists;
    }

    void dfs(Map<Integer, List<Integer>> graph, int N, int count, int at) {

        if (count == N) {
            hamiltonianPathExists = true;
            return;
        }

        visited[at] = true;

        List<Integer> neighbours = graph.get(at);

        for (Integer neighbour : neighbours) {

            if (!hamiltonianPathExists) {

                if (!visited[neighbour]) {

                    dfs(graph, N, count + 1, neighbour);

                    if (hamiltonianPathExists) {
                        return;
                    }

                    // We'll backtrack and mark this node as unvisited
                    // as there may exist another path that goes
                    // through this node.
                    visited[neighbour] = false;

                }
            }
        }
    }
}
