package texthack.algorithms.graph;

import texthack.datastructures.MyArrayList;
import texthack.datastructures.MyHashMap;

/**
 * Weighted directed graph, adjacency-list based, from scratch.
 * Nodes are identified by String labels (e.g. document/citation IDs).
 * Used as the substrate for citation-flow (max-flow) analysis.
 */
public class Graph {

    public static class Edge {
        public final int to;
        public int capacity;
        public int flow;
        public final int reverseIndex; // index of the reverse edge in adj list of 'to'
        public Edge(int to, int capacity, int reverseIndex) {
            this.to = to;
            this.capacity = capacity;
            this.reverseIndex = reverseIndex;
        }
    }

    private final MyHashMap<String, Integer> nodeIndex = new MyHashMap<>();
    private final MyArrayList<String> nodeNames = new MyArrayList<>();
    private final MyArrayList<MyArrayList<Edge>> adjacency = new MyArrayList<>();

    public int addNode(String name) {
        if (nodeIndex.containsKey(name)) return nodeIndex.get(name);
        int idx = nodeNames.size();
        nodeIndex.put(name, idx);
        nodeNames.add(name);
        adjacency.add(new MyArrayList<>());
        return idx;
    }

    public int indexOf(String name) {
        Integer idx = nodeIndex.get(name);
        return idx == null ? -1 : idx;
    }

    public String nameOf(int idx) { return nodeNames.get(idx); }

    public int nodeCount() { return nodeNames.size(); }

    public MyArrayList<Edge> edgesFrom(int idx) { return adjacency.get(idx); }

    /** Adds a directed edge with capacity, plus a zero-capacity reverse edge for residual graphs. */
    public void addEdge(String fromName, String toName, int capacity) {
        int from = addNode(fromName);
        int to = addNode(toName);
        MyArrayList<Edge> fromEdges = adjacency.get(from);
        MyArrayList<Edge> toEdges = adjacency.get(to);
        Edge forward = new Edge(to, capacity, toEdges.size());
        Edge backward = new Edge(from, 0, fromEdges.size());
        fromEdges.add(forward);
        toEdges.add(backward);
    }
}
