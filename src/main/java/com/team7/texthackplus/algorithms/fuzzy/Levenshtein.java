package com.team7.texthackplus.algorithms.fuzzy;

/**
 * Levenshtein edit‑distance implementation (dynamic programming).
 * No java.util collections are used – only primitive int arrays.
 * The algorithm can be run with an optional maximum distance (threshold)
 * to allow early termination when the distance exceeds the bound.
 *
 * The {@link SearchResult} class mirrors the one used by the KMP matcher
 * and provides the computed distance, number of character comparisons,
 * elapsed time (nanoseconds), algorithm name and its theoretical complexity.
 */
public class Levenshtein {
    /** Result container for the fuzzy‑search query service. */
    public static class SearchResult {
        private final int distance; // edit distance between the two strings
        private final int comparisons; // how many character comparisons were made
        private final long timeNs; // elapsed time in nanoseconds
        private final String algorithm = "Levenshtein";
        private final String complexity = "O(n·m)"; // n = text length, m = pattern length

        public SearchResult(int distance, int comparisons, long timeNs) {
            this.distance = distance;
            this.comparisons = comparisons;
            this.timeNs = timeNs;
        }

        public int getDistance() { return distance; }
        public int getComparisons() { return comparisons; }
        public long getTimeNs() { return timeNs; }
        public String getAlgorithm() { return algorithm; }
        public String getComplexity() { return complexity; }
    }

    /**
     * Computes the full Levenshtein distance between {@code s1} and {@code s2}.
     * This method always computes the exact distance.
     */
    public static SearchResult computeDistance(String s1, String s2) {
        return computeDistance(s1, s2, Integer.MAX_VALUE);
    }

    /**
     * Computes the Levenshtein distance with an early‑stop threshold.
     * If the distance exceeds {@code maxDistance}, the algorithm stops
     * as soon as it can determine that the final distance will be larger.
     * The returned distance will be the minimal value observed (which may be
     * greater than {@code maxDistance} if the early stop occurred).
     */
    public static SearchResult computeDistance(String s1, String s2, int maxDistance) {
        long start = System.nanoTime();
        int comparisons = 0;
        int n = s1.length();
        int m = s2.length();

        // Edge cases – one string empty.
        if (n == 0) {
            long end = System.nanoTime();
            return new SearchResult(m, comparisons, end - start);
        }
        if (m == 0) {
            long end = System.nanoTime();
            return new SearchResult(n, comparisons, end - start);
        }

        // Use only two rows to keep memory O(min(n,m)). Ensure m is the shorter string.
        if (m > n) {
            // swap to make m <= n for less memory.
            String tmp = s1; s1 = s2; s2 = tmp;
            int tmpLen = n; n = m; m = tmpLen;
        }

        int[] prev = new int[m + 1]; // previous row of DP table
        int[] cur = new int[m + 1];  // current row

        // Initialize base case: distance from empty prefix of s1 to prefixes of s2.
        for (int j = 0; j <= m; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= n; i++) {
            cur[0] = i;
            int minInRow = cur[0];
            char c1 = s1.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                comparisons++;
                char c2 = s2.charAt(j - 1);
                int cost = (c1 == c2) ? 0 : 1;
                int del = prev[j] + 1;      // deletion
                int ins = cur[j - 1] + 1;   // insertion
                int sub = prev[j - 1] + cost; // substitution
                int best = del;
                if (ins < best) best = ins;
                if (sub < best) best = sub;
                cur[j] = best;
                if (best < minInRow) minInRow = best;
            }
            // Early termination check – if the minimal value of the current row
            // already exceeds the allowed threshold, we can stop.
            if (minInRow > maxDistance) {
                long end = System.nanoTime();
                return new SearchResult(minInRow, comparisons, end - start);
            }
            // swap rows for next iteration
            int[] tmpRow = prev; prev = cur; cur = tmpRow;
        }
        long end = System.nanoTime();
        return new SearchResult(prev[m], comparisons, end - start);
    }
}
