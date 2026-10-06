package com.texthackplus.service;

import com.texthackplus.algorithms.scheduling.JobScheduler;

/**
 * Plain assert tests for the Layer 3 QueryService dispatcher.
 */
public class QueryServiceTest {

    public static void main(String[] args) {
        testExactMatch();
        testFuzzyMatch();
        testSimilarity();
        testCitationFlow();
        testJobScheduling();
        testPrimalityTest();
        System.out.println("QueryService tests passed!");
    }

    private static void testExactMatch() {
        QueryResult res = QueryService.executeQuery(QueryService.QueryType.EXACT_MATCH, "hello world", "world");
        assert res.getAlgorithm().equals("KMP") : "Wrong algorithm name";
        assert (Integer) res.getAnswer() == 6 : "Expected index 6";
        assert res.getOperations() > 0 : "Operations should be > 0";
    }

    private static void testFuzzyMatch() {
        QueryResult res = QueryService.executeQuery(QueryService.QueryType.FUZZY_MATCH, "kitten", "sitten");
        assert res.getAlgorithm().equals("Levenshtein") : "Wrong algorithm name";
        assert (Integer) res.getAnswer() == 1 : "Expected distance 1";
    }

    private static void testSimilarity() {
        QueryResult res = QueryService.executeQuery(QueryService.QueryType.SIMILARITY, "ABAB", "BABA");
        assert res.getAlgorithm().equals("Suffix Array + LCP") : "Wrong algorithm name";
        String answer = (String) res.getAnswer();
        assert answer.equals("BAB") || answer.equals("ABA") : "Expected BAB or ABA";
    }

    private static void testCitationFlow() {
        int[][] capacities = {
            {0, 10, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 10},
            {0, 0, 0, 0}
        };
        QueryResult res = QueryService.executeQuery(QueryService.QueryType.CITATION_FLOW, capacities, 0, 3);
        assert res.getAlgorithm().equals("Edmonds-Karp Max Flow") : "Wrong algorithm name";
        assert (Integer) res.getAnswer() == 0 : "Expected max flow 0";
    }

    private static void testJobScheduling() {
        JobScheduler.Job[] jobs = new JobScheduler.Job[] {
            new JobScheduler.Job(1, 10, 5)
        };
        QueryResult res = QueryService.executeQuery(QueryService.QueryType.JOB_SCHEDULING, (Object) jobs);
        assert res.getAlgorithm().equals("Priority Job Scheduler") : "Wrong algorithm name";
        assert res.getAnswer() != null : "Expected non-null order";
    }

    private static void testPrimalityTest() {
        QueryResult res = QueryService.executeQuery(QueryService.QueryType.PRIMALITY_TEST, 17L, 5);
        assert res.getAlgorithm().equals("Miller-Rabin Primality Test") : "Wrong algorithm name";
        assert (Boolean) res.getAnswer() == true : "Expected true for 17";
    }
}
