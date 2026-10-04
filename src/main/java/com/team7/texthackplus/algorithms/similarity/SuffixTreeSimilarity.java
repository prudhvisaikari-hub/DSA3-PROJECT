package com.team7.texthackplus.algorithms.similarity;

/**
 * Genuine Suffix Array and LCP (Longest Common Prefix) array-based similarity metric.
 *
 * Replaces the naive O(n^3) approach with a true suffix structure.
 * 1. Combines strings as "a#b".
 * 2. Builds a Suffix Array using a comparison-based QuickSort (O(n^2 log n) worst case due to string comparisons).
 * 3. Builds an LCP array using Kasai's algorithm in O(n) time.
 * 4. Scans the LCP array to find the longest common substring originating from both original strings.
 */
public class SuffixTreeSimilarity {

    /** Result container used by the query service. */
    public static class SearchResult {
        private final String substring;
        private final int length;
        private final int comparisons;
        private final long timeNs;
        private final String algorithm = "Suffix Array + LCP";
        private final String complexity = "O(n^2 log n)"; // Construction sort bottleneck

        public SearchResult(String substring, int length, int comparisons, long timeNs) {
            this.substring = substring;
            this.length = length;
            this.comparisons = comparisons;
            this.timeNs = timeNs;
        }

        public String getSubstring() { return substring; }
        public int getLength() { return length; }
        public int getComparisons() { return comparisons; }
        public long getTimeNs() { return timeNs; }
        public String getAlgorithm() { return algorithm; }
        public String getComplexity() { return complexity; }
    }

    /**
     * Computes the longest common substring between two strings.
     *
     * @param a first string (may be empty, never {@code null})
     * @param b second string (may be empty, never {@code null})
     * @return a {@link SearchResult} describing the longest common substring
     */
    public static SearchResult longestCommonSubstring(String a, String b) {
        long start = System.nanoTime();
        int comparisons = 0;

        if (a.isEmpty() || b.isEmpty()) {
            return new SearchResult("", 0, comparisons, System.nanoTime() - start);
        }

        // 1. Combine strings with a unique separator
        String s = a + "#" + b;
        int sepIdx = a.length();
        int n = s.length();

        // 2. Build Suffix Array
        int[] sa = buildSuffixArray(s);

        // 3. Build LCP Array (Kasai's algorithm)
        int[] lcp = buildLCPArray(s, sa);

        // 4. Find the max LCP between adjacent suffixes from different source strings
        int maxLen = 0;
        int bestStart = -1;

        for (int i = 1; i < n; i++) {
            comparisons++;
            int pos1 = sa[i - 1];
            int pos2 = sa[i];

            // Check if one is from 'a' (before '#') and one is from 'b' (after '#')
            boolean p1InA = pos1 < sepIdx;
            boolean p2InA = pos2 < sepIdx;
            boolean p1InB = pos1 > sepIdx;
            boolean p2InB = pos2 > sepIdx;

            if ((p1InA && p2InB) || (p1InB && p2InA)) {
                if (lcp[i] > maxLen) {
                    maxLen = lcp[i];
                    bestStart = pos1; // Does not matter which pos we take, they match up to maxLen
                }
            }
        }

        String best = maxLen > 0 ? s.substring(bestStart, bestStart + maxLen) : "";
        long end = System.nanoTime();
        return new SearchResult(best, maxLen, comparisons, end - start);
    }

    /** Visible for testing: builds suffix array using quicksort. */
    public static int[] buildSuffixArray(String s) {
        int n = s.length();
        int[] sa = new int[n];
        for (int i = 0; i < n; i++) sa[i] = i;
        quickSort(sa, s, 0, n - 1);
        return sa;
    }

    private static void quickSort(int[] sa, String s, int left, int right) {
        if (left < right) {
            int pivot = partition(sa, s, left, right);
            quickSort(sa, s, left, pivot - 1);
            quickSort(sa, s, pivot + 1, right);
        }
    }

    private static int partition(int[] sa, String s, int left, int right) {
        int pivotIdx = sa[right];
        int i = left - 1;
        for (int j = left; j < right; j++) {
            if (compareSuffixes(s, sa[j], pivotIdx) <= 0) {
                i++;
                int temp = sa[i];
                sa[i] = sa[j];
                sa[j] = temp;
            }
        }
        int temp = sa[i + 1];
        sa[i + 1] = sa[right];
        sa[right] = temp;
        return i + 1;
    }

    private static int compareSuffixes(String s, int i, int j) {
        int len = s.length();
        while (i < len && j < len) {
            char ci = s.charAt(i);
            char cj = s.charAt(j);
            if (ci != cj) {
                return ci - cj; // standard character comparison
            }
            i++;
            j++;
        }
        return (len - i) - (len - j);
    }

    /** Visible for testing: builds LCP array using Kasai's algorithm in O(n). */
    public static int[] buildLCPArray(String s, int[] sa) {
        int n = s.length();
        int[] lcp = new int[n];
        int[] inv = new int[n];
        for (int i = 0; i < n; i++) {
            inv[sa[i]] = i;
        }

        int k = 0;
        for (int i = 0; i < n; i++) {
            if (inv[i] > 0) {
                int j = sa[inv[i] - 1];
                while (i + k < n && j + k < n && s.charAt(i + k) == s.charAt(j + k)) {
                    k++;
                }
                lcp[inv[i]] = k;
                if (k > 0) {
                    k--;
                }
            }
        }
        return lcp;
    }
}
