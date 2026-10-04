package com.team7.texthackplus.algorithms.flow;

public class MaxFlowCitationTest {

    public static void main(String[] args) {
        testSimpleGraph();
        testDisconnectedGraph();
        System.out.println("MaxFlowCitation tests passed!");
    }

    private static void testSimpleGraph() {
        int[][] capacities = {
            {0, 16, 13, 0, 0, 0},
            {0, 0, 10, 12, 0, 0},
            {0, 4, 0, 0, 14, 0},
            {0, 0, 9, 0, 0, 20},
            {0, 0, 0, 7, 0, 4},
            {0, 0, 0, 0, 0, 0}
        };

        MaxFlowCitation.SearchResult result = MaxFlowCitation.computeMaxFlow(capacities, 0, 5);
        assert result.getMaxFlow() == 23 : "Expected max flow 23 but got " + result.getMaxFlow();
    }

    private static void testDisconnectedGraph() {
        int[][] capacities = {
            {0, 10, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 10},
            {0, 0, 0, 0}
        };

        MaxFlowCitation.SearchResult result = MaxFlowCitation.computeMaxFlow(capacities, 0, 3);
        assert result.getMaxFlow() == 0 : "Expected max flow 0 but got " + result.getMaxFlow();
    }
}
