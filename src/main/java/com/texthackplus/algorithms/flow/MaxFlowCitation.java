package com.texthackplus.algorithms.flow;

import com.texthackplus.structures.queue.Queue;

/**
 * Computes the maximum flow in a citation network to identify influence bottlenecks.
 *
 * Uses the Edmonds-Karp algorithm (BFS-based augmenting paths).
 * To comply with the "no java.util" constraint, we use our custom {@code Queue}
 * and primitive arrays for tracking capacities and parent pointers.
 */
public class MaxFlowCitation {

    public static class SearchResult {
        private final int maxFlow;
        private final int comparisons;
        private final long timeNs;
        private final String algorithm = "Edmonds-Karp Max Flow";
        private final String complexity = "O(V * E^2)";

        public SearchResult(int maxFlow, int comparisons, long timeNs) {
            this.maxFlow = maxFlow;
            this.comparisons = comparisons;
            this.timeNs = timeNs;
        }

        public int getMaxFlow() { return maxFlow; }
        public int getComparisons() { return comparisons; }
        public long getTimeNs() { return timeNs; }
        public String getAlgorithm() { return algorithm; }
        public String getComplexity() { return complexity; }
    }

    /**
     * Computes the maximum flow from source to sink.
     *
     * @param capacities Adjacency matrix representing edge capacities.
     * @param source     The source vertex.
     * @param sink       The sink vertex.
     * @return a {@link SearchResult} containing the maximum flow.
     */
    public static SearchResult computeMaxFlow(int[][] capacities, int source, int sink) {
        long start = System.nanoTime();
        int comparisons = 0;
        int n = capacities.length;

        // Create a residual capacity matrix.
        int[][] residual = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                residual[i][j] = capacities[i][j];
            }
        }

        int[] parent = new int[n];
        int maxFlow = 0;

        // While there is an augmenting path from source to sink...
        while (bfs(residual, source, sink, parent)) {
            comparisons++; // One augmenting path found

            // Find the bottleneck capacity in the augmenting path.
            int pathFlow = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                if (residual[u][v] < pathFlow) {
                    pathFlow = residual[u][v];
                }
            }

            // Update residual capacities along the path.
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                residual[u][v] -= pathFlow;
                residual[v][u] += pathFlow;
            }

            maxFlow += pathFlow;
        }

        long end = System.nanoTime();
        return new SearchResult(maxFlow, comparisons, end - start);
    }

    private static boolean bfs(int[][] residual, int source, int sink, int[] parent) {
        int n = residual.length;
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new Queue<>();

        queue.enqueue(source);
        visited[source] = true;
        parent[source] = -1;

        while (!queue.isEmpty()) {
            int u = queue.dequeue();

            for (int v = 0; v < n; v++) {
                if (!visited[v] && residual[u][v] > 0) {
                    queue.enqueue(v);
                    parent[v] = u;
                    visited[v] = true;
                    if (v == sink) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
