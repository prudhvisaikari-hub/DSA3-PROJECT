package com.team7.texthackplus.algorithms.exact;

/**
 * Knuth‑Morris‑Pratt exact pattern matcher.
 * Implements the classic O(n + m) algorithm using a prefix‑function (LPS array).
 * The implementation avoids any java.util collections – it uses primitive
 * arrays and the custom {@code DynamicArray} only for the optional result list.
 *
 * The {@link SearchResult} inner class carries the answer (first match index),
 * the number of character comparisons performed, the elapsed time in nanoseconds,
 * and metadata about the algorithm for the educational query service.
 */
public class KMPMatcher {
    /** Result container used by the query service. */
    public static class SearchResult {
        private final int index; // -1 if not found
        private final int comparisons;
        private final long timeNs;
        private final String algorithm = "KMP";
        private final String complexity = "O(n + m)"; // n = text length, m = pattern length

        public SearchResult(int index, int comparisons, long timeNs) {
            this.index = index;
            this.comparisons = comparisons;
            this.timeNs = timeNs;
        }

        public int getIndex() { return index; }
        public int getComparisons() { return comparisons; }
        public long getTimeNs() { return timeNs; }
        public String getAlgorithm() { return algorithm; }
        public String getComplexity() { return complexity; }
    }

    /**
     * Searches {@code pattern} inside {@code text} using KMP.
     *
     * @param text    The haystack string (may be empty, never {@code null}).
     * @param pattern The needle string (may be empty, never {@code null}).
     * @return a {@link SearchResult} containing the first occurrence index or -1.
     */
    public static SearchResult search(String text, String pattern) {
        long start = System.nanoTime();
        int comparisons = 0;
        int n = text.length();
        int m = pattern.length();
        if (m == 0) {
            long end = System.nanoTime();
            return new SearchResult(0, comparisons, end - start);
        }
        // Build longest‑prefix‑suffix (LPS) array for pattern.
        int[] lps = new int[m];
        // lps[0] is always 0.
        int len = 0; // length of the previous longest prefix suffix
        int i = 1;
        while (i < m) {
            comparisons++;
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1]; // try shorter prefix
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        // Search using the LPS table.
        i = 0; // index for text
        int j = 0; // index for pattern
        int foundIdx = -1;
        while (i < n) {
            comparisons++;
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == m) {
                    foundIdx = i - j;
                    break; // first occurrence found
                }
            } else {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        long end = System.nanoTime();
        return new SearchResult(foundIdx, comparisons, end - start);
    }

    /**
     * Finds all occurrences of {@code pattern} in {@code text} using KMP.
     *
     * @param text    The haystack string.
     * @param pattern The needle string.
     * @return a {@link com.team7.texthackplus.structures.array.DynamicArray} of starting indices.
     */
    public static com.team7.texthackplus.structures.array.DynamicArray<Integer> searchAll(String text, String pattern) {
        com.team7.texthackplus.structures.array.DynamicArray<Integer> matches = new com.team7.texthackplus.structures.array.DynamicArray<>();
        int n = text.length();
        int m = pattern.length();
        if (m == 0 || n == 0) return matches;

        int[] lps = new int[m];
        int len = 0;
        int i = 1;
        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        i = 0;
        int j = 0;
        while (i < n) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == m) {
                    matches.add(i - j);
                    j = lps[j - 1];
                }
            } else {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return matches;
    }
}
