package com.team7.texthackplus.algorithms.fuzzy;

/**
 * Plain assert tests for {@link Levenshtein} fuzzy‑search implementation.
 */
public class LevenshteinTest {
    
    public static void main(String[] args) {
        testIdenticalStrings();
        testSingleInsertion();
        testInsertionDeletion();
        testEarlyStopThreshold();
        System.out.println("Levenshtein tests passed!");
    }

    private static void testIdenticalStrings() {
        Levenshtein.SearchResult res = Levenshtein.computeDistance("hello", "hello");
        assert res.getDistance() == 0 : "Expected 0 but got " + res.getDistance();
        assert res.getComparisons() > 0 : "Comparisons should be > 0";
    }

    private static void testSingleInsertion() {
        Levenshtein.SearchResult res = Levenshtein.computeDistance("kitten", "sitten");
        assert res.getDistance() == 1 : "Expected 1 but got " + res.getDistance();
    }

    private static void testInsertionDeletion() {
        Levenshtein.SearchResult res = Levenshtein.computeDistance("flaw", "lawn");
        assert res.getDistance() == 2 : "Expected 2 but got " + res.getDistance();
    }

    private static void testEarlyStopThreshold() {
        Levenshtein.SearchResult res = Levenshtein.computeDistance("abcdefgh", "xyz", 1);
        assert res.getDistance() > 1 : "Expected > 1 but got " + res.getDistance();
    }
}
