import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Edge {
    int from, to;
    Edge residual;
    long flow;
    final long capacity;

    public Edge(int from, int to, long capacity) {
        this.from = from;
        this.to = to;
        this.capacity = capacity;
    }

    public boolean isResidual() {
        return capacity == 0;
    }

    public long getRemainingCapacity() {
        return capacity - flow;
    }

    public void augment(long bottleneck) {
        flow += bottleneck;
        residual.flow -= bottleneck;
    }

    public String toString(int s, int t) {
        String u = (from == s) ? "s" : ((from == t) ? "t" : String.valueOf(from));
        String v = (to == s) ? "s" : ((to == t) ? "t" : String.valueOf(to));
        return String.format("Edge %s -> %s | flow = %3d | capacity = %3d | isResidual = %s", u, v, flow, capacity,
                isResidual());
    }
}

abstract class NetworkFlowSolverBase {

    // To avoid overflow, set infinity to a value less than Long.MAX_VALUE
    static final long INF = Long.MAX_VALUE >> 1;

    // Inputs: n = number of nodes, s = source, t = sink
    final int n, s, t;

    // 'visited' and 'visitedToken' are variables used in graph sub-routines to
    // track whether a node has been visited or not. In particular, node 'i' was
    // recently visited if visited[i] = visitedToken is true. This is handy
    // because to mark all nodes as unvisited simply increment the visitedToken.
    protected int visitedToken = 1;
    protected int[] visited;

    // Indicates whether the network flow algorithm has ran. The solver only
    // needs to run once because it always yields the same result.
    protected boolean solved;

    // The maximum flow. Calculated by calling the {@link #solve} method.
    protected long maxFlow;

    // The adjacency list representing the flow graph.
    protected Map<Integer, List<Edge>> graph;

    /**
     * Creates an instance of a flow network solver. Use the {@link #addEdge}
     * method to add edges to the graph.
     * 
     * @param n The number of nodes in the graph including s and t.
     * @param s The index of the source node, 0 <= s < n
     * @param t The index of the sink node, 0 <= t < n and t != s
     */
    public NetworkFlowSolverBase(int n, int s, int t) {
        this.n = n;
        this.s = s;
        this.t = t;
        initializeEmptyFlowGraph();
        visited = new int[n];
    }

    // Constructs an empty graph with n nodes including s and t.
    private void initializeEmptyFlowGraph() {
        graph = new HashMap<>(n);
        for (int i = 0; i < n; i++) {
            graph.put(i, new ArrayList<Edge>());
        }
    }

    /**
     * Adds a directed edge (and its residual edge) to the flow graph.
     * 
     * @param from     The index of the node the directed edge starts at.
     * @param to       The index of the node the directed edge ends at.
     * @param capacity The capacity of the edge.
     */
    public void addEdge(int from, int to, int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Forward edge capacity <= 0");
        }
        Edge e1 = new Edge(from, to, capacity);
        Edge e2 = new Edge(to, from, 0);
        e1.residual = e2;
        e2.residual = e1;
        graph.get(from).add(e1);
        graph.get(to).add(e2);
    }

    /**
     * Returns the residual graph after the solver has been executed. This
     * allows us to inspect the {@link Edge#flow} and {@link Edge#capacity}
     * values of each edge. This is useful if we are debugging or want to
     * figure out which edges were used during the max flow.
     */
    public Map<Integer, List<Edge>> getFlowGraph() {
        execute();
        return graph;
    }

    // Returns the maximum flow from the source to the sink.
    public long getMaxFlow() {
        execute();
        return maxFlow;
    }

    // Wrapper method that ensures we only call solve() once
    private void execute() {
        if (solved)
            return;
        solved = true;
        solve();
    }

    // Method to implement which solves the network flow problem.
    public abstract void solve();
}

class FordFulkersonDfsSolver extends NetworkFlowSolverBase {

    /**
     * Creates an instance of a flow network solver. Use the {@link #addEdge}
     * method to add edges to this graph.
     * 
     * @param n The number of nodes in the graph including s and t.
     * @param s The index of the source node, 0 <= s < n
     * @param t The index of the sink node, 0 <= t < n and t != s
     */
    public FordFulkersonDfsSolver(int n, int s, int t) {
        super(n, s, t);
    }

    // Performs the Ford-Fulkerson method applying a depth first search as
    // a means of finding an augmenting path.
    @Override
    public void solve() {
        // Find max flow by adding all augmenting path flows.
        for (long f = dfs(s, INF); f != 0; f = dfs(s, INF)) {
            visitedToken++;
            maxFlow += f;
        }
    }

    // DFS will return the Bottlneck value found along the augmented path.
    private long dfs(int at, long flow) {
        // At sink node, return augmented path flow.
        if (at == t)
            return flow;

        // Mark the current node as visited.
        visited[at] = visitedToken;

        List<Edge> edges = graph.get(at);

        for (Edge edge : edges) {

            if (edge.getRemainingCapacity() > 0 && visited[edge.to] != visitedToken) {

                long bottleNeck = dfs(edge.to, Math.min(flow, edge.getRemainingCapacity()));

                // If we made it from s -> t (a.k.a. bottleneck > 0) then
                // augment flow with bottleneck value.
                if (bottleNeck > 0) {
                    edge.augment(bottleNeck);
                    return bottleNeck;
                }
            }
        }
        return 0;
    }
}

public class MaxFlowFordFulkerson {

    public static void main(String[] args) {
        // n is the number of nodes including the source and the sink.
        int n = 12;

        int s = n - 2;
        int t = n - 1;

        NetworkFlowSolverBase solver = new FordFulkersonDfsSolver(n, s, t);

        // Edges from source
        solver.addEdge(s, 0, 10);
        solver.addEdge(s, 1, 5);
        solver.addEdge(s, 2, 10);

        // Middle edges
        solver.addEdge(0, 3, 10);
        solver.addEdge(1, 2, 10);
        solver.addEdge(2, 5, 15);
        solver.addEdge(3, 1, 2);
        solver.addEdge(3, 6, 15);
        solver.addEdge(4, 1, 15);
        solver.addEdge(4, 3, 3);
        solver.addEdge(5, 4, 4);
        solver.addEdge(5, 8, 10);
        solver.addEdge(6, 7, 10);
        solver.addEdge(7, 4, 10);
        solver.addEdge(7, 5, 7);

        // Edges to sink
        solver.addEdge(6, t, 15);
        solver.addEdge(8, t, 10);

        // Prints:
        // Maximum Flow is: 23
        System.out.println("Maximum Flow is: " + solver.getMaxFlow());

        Map<Integer, List<Edge>> resultGraph = solver.getFlowGraph();

        // Displays all edges part of the resulting residual graph
        for (Map.Entry<Integer, List<Edge>> edges : resultGraph.entrySet()) {
            for (Edge e : edges.getValue()) {
                System.out.println(e.toString());
            }
        }
    }
}
