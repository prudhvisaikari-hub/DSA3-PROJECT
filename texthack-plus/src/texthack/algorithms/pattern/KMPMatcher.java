package texthack.algorithms.pattern;

import texthack.datastructures.MyArrayList;

/**
 * Knuth-Morris-Pratt exact pattern matching, O(n + m).
 * Used for TextHack+'s "exact pattern matching" query type.
 */
public class KMPMatcher {

    /** Builds the longest-proper-prefix-suffix (failure) table for the pattern. */
    private static int[] buildLPS(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;
        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                lps[i++] = ++len;
            } else if (len != 0) {
                len = lps[len - 1];
            } else {
                lps[i++] = 0;
            }
        }
        return lps;
    }

    /** Returns all 0-based starting indices where pattern occurs in text. */
    public static MyArrayList<Integer> search(String text, String pattern) {
        MyArrayList<Integer> matches = new MyArrayList<>();
        if (pattern == null || pattern.isEmpty() || text == null) return matches;
        int n = text.length(), m = pattern.length();
        int[] lps = buildLPS(pattern);
        int i = 0, j = 0;
        while (i < n) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == m) {
                    matches.add(i - j);
                    j = lps[j - 1];
                }
            } else if (j != 0) {
                j = lps[j - 1];
            } else {
                i++;
            }
        }
        return matches;
    }
}
