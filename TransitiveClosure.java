import java.util.ArrayList;
import java.util.List;

/**
 * Transitive Closure of a graph is used to find a matrix which
 * tell us whether for a given vertex 'i', we can reach a vertex 'j'.
 * 0 <= i, j < V and i != j.
 */
public class TransitiveClosure {

    static ArrayList<ArrayList<Integer>> res;

    static boolean[] visited;

    static ArrayList<ArrayList<Integer>> transitiveClosure(int N, int adj[][]) {

        res = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            res.add(new ArrayList<>());
            for (int j = 0; j < N; j++) {
                res.get(i).add(0);
            }
        }

        List<List<Integer>> graph = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            graph.add(new ArrayList<>());
            for (int j = 0; j < N; j++) {
                if (adj[i][j] == 1 && i != j) {
                    graph.get(i).add(j);
                }
            }
        }

        for (int i = 0; i < N; i++) {

            visited = new boolean[N];

            dfs(graph, i, i);
        }

        return res;

    }

    static void dfs(List<List<Integer>> graph, int s, int at) {

        res.get(s).set(at, 1);

        visited[at] = true;

        List<Integer> neighbours = graph.get(at);

        for (Integer neighbour : neighbours) {

            if (!visited[neighbour]) {
                dfs(graph, s, neighbour);
            }

        }

    }

}