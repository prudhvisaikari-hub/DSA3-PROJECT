package com.team7.texthackplus.structures.graph;

/**
 * Simple adjacency‑list graph for directed citation networks.
 * Vertices are identified by integer IDs (e.g., document IDs).
 * Supports adding edges and basic BFS/DFS traversals used by the
 * max‑flow algorithm.
 */
public class AdjListGraph {
    private final int vertexCount;
    private final com.team7.texthackplus.structures.list.SinglyLinkedList<Integer>[] adj;

    @SuppressWarnings("unchecked")
    public AdjListGraph(int vertexCount) {
        if (vertexCount <= 0) {
            throw new IllegalArgumentException("Vertex count must be positive");
        }
        this.vertexCount = vertexCount;
        this.adj = (com.team7.texthackplus.structures.list.SinglyLinkedList<Integer>[]) new com.team7.texthackplus.structures.list.SinglyLinkedList[vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            adj[i] = new com.team7.texthackplus.structures.list.SinglyLinkedList<>();
        }
    }

    /** Adds a directed edge from {@code src} to {@code dest}. */
    public void addEdge(int src, int dest) {
        validateVertex(src);
        validateVertex(dest);
        adj[src].addFirst(dest); // prepend for O(1) insertion
    }

    /** Returns an iterable (array) of neighbours of {@code v}. */
    public int[] neighbours(int v) {
        validateVertex(v);
        int size = adj[v].size();
        int[] nbrs = new int[size];
        for (int i = 0; i < size; i++) {
            nbrs[i] = adj[v].get(i);
        }
        return nbrs;
    }

    /** Returns number of vertices. */
    public int vertexCount() {
        return vertexCount;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= vertexCount) {
            throw new IllegalArgumentException("Vertex " + v + " is out of bounds");
        }
    }
}
