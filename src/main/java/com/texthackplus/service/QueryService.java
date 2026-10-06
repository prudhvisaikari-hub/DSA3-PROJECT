package com.texthackplus.service;

import com.texthackplus.algorithms.exact.KMPMatcher;
import com.texthackplus.algorithms.fuzzy.Levenshtein;
import com.texthackplus.algorithms.similarity.SuffixTreeSimilarity;
import com.texthackplus.algorithms.flow.MaxFlowCitation;
import com.texthackplus.algorithms.scheduling.JobScheduler;
import com.texthackplus.algorithms.math.MillerRabinPrimality;

/**
 * Unified query dispatcher for the TextHack+ engine.
 * Routes requests to the appropriate Layer 2 algorithm and wraps the response
 * in a uniform {@link QueryResult} with benchmarking metadata.
 */
public class QueryService {

    /** Query types supported by the service. */
    public enum QueryType {
        EXACT_MATCH,
        FUZZY_MATCH,
        SIMILARITY,
        CITATION_FLOW,
        JOB_SCHEDULING,
        PRIMALITY_TEST
    }

    /**
     * Dispatches the query based on the specified type.
     * 
     * @param type   The type of algorithm to execute.
     * @param params An array of Object parameters required by the specific algorithm.
     *               The caller is responsible for passing the correct types.
     * @return A {@link QueryResult} containing the result and metadata.
     */
    public static QueryResult executeQuery(QueryType type, Object... params) {
        switch (type) {
            case EXACT_MATCH: {
                // params: String text, String pattern
                String text = (String) params[0];
                String pattern = (String) params[1];
                KMPMatcher.SearchResult res = KMPMatcher.search(text, pattern);
                return new QueryResult(
                        res.getIndex(),
                        res.getAlgorithm(),
                        res.getComplexity(),
                        res.getTimeNs(),
                        res.getComparisons()
                );
            }
            case FUZZY_MATCH: {
                // params: String text1, String text2, (optional) int maxDistance
                String text1 = (String) params[0];
                String text2 = (String) params[1];
                Levenshtein.SearchResult res;
                if (params.length > 2) {
                    int maxDist = (Integer) params[2];
                    res = Levenshtein.computeDistance(text1, text2, maxDist);
                } else {
                    res = Levenshtein.computeDistance(text1, text2);
                }
                return new QueryResult(
                        res.getDistance(),
                        res.getAlgorithm(),
                        res.getComplexity(),
                        res.getTimeNs(),
                        res.getComparisons()
                );
            }
            case SIMILARITY: {
                // params: String text1, String text2
                String text1 = (String) params[0];
                String text2 = (String) params[1];
                SuffixTreeSimilarity.SearchResult res = SuffixTreeSimilarity.longestCommonSubstring(text1, text2);
                return new QueryResult(
                        res.getSubstring(), // We can also return a custom object, but returning the string is fine
                        res.getAlgorithm(),
                        res.getComplexity(),
                        res.getTimeNs(),
                        res.getComparisons()
                );
            }
            case CITATION_FLOW: {
                // params: int[][] capacities, int source, int sink
                int[][] capacities = (int[][]) params[0];
                int source = (Integer) params[1];
                int sink = (Integer) params[2];
                MaxFlowCitation.SearchResult res = MaxFlowCitation.computeMaxFlow(capacities, source, sink);
                return new QueryResult(
                        res.getMaxFlow(),
                        res.getAlgorithm(),
                        res.getComplexity(),
                        res.getTimeNs(),
                        res.getComparisons() // operations
                );
            }
            case JOB_SCHEDULING: {
                // params: JobScheduler.Job[] jobs
                JobScheduler.Job[] jobs = (JobScheduler.Job[]) params[0];
                JobScheduler.SearchResult res = JobScheduler.schedule(jobs);
                return new QueryResult(
                        res.getOrder(),
                        res.getAlgorithm(),
                        res.getComplexity(),
                        res.getTimeNs(),
                        res.getComparisons() // operations
                );
            }
            case PRIMALITY_TEST: {
                // params: long n, int iterations
                long n = (Long) params[0];
                int iterations = (Integer) params[1];
                MillerRabinPrimality.SearchResult res = MillerRabinPrimality.isPrime(n, iterations);
                return new QueryResult(
                        res.isPrime(),
                        res.getAlgorithm(),
                        res.getComplexity(),
                        res.getTimeNs(),
                        res.getComparisons()
                );
            }
            default:
                throw new IllegalArgumentException("Unknown QueryType: " + type);
        }
    }
}
