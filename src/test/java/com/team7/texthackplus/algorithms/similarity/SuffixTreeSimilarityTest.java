package com.team7.texthackplus.algorithms.similarity;

public class SuffixTreeSimilarityTest {

    public static void main(String[] args) {
        testSuffixArrayConstruction();
        testLCPArrayConstruction();
        testLongestCommonSubstring();
        testEmptyString();
        testNoCommonSubstring();
        testFullMatch();
        System.out.println("SuffixTreeSimilarity tests passed!");
    }

    private static void testSuffixArrayConstruction() {
        // String "banana#"
        // Suffixes:
        // 0: banana#
        // 1: anana#
        // 2: nana#
        // 3: ana#
        // 4: na#
        // 5: a#
        // 6: #
        // Sorted:
        // 6: #
        // 5: a#
        // 3: ana#
        // 1: anana#
        // 0: banana#
        // 4: na#
        // 2: nana#
        String s = "banana#";
        int[] sa = SuffixTreeSimilarity.buildSuffixArray(s);
        
        assert sa.length == 7 : "Expected length 7";
        assert sa[0] == 6 : "SA[0] should be 6 (#)";
        assert sa[1] == 5 : "SA[1] should be 5 (a#)";
        assert sa[2] == 3 : "SA[2] should be 3 (ana#)";
        assert sa[3] == 1 : "SA[3] should be 1 (anana#)";
        assert sa[4] == 0 : "SA[4] should be 0 (banana#)";
        assert sa[5] == 4 : "SA[5] should be 4 (na#)";
        assert sa[6] == 2 : "SA[6] should be 2 (nana#)";
    }

    private static void testLCPArrayConstruction() {
        String s = "banana#";
        int[] sa = SuffixTreeSimilarity.buildSuffixArray(s);
        int[] lcp = SuffixTreeSimilarity.buildLCPArray(s, sa);
        
        // LCP of adjacent suffixes in sorted order:
        // SA[0] vs SA[-1] -> lcp[0] = 0
        // SA[1] vs SA[0]  -> a# vs # -> 0
        // SA[2] vs SA[1]  -> ana# vs a# -> 1 (a)
        // SA[3] vs SA[2]  -> anana# vs ana# -> 3 (ana)
        // SA[4] vs SA[3]  -> banana# vs anana# -> 0
        // SA[5] vs SA[4]  -> na# vs banana# -> 0
        // SA[6] vs SA[5]  -> nana# vs na# -> 2 (na)
        
        assert lcp.length == 7 : "Expected length 7";
        assert lcp[0] == 0 : "lcp[0] should be 0";
        assert lcp[1] == 0 : "lcp[1] should be 0";
        assert lcp[2] == 1 : "lcp[2] should be 1";
        assert lcp[3] == 3 : "lcp[3] should be 3";
        assert lcp[4] == 0 : "lcp[4] should be 0";
        assert lcp[5] == 0 : "lcp[5] should be 0";
        assert lcp[6] == 2 : "lcp[6] should be 2";
    }

    private static void testLongestCommonSubstring() {
        String a = "ABAB";
        String b = "BABA";
        SuffixTreeSimilarity.SearchResult result = SuffixTreeSimilarity.longestCommonSubstring(a, b);
        assert result.getSubstring().equals("BAB") || result.getSubstring().equals("ABA") : "Expected 'BAB' or 'ABA' but got " + result.getSubstring();
        assert result.getLength() == 3 : "Expected length 3 but got " + result.getLength();
    }

    private static void testEmptyString() {
        SuffixTreeSimilarity.SearchResult result = SuffixTreeSimilarity.longestCommonSubstring("", "ABC");
        assert result.getSubstring().isEmpty() : "Expected empty string but got " + result.getSubstring();
        assert result.getLength() == 0 : "Expected length 0 but got " + result.getLength();
    }

    private static void testNoCommonSubstring() {
        SuffixTreeSimilarity.SearchResult result = SuffixTreeSimilarity.longestCommonSubstring("ABC", "DEF");
        assert result.getSubstring().isEmpty() : "Expected empty string but got " + result.getSubstring();
        assert result.getLength() == 0 : "Expected length 0 but got " + result.getLength();
    }
    
    private static void testFullMatch() {
        SuffixTreeSimilarity.SearchResult result = SuffixTreeSimilarity.longestCommonSubstring("HELLO", "HELLO");
        assert result.getSubstring().equals("HELLO") : "Expected 'HELLO' but got " + result.getSubstring();
        assert result.getLength() == 5 : "Expected length 5 but got " + result.getLength();
    }
}
