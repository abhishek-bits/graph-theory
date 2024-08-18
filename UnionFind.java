public class UnionFind {

    private int[] par;
    private int[] rank;
    private int numOfComponents;

    public UnionFind(int N) {
        par = new int[N];
        rank = new int[N];
        numOfComponents = N;

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

        numOfComponents--;
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

    public int getNumOfComponents() {
        return this.numOfComponents;
    }

}
