package texthack.algorithms.graph;

import texthack.datastructures.MyArrayList;
import texthack.datastructures.MyQueue;

/**
 * Edmonds-Karp implementation of the Ford-Fulkerson max-flow method
 * (BFS augmenting paths, O(V * E^2)). Used to model "citation flow" -
 * treating citation counts between papers/authors as edge capacities
 * and computing the maximum sustainable flow of influence between a
 * source and sink node.
 */
public class MaxFlow {

    public static class Result {
        public final int maxFlow;
        public final int[] parentNode;
        public final int[] parentEdge;
        Result(int maxFlow, int[] parentNode, int[] parentEdge) {
            this.maxFlow = maxFlow;
            this.parentNode = parentNode;
            this.parentEdge = parentEdge;
        }
    }

    public static Result computeMaxFlow(Graph graph, String sourceName, String sinkName) {
        int source = graph.indexOf(sourceName);
        int sink = graph.indexOf(sinkName);
        if (source == -1 || sink == -1) return new Result(0, null, null);

        int n = graph.nodeCount();
        int totalFlow = 0;
        int[] parentNode = new int[n];
        int[] parentEdge = new int[n];

        while (true) {
            for (int i = 0; i < n; i++) { parentNode[i] = -1; parentEdge[i] = -1; }
            parentNode[source] = source;

            MyQueue<Integer> queue = new MyQueue<>();
            queue.enqueue(source);
            boolean reachedSink = false;

            while (!queue.isEmpty() && !reachedSink) {
                int u = queue.dequeue();
                MyArrayList<Graph.Edge> edges = graph.edgesFrom(u);
                for (int e = 0; e < edges.size(); e++) {
                    Graph.Edge edge = edges.get(e);
                    int residual = edge.capacity - edge.flow;
                    if (residual > 0 && parentNode[edge.to] == -1) {
                        parentNode[edge.to] = u;
                        parentEdge[edge.to] = e;
                        if (edge.to == sink) { reachedSink = true; break; }
                        queue.enqueue(edge.to);
                    }
                }
            }

            if (parentNode[sink] == -1) break; // no augmenting path left

            // find bottleneck capacity along the path
            int bottleneck = Integer.MAX_VALUE;
            int v = sink;
            while (v != source) {
                int u = parentNode[v];
                Graph.Edge edge = graph.edgesFrom(u).get(parentEdge[v]);
                bottleneck = Math.min(bottleneck, edge.capacity - edge.flow);
                v = u;
            }

            // push flow along the path, updating forward and reverse edges
            v = sink;
            while (v != source) {
                int u = parentNode[v];
                Graph.Edge forward = graph.edgesFrom(u).get(parentEdge[v]);
                forward.flow += bottleneck;
                Graph.Edge backward = graph.edgesFrom(v).get(forward.reverseIndex);
                backward.flow -= bottleneck;
                v = u;
            }

            totalFlow += bottleneck;
        }

        return new Result(totalFlow, parentNode, parentEdge);
    }
}
