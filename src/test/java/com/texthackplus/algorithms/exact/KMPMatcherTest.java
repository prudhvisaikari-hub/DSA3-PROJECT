package com.texthackplus.algorithms.exact;

import com.texthackplus.algorithms.exact.KMPMatcher.SearchResult;

/**
 * Simple test harness for KMPMatcher using built‑in Java assertions.
 * Run with VM option -ea to enable assertions.
 */
public class KMPMatcherTest {
    public static void main(String[] args) {
        // Test 1 – pattern found
        String text = "abxabcabcaby";
        String pattern = "abcaby";
        SearchResult res = KMPMatcher.search(text, pattern);
        assert res.getIndex() == 6 : "Expected index 6, got " + res.getIndex();
        assert res.getComparisons() > 0;

        // Test 2 – pattern not found
        String text2 = "abcdefgh";
        String pattern2 = "xyz";
        SearchResult res2 = KMPMatcher.search(text2, pattern2);
        assert res2.getIndex() == -1 : "Expected -1, got " + res2.getIndex();

        // Test 3 – empty pattern
        String text3 = "anytext";
        String pattern3 = "";
        SearchResult res3 = KMPMatcher.search(text3, pattern3);
        assert res3.getIndex() == 0 : "Empty pattern should match at 0";

        System.out.println("KMPMatcherTest passed.");
    }
}
