package com.texthackplus.service;

/**
 * Unified result object returned by the QueryService.
 * Contains the actual answer from the algorithm and metadata for benchmarking.
 */
public class QueryResult {
    private final Object answer;
    private final String algorithm;
    private final String complexity;
    private final long timeMs;
    private final int operations;

    public QueryResult(Object answer, String algorithm, String complexity, long timeNs, int operations) {
        this.answer = answer;
        this.algorithm = algorithm;
        this.complexity = complexity;
        this.timeMs = timeNs / 1_000_000; // Convert nanoseconds to milliseconds
        this.operations = operations;
    }

    public Object getAnswer() { return answer; }
    public String getAlgorithm() { return algorithm; }
    public String getComplexity() { return complexity; }
    public long getTimeMs() { return timeMs; }
    public int getOperations() { return operations; }
}
