package texthack;

import texthack.algorithms.graph.Graph;
import texthack.algorithms.graph.MaxFlow;
import texthack.algorithms.pattern.EditDistance;
import texthack.algorithms.primality.MillerRabin;
import texthack.algorithms.scheduling.JobScheduler;
import texthack.corpus.CorpusIngestor;
import texthack.corpus.Document;
import texthack.datastructures.MyArrayList;
import texthack.datastructures.MyHashMap;
import texthack.query.QueryService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * TextHack+ CLI - demonstrates every query family from the abstract:
 * exact pattern matching, fuzzy search, document similarity, citation-flow
 * (max-flow), NP-hard-style task scheduling, and probabilistic primality
 * testing. Each menu option prints which algorithm family serves it,
 * matching the abstract's "educational tool" framing.
 */
public class Main {

    private static final QueryService queryService = new QueryService();

    public static void main(String[] args) throws IOException {
        loadSampleCorpus();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        boolean running = true;
        while (running) {
            printMenu();
            String choice = in.readLine();
            if (choice == null) break;
            switch (choice.trim()) {
                case "1": exactPatternDemo(in); break;
                case "2": fuzzySearchDemo(in); break;
                case "3": documentSimilarityDemo(in); break;
                case "4": citationFlowDemo(); break;
                case "5": taskSchedulingDemo(); break;
                case "6": primalityDemo(in); break;
                case "0": running = false; break;
                default: System.out.println("Unrecognized option.\n");
            }
        }
        System.out.println("Goodbye.");
    }

    private static void printMenu() {
        System.out.println("=========================================");
        System.out.println(" TextHack+ Intelligent Language Query System");
        System.out.println("=========================================");
        System.out.println("1. Exact pattern matching   (KMP)");
        System.out.println("2. Fuzzy search              (Edit distance)");
        System.out.println("3. Document similarity       (Suffix array)");
        System.out.println("4. Citation-flow analysis    (Max-flow / Edmonds-Karp)");
        System.out.println("5. Task scheduling demo      (NP-hard illustration)");
        System.out.println("6. Primality test            (Miller-Rabin)");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private static void loadSampleCorpus() throws IOException {
        MyArrayList<Document> docs = CorpusIngestor.ingestFolder("sample_corpus", "en");
        queryService.loadDocuments(docs);
        System.out.println("Loaded " + docs.size() + " sample document(s), vocabulary size = "
                + queryService.vocabularySize() + "\n");
    }

    private static void exactPatternDemo(BufferedReader in) throws IOException {
        System.out.print("Enter a pattern to search for: ");
        String pattern = in.readLine();
        MyHashMap<String, MyArrayList<Integer>> results = queryService.exactSearch(pattern);
        MyArrayList<String> docIds = results.keys();
        if (docIds.size() == 0) {
            System.out.println("No matches found.\n");
            return;
        }
        for (int i = 0; i < docIds.size(); i++) {
            String docId = docIds.get(i);
            MyArrayList<Integer> positions = results.get(docId);
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < positions.size(); j++) {
                if (j > 0) sb.append(", ");
                sb.append(positions.get(j));
            }
            System.out.println(docId + " -> positions [" + sb + "]");
        }
        System.out.println();
    }

    private static void fuzzySearchDemo(BufferedReader in) throws IOException {
        System.out.print("Enter a term for fuzzy search: ");
        String term = in.readLine();
        System.out.print("Max edit distance (e.g. 2): ");
        int maxDist = Integer.parseInt(in.readLine().trim());
        MyArrayList<EditDistance.FuzzyMatch> matches = queryService.fuzzySearch(term, maxDist);
        if (matches.size() == 0) {
            System.out.println("No fuzzy matches found.\n");
            return;
        }
        int shown = Math.min(matches.size(), 15);
        for (int i = 0; i < shown; i++) {
            EditDistance.FuzzyMatch m = matches.get(i);
            System.out.println(m.word + " (distance " + m.distance + ")");
        }
        System.out.println();
    }

    private static void documentSimilarityDemo(BufferedReader in) throws IOException {
        MyArrayList<Document> docs = queryService.allDocuments();
        if (docs.size() < 2) {
            System.out.println("Need at least 2 documents in sample_corpus/ for this demo.\n");
            return;
        }
        System.out.println("Available documents:");
        for (int i = 0; i < docs.size(); i++) System.out.println("  - " + docs.get(i).id);
        System.out.print("First document ID: ");
        String id1 = in.readLine().trim();
        System.out.print("Second document ID: ");
        String id2 = in.readLine().trim();
        double score = queryService.documentSimilarity(id1, id2);
        if (score < 0) System.out.println("One or both document IDs not found.\n");
        else System.out.printf("Similarity score: %.4f (1.0 = identical LCS relative to shorter doc)%n%n", score);
    }

    private static void citationFlowDemo() {
        Graph graph = new Graph();
        // toy citation network: capacity = strength of citation influence
        graph.addEdge("PaperA", "PaperB", 10);
        graph.addEdge("PaperA", "PaperC", 5);
        graph.addEdge("PaperB", "PaperC", 4);
        graph.addEdge("PaperB", "PaperD", 8);
        graph.addEdge("PaperC", "PaperD", 9);
        graph.addEdge("PaperD", "Sink", 10);
        graph.addEdge("PaperC", "Sink", 6);

        MaxFlow.Result result = MaxFlow.computeMaxFlow(graph, "PaperA", "Sink");
        System.out.println("(demo network: PaperA -> ... -> Sink, built-in sample)");
        System.out.println("Maximum citation-influence flow from PaperA to Sink: " + result.maxFlow);
        System.out.println();
    }

    private static void taskSchedulingDemo() {
        MyArrayList<JobScheduler.Job> jobs = new MyArrayList<>();
        jobs.add(new JobScheduler.Job("J1", 2, 100));
        jobs.add(new JobScheduler.Job("J2", 1, 19));
        jobs.add(new JobScheduler.Job("J3", 2, 27));
        jobs.add(new JobScheduler.Job("J4", 1, 25));
        jobs.add(new JobScheduler.Job("J5", 3, 15));

        JobScheduler.ScheduleResult greedy = JobScheduler.greedySchedule(jobs);
        JobScheduler.ScheduleResult brute = JobScheduler.bruteForceSchedule(jobs);

        System.out.println("(demo job set: 5 jobs with deadlines/profits, built-in sample)");
        System.out.print("Greedy schedule: ");
        printJobs(greedy);
        System.out.print("Brute-force (exhaustive, 2^n subsets) schedule: ");
        printJobs(brute);
        System.out.println();
    }

    private static void printJobs(JobScheduler.ScheduleResult result) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.scheduledJobs.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(result.scheduledJobs.get(i).id);
        }
        System.out.println("[" + sb + "] total profit = " + result.totalProfit);
    }

    private static void primalityDemo(BufferedReader in) throws IOException {
        System.out.print("Enter a number to test for primality: ");
        long n = Long.parseLong(in.readLine().trim());
        boolean prime = MillerRabin.isProbablePrime(n);
        System.out.println(n + " is " + (prime ? "probably PRIME" : "COMPOSITE") + " (Miller-Rabin, 20 rounds)\n");
    }
}
