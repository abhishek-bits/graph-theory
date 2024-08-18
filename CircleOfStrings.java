import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <a href=
 * "https://www.geeksforgeeks.org/problems/circle-of-strings4530/1">GFG: Circle
 * of Strings</a>
 * 
 * <p>
 * Given an array of lowercase strings A[] of size N,
 * determine if the strings can be chained together to form a circle.
 * A string X can be chained together with another string Y if
 * the last character of X is same as first character of Y.
 * If every string of the array can be chained with exactly two strings of the
 * array
 * (one with the first character and second with the last character of the
 * string),
 * it will form a circle.
 * </p>
 * 
 * <p>
 * <b>For example</b>, for the array arr[] = {"for", "geek", "rig", "kaf"}
 * the answer will be Yes as the given strings can be chained as "for", "rig",
 * "geek" and "kaf".
 * </p>
 */
public class CircleOfStrings {
    int isCircle(int N, String A[]) {
        Map<Integer, List<Integer>> graph = new HashMap<>();

        for (String s : A) {
            int u = s.charAt(0) - 'a';
            int v = s.charAt(s.length() - 1) - 'a';

            if (!graph.containsKey(u)) {
                graph.put(u, new ArrayList<>());
            }
            if (!graph.containsKey(v)) {
                graph.put(v, new ArrayList<>());
            }

            graph.get(u).add(v);
        }

        // The graph may be disconnected.
        if (!isConnected(graph)) {
            return 0;
        }

        // Does this graph has an eulerian circuit ?
        return hasEulerianCircuit(graph) ? 1 : 0;
    }

    /**
     * Given a Directed Graph,
     * the existence of a Eulerian Circuit is found out from the property:
     * "Every vertex has an equal in-degree and out-degree."
     */
    boolean hasEulerianCircuit(Map<Integer, List<Integer>> graph) {

        int[] inDegree = new int[26];
        int[] outDegree = new int[26];

        Set<Integer> graphNodes = new HashSet<>();

        for (Map.Entry<Integer, List<Integer>> entry : graph.entrySet()) {

            int u = entry.getKey();

            List<Integer> vList = entry.getValue();

            for (Integer v : vList) {

                graphNodes.add(v);

                inDegree[v]++;
                outDegree[u]++;

            }

            graphNodes.add(u);
        }

        // System.out.println(Arrays.toString(inDegree));
        // System.out.println(Arrays.toString(outDegree));

        for (int i = 0; i < 26; i++) {
            if (graphNodes.contains(i)) {
                if (inDegree[i] != outDegree[i]) {
                    return false;
                }
            }
        }

        return true;

    }

    boolean isConnected(Map<Integer, List<Integer>> graph) {

        Map<Integer, Boolean> visited = new HashMap<>();

        int source = -1;

        for (Integer u : graph.keySet()) {
            if (source == -1) {
                source = u;
            }
            visited.put(u, Boolean.FALSE);
        }

        dfs(graph, visited, source);

        // System.out.println(visited);

        for (Map.Entry<Integer, Boolean> entry : visited.entrySet()) {
            if (Boolean.FALSE.equals(entry.getValue())) {
                return false;
            }
        }

        return true;

    }

    void dfs(Map<Integer, List<Integer>> graph, Map<Integer, Boolean> visited, int u) {

        visited.put(u, Boolean.TRUE);

        List<Integer> vList = graph.get(u);

        for (Integer v : vList) {
            if (!visited.get(v)) {
                dfs(graph, visited, v);
            }
        }

    }

}