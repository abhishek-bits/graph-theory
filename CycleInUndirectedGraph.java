import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CycleInUndirectedGraph {
    // Function to detect cycle using DSU in an undirected graph.
    public int detectCycle(int V, ArrayList<ArrayList<Integer>> graph) {

        UnionFind unionFind = new UnionFind(V);

        Map<Integer, Map<Integer, Integer>> map = new HashMap<>();

        for (int i = 0; i < V; i++) {

            List<Integer> neighbours = graph.get(i);

            for (Integer neighbour : neighbours) {

                // If we have already previously connected the two vertices,
                // We do not have to bother about them again.
                if (map.containsKey(i) && map.get(i).containsKey(neighbour)) {
                    continue;
                }
                if (map.containsKey(neighbour) && map.get(neighbour).containsKey(i)) {
                    continue;
                }

                if (!map.containsKey(i)) {
                    map.put(i, new HashMap<>());
                }

                map.get(i).put(neighbour, 1);

                if (unionFind.isConnected(i, neighbour)) {
                    // We are connecting to a sibling who is already part
                    // of same equivalence class.
                    return 1;
                }

                unionFind.union(i, neighbour);

            }

        }

        return 0;
    }

}

class UnionFind {

    int[] par;
    int[] rank;

    public UnionFind(int N) {
        par = new int[N];
        rank = new int[N];

        for (int i = 0; i < N; i++) {
            par[i] = i;
            rank[i] = 1;
        }

    }

    public void union(int v1, int v2) {

        int rootV1 = find(v1);
        int rootV2 = find(v2);

        if (rootV1 == rootV2) {
            return;
        }

        if (rank[rootV1] < rank[rootV2]) {
            par[rootV1] = rootV2;
            rank[rootV2] += rank[rootV1];
        } else {
            par[rootV2] = rootV1;
            rank[rootV1] += rank[rootV2];
        }
    }

    public int find(int v) {

        int rootV = v;

        while (par[rootV] != rootV) {
            rootV = par[rootV];
        }

        // Path compression
        while (v != rootV) {
            v = par[v];
            par[v] = rootV;
            rank[rootV]++;
        }

        return rootV;
    }

    public boolean isConnected(int v1, int v2) {
        return find(v1) == find(v2);
    }
}