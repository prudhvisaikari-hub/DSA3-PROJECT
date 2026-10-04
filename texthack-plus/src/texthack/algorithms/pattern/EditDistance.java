package texthack.algorithms.pattern;

import texthack.datastructures.MyArrayList;

/**
 * Levenshtein edit distance (dynamic programming, O(n*m)) plus a fuzzy-search
 * helper that scans a vocabulary and returns every word within a given
 * edit-distance threshold of the query. Backs TextHack+'s "fuzzy search"
 * query type (handles typos / near-matches).
 */
public class EditDistance {

    public static int distance(String a, String b) {
        int n = a.length(), m = b.length();
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int insert = dp[i][j - 1] + 1;
                    int delete = dp[i - 1][j] + 1;
                    int replace = dp[i - 1][j - 1] + 1;
                    dp[i][j] = Math.min(insert, Math.min(delete, replace));
                }
            }
        }
        return dp[n][m];
    }

    public static class FuzzyMatch {
        public final String word;
        public final int distance;
        public FuzzyMatch(String word, int distance) { this.word = word; this.distance = distance; }
    }

    /** Scans the vocabulary and returns matches within maxDistance, sorted by closeness. */
    public static MyArrayList<FuzzyMatch> fuzzySearch(String query, MyArrayList<String> vocabulary, int maxDistance) {
        MyArrayList<FuzzyMatch> results = new MyArrayList<>();
        for (int i = 0; i < vocabulary.size(); i++) {
            String word = vocabulary.get(i);
            int d = distance(query, word);
            if (d <= maxDistance) results.add(new FuzzyMatch(word, d));
        }
        results.sort((x, y) -> Integer.compare(x.distance, y.distance));
        return results;
    }
}
