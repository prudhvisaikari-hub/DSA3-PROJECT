package texthack.algorithms.similarity;

/**
 * Document similarity scoring using suffix-array-based longest common
 * substring (LCS). Score is normalized by the shorter document's length,
 * giving a value in [0, 1].
 */
public class DocumentSimilarity {

    public static double similarity(String doc1, String doc2) {
        if (doc1.isEmpty() || doc2.isEmpty()) return 0.0;
        int lcsLen = SuffixArray.longestCommonSubstringLength(doc1, doc2);
        int shorter = Math.min(doc1.length(), doc2.length());
        return (double) lcsLen / shorter;
    }

    public static int longestCommonSubstring(String doc1, String doc2) {
        return SuffixArray.longestCommonSubstringLength(doc1, doc2);
    }
}
