package texthack.algorithms.similarity;

/**
 * Suffix array + Kasai's LCP algorithm, built from scratch.
 * O(n log^2 n) construction via prefix-doubling, O(n) LCP via Kasai.
 * This underlies document-similarity queries (longest common substring
 * between two documents, repeated-phrase detection, etc.).
 */
public class SuffixArray {

    private final String text;
    private final int n;
    private int[] suffixArray;
    private int[] lcpArray;

    public SuffixArray(String text) {
        // sentinel char lower than all others to simplify comparisons
        this.text = text + '\u0000';
        this.n = this.text.length();
        build();
        buildLCP();
    }

    // Compares two suffix indices by (rank[i], rank[i+k]) without java.util.Comparator.
    private int compareByRank(int a, int b, int[] rank, int k) {
        if (rank[a] != rank[b]) return Integer.compare(rank[a], rank[b]);
        int ra = a + k < n ? rank[a + k] : -1;
        int rb = b + k < n ? rank[b + k] : -1;
        return Integer.compare(ra, rb);
    }

    // From-scratch merge sort on the suffix-index array, ordered by compareByRank.
    private void mergeSort(int[] sa, int[] rank, int k, int lo, int hi, int[] buffer) {
        if (hi - lo <= 1) return;
        int mid = (lo + hi) / 2;
        mergeSort(sa, rank, k, lo, mid, buffer);
        mergeSort(sa, rank, k, mid, hi, buffer);
        int i = lo, j = mid, t = lo;
        while (i < mid && j < hi) {
            if (compareByRank(sa[i], sa[j], rank, k) <= 0) buffer[t++] = sa[i++];
            else buffer[t++] = sa[j++];
        }
        while (i < mid) buffer[t++] = sa[i++];
        while (j < hi) buffer[t++] = sa[j++];
        System.arraycopy(buffer, lo, sa, lo, hi - lo);
    }

    private void build() {
        int[] sa = new int[n];
        int[] rank = new int[n];
        int[] tmp = new int[n];
        int[] buffer = new int[n];
        for (int i = 0; i < n; i++) {
            sa[i] = i;
            rank[i] = text.charAt(i);
        }

        for (int k = 1; k < n; k <<= 1) {
            mergeSort(sa, rank, k, 0, n, buffer);

            tmp[sa[0]] = 0;
            for (int i = 1; i < n; i++) {
                tmp[sa[i]] = tmp[sa[i - 1]] + (compareByRank(sa[i - 1], sa[i], rank, k) < 0 ? 1 : 0);
            }
            System.arraycopy(tmp, 0, rank, 0, n);
            if (rank[sa[n - 1]] == n - 1) break;
        }

        suffixArray = sa;
    }

    private void buildLCP() {
        // Kasai's algorithm
        int[] rankOfSuffix = new int[n];
        for (int i = 0; i < n; i++) rankOfSuffix[suffixArray[i]] = i;
        lcpArray = new int[n];
        int h = 0;
        for (int i = 0; i < n; i++) {
            if (rankOfSuffix[i] > 0) {
                int j = suffixArray[rankOfSuffix[i] - 1];
                while (i + h < n && j + h < n && text.charAt(i + h) == text.charAt(j + h)) h++;
                lcpArray[rankOfSuffix[i]] = h;
                if (h > 0) h--;
            } else {
                h = 0;
            }
        }
    }

    public int[] getSuffixArray() { return suffixArray; }
    public int[] getLcpArray() { return lcpArray; }
    public String getText() { return text; }

    /** Length of the longest common substring between this text and other. */
    public static int longestCommonSubstringLength(String s1, String s2) {
        String combined = s1 + '\u0001' + s2;
        SuffixArray sa = new SuffixArray(combined);
        int[] arr = sa.getSuffixArray();
        int[] lcp = sa.getLcpArray();
        int best = 0;
        for (int i = 1; i < arr.length; i++) {
            boolean prevInFirst = arr[i - 1] < s1.length();
            boolean curInFirst = arr[i] < s1.length();
            // an LCS candidate must straddle the boundary between the two strings
            if (prevInFirst != curInFirst) best = Math.max(best, lcp[i]);
        }
        return best;
    }
}
