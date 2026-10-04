package com.team7.texthackplus.cli;

import com.team7.texthackplus.service.QueryService;
import com.team7.texthackplus.service.QueryResult;
import com.team7.texthackplus.algorithms.exact.KMPMatcher;
import com.team7.texthackplus.algorithms.fuzzy.Levenshtein;
import com.team7.texthackplus.algorithms.similarity.SuffixTreeSimilarity;
import com.team7.texthackplus.algorithms.scheduling.JobScheduler;
import com.team7.texthackplus.algorithms.math.MillerRabinPrimality;
import com.team7.texthackplus.ingestion.CorpusLoader;
import com.team7.texthackplus.ingestion.DocumentStore;
import com.team7.texthackplus.structures.array.DynamicArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CLI {
    private static boolean domainMode = false;
    private static String activeCorpus = "english.txt";
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("   Welcome to TextHack+ Engine CLI    ");
        System.out.println("======================================");

        boolean running = true;
        while (running) {
            printMenu();
            try {
                String choiceStr = reader.readLine();
                if (choiceStr == null) break;
                choiceStr = choiceStr.trim();
                if (choiceStr.isEmpty()) continue;

                int choice = Integer.parseInt(choiceStr);
                switch (choice) {
                    case 1:
                        selectCorpus();
                        break;
                    case 2:
                        toggleDomainMode();
                        break;
                    case 3:
                        handleExactMatch();
                        break;
                    case 4:
                        handleFuzzyMatch();
                        break;
                    case 5:
                        handleSimilarity();
                        break;
                    case 6:
                        handleCitationFlow();
                        break;
                    case 7:
                        handleJobScheduling();
                        break;
                    case 8:
                        handlePrimalityTest();
                        break;
                    case 9:
                        System.out.println("Exiting... Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (IOException e) {
                System.out.println("Error reading input: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("Current Corpus: " + activeCorpus + " | Domain Mode: " + (domainMode ? "ON (Medical)" : "OFF (General)"));
        System.out.println("1. Select Corpus");
        System.out.println("2. Toggle Domain Mode");
        System.out.println("3. Exact Match (KMP Algorithm on Corpus / Text)");
        System.out.println("4. Fuzzy Match (Levenshtein Spell-Checker / Distance)");
        System.out.println("5. Document Similarity (Suffix Array + LCP Plagiarism Analysis)");
        System.out.println("6. Citation-Flow Analysis (Edmonds-Karp Max Flow)");
        System.out.println("7. Task Scheduling Demo (Priority Queue / MaxHeap)");
        System.out.println("8. Primality Test & Hash Sizing (Miller-Rabin)");
        System.out.println("9. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void selectCorpus() throws IOException {
        if (domainMode) {
            System.out.println("Domain mode is ON. Only 'medical.txt' is active.");
            activeCorpus = "medical.txt";
            return;
        }
        System.out.println("Select a corpus:");
        System.out.println("1. English (english.txt)");
        System.out.println("2. Hindi (hindi.txt)");
        System.out.print("Choice: ");
        String c = reader.readLine();
        if ("1".equals(c)) {
            activeCorpus = "english.txt";
            System.out.println("Selected english.txt");
        } else if ("2".equals(c)) {
            activeCorpus = "hindi.txt";
            System.out.println("Selected hindi.txt");
        } else {
            System.out.println("Invalid choice. Kept " + activeCorpus);
        }
    }

    private static void toggleDomainMode() {
        domainMode = !domainMode;
        if (domainMode) {
            activeCorpus = "medical.txt";
            System.out.println("Domain Mode enabled. Switched to medical.txt.");
        } else {
            activeCorpus = "english.txt";
            System.out.println("Domain Mode disabled. Switched to english.txt.");
        }
    }

    private static void printResult(QueryResult result) {
        System.out.println("---- Result ----");
        System.out.println("Answer:       " + result.getAnswer());
        System.out.println("Algorithm:    " + result.getAlgorithm());
        System.out.println("Complexity:   " + result.getComplexity());
        System.out.println("Time (ms):    " + result.getTimeMs());
        System.out.println("Operations:   " + result.getOperations());
        System.out.println("----------------");
    }

    private static String loadActiveCorpusContent() {
        try {
            Path p = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "corpora", activeCorpus);
            if (!Files.exists(p)) {
                return "";
            }
            return Files.readString(p, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }

    private static void handleExactMatch() throws IOException {
        System.out.println("--- Exact Match (KMP) ---");
        System.out.println("1. Search pattern across active Corpus file (" + activeCorpus + ")");
        System.out.println("2. Search pattern in custom string input");
        System.out.print("Choice [1/2]: ");
        String mode = reader.readLine().trim();

        String text;
        if ("1".equals(mode)) {
            text = loadActiveCorpusContent();
            if (text.isEmpty()) {
                System.out.println("Error: Could not read active corpus file (" + activeCorpus + ").");
                return;
            }
            System.out.println("Loaded corpus '" + activeCorpus + "' (" + text.length() + " characters).");
        } else {
            System.out.print("Enter text to search in: ");
            text = reader.readLine();
        }

        System.out.print("Enter pattern to search for: ");
        String pattern = reader.readLine();

        // Perform unified query execution
        QueryResult res = QueryService.executeQuery(QueryService.QueryType.EXACT_MATCH, text, pattern);

        // Also run multi-match search to find all occurrences in corpus mode
        DynamicArray<Integer> allMatches = KMPMatcher.searchAll(text, pattern);

        System.out.println("---- KMP Analysis Result ----");
        System.out.println("Total Matches Found: " + allMatches.size());
        if (allMatches.size() > 0) {
            System.out.print("Match Indices: ");
            for (int i = 0; i < Math.min(allMatches.size(), 10); i++) {
                System.out.print(allMatches.get(i) + " ");
            }
            if (allMatches.size() > 10) System.out.print("... (" + (allMatches.size() - 10) + " more)");
            System.out.println();
        }
        System.out.println("First Match Index:   " + res.getAnswer());
        System.out.println("Algorithm:           " + res.getAlgorithm());
        System.out.println("Complexity:          " + res.getComplexity());
        System.out.println("Time (ms):           " + res.getTimeMs());
        System.out.println("Operations:          " + res.getOperations());
        System.out.println("-----------------------------");
    }

    private static void handleFuzzyMatch() throws IOException {
        System.out.println("--- Fuzzy Match (Levenshtein Distance) ---");
        System.out.println("1. Vocabulary Spell-Checker across Corpus");
        System.out.println("2. Compute Edit Distance between two custom strings");
        System.out.print("Choice [1/2]: ");
        String mode = reader.readLine().trim();

        if ("1".equals(mode)) {
            System.out.print("Enter query word for spell-check: ");
            String queryWord = reader.readLine().trim();
            if (queryWord.isEmpty()) return;

            CorpusLoader loader = new CorpusLoader();
            try {
                loader.loadAll();
            } catch (IOException e) {
                System.out.println("Error loading corpus for spell-checker: " + e.getMessage());
                return;
            }

            DocumentStore store = loader.getStore();
            DynamicArray<String> tokens = store.getAllTokens();
            System.out.println("Searching across indexed vocabulary (" + tokens.size() + " unique words)...");

            long start = System.nanoTime();
            int totalOps = 0;
            int matchesFound = 0;

            System.out.println("\n---- Spell Check Suggestions (Edit Distance <= 2) ----");
            for (int i = 0; i < tokens.size(); i++) {
                String token = tokens.get(i);
                Levenshtein.SearchResult res = Levenshtein.computeDistance(token, queryWord, 2);
                totalOps += res.getComparisons();
                if (res.getDistance() <= 2) {
                    matchesFound++;
                    DynamicArray<Integer> docIds = store.getPostingList(token);
                    System.out.println("  • " + token + " (Edit Distance: " + res.getDistance() + ", Found in " + docIds.size() + " doc(s))");
                }
            }
            long end = System.nanoTime();
            double timeMs = (end - start) / 1_000_000.0;

            System.out.println("-----------------------------------------------------");
            System.out.println("Matches Found:  " + matchesFound);
            System.out.println("Algorithm:      Levenshtein Vocabulary Search");
            System.out.println("Complexity:     O(V * n * m)");
            System.out.println("Time (ms):      " + String.format("%.3f", timeMs));
            System.out.println("Operations:     " + totalOps);
            System.out.println("-----------------------------------------------------");
        } else {
            System.out.print("Enter first string: ");
            String s1 = reader.readLine();
            System.out.print("Enter second string: ");
            String s2 = reader.readLine();
            QueryResult res = QueryService.executeQuery(QueryService.QueryType.FUZZY_MATCH, s1, s2);
            printResult(res);
        }
    }

    private static void handleSimilarity() throws IOException {
        System.out.println("--- Document Similarity (Suffix Array + LCP) ---");
        System.out.println("1. Compare Corpus Files (english.txt vs medical.txt)");
        System.out.println("2. Compare two custom text strings");
        System.out.print("Choice [1/2]: ");
        String mode = reader.readLine().trim();

        if ("1".equals(mode)) {
            Path p1 = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "corpora", "english.txt");
            Path p2 = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "corpora", "medical.txt");
            if (!Files.exists(p1) || !Files.exists(p2)) {
                System.out.println("Corpus files not found.");
                return;
            }

            String text1 = Files.readString(p1, StandardCharsets.UTF_8);
            String text2 = Files.readString(p2, StandardCharsets.UTF_8);

            System.out.println("Comparing 'english.txt' (" + text1.length() + " chars) vs 'medical.txt' (" + text2.length() + " chars)...");
            QueryResult res = QueryService.executeQuery(QueryService.QueryType.SIMILARITY, text1, text2);

            String lcs = (String) res.getAnswer();
            System.out.println("---- Document Similarity Analysis ----");
            System.out.println("Longest Common Substring: \"" + lcs + "\"");
            System.out.println("Common Substring Length:  " + lcs.length());
            System.out.println("Algorithm:                " + res.getAlgorithm());
            System.out.println("Complexity:               " + res.getComplexity());
            System.out.println("Time (ms):                " + res.getTimeMs());
            System.out.println("Operations:               " + res.getOperations());
            System.out.println("--------------------------------------");
        } else {
            System.out.print("Enter first string: ");
            String s1 = reader.readLine();
            System.out.print("Enter second string: ");
            String s2 = reader.readLine();
            QueryResult res = QueryService.executeQuery(QueryService.QueryType.SIMILARITY, s1, s2);
            printResult(res);
        }
    }

    private static void handleCitationFlow() throws IOException {
        System.out.println("--- Citation-Flow Analysis (Edmonds-Karp Max Flow) ---");
        System.out.println("1. Predefined 6-Node Paper Citation Network");
        System.out.println("2. Custom Citation Network Matrix Input");
        System.out.print("Choice [1/2]: ");
        String mode = reader.readLine().trim();

        int[][] capacities;
        int source = 0;
        int sink = 5;

        if ("2".equals(mode)) {
            System.out.print("Enter number of paper nodes (e.g., 4): ");
            int n = Integer.parseInt(reader.readLine().trim());
            capacities = new int[n][n];
            System.out.println("Enter capacity matrix row by row (space-separated integers):");
            for (int i = 0; i < n; i++) {
                System.out.print("Row " + i + ": ");
                String[] parts = reader.readLine().trim().split("\\s+");
                for (int j = 0; j < Math.min(n, parts.length); j++) {
                    capacities[i][j] = Integer.parseInt(parts[j]);
                }
            }
            System.out.print("Enter Source Node (0 to " + (n-1) + "): ");
            source = Integer.parseInt(reader.readLine().trim());
            System.out.print("Enter Sink Node (0 to " + (n-1) + "): ");
            sink = Integer.parseInt(reader.readLine().trim());
        } else {
            System.out.println("Using predefined paper citation network (6 papers, Paper 0 -> Paper 5).");
            capacities = new int[][]{
                {0, 16, 13, 0, 0, 0},
                {0, 0, 10, 12, 0, 0},
                {0, 4, 0, 0, 14, 0},
                {0, 0, 9, 0, 0, 20},
                {0, 0, 0, 7, 0, 4},
                {0, 0, 0, 0, 0, 0}
            };
        }

        QueryResult res = QueryService.executeQuery(QueryService.QueryType.CITATION_FLOW, capacities, source, sink);
        printResult(res);
    }

    private static void handleJobScheduling() throws IOException {
        System.out.println("--- Task Scheduling Engine (MaxHeap Priority Queue) ---");
        System.out.println("1. Default Predefined Job Set");
        System.out.println("2. Enter Custom Job Queue");
        System.out.print("Choice [1/2]: ");
        String mode = reader.readLine().trim();

        JobScheduler.Job[] jobs;
        if ("2".equals(mode)) {
            System.out.print("Enter number of batch jobs to schedule: ");
            int count = Integer.parseInt(reader.readLine().trim());
            jobs = new JobScheduler.Job[count];
            for (int i = 0; i < count; i++) {
                System.out.println("Job #" + (i + 1) + ":");
                System.out.print("  Priority (higher = more urgent): ");
                int prio = Integer.parseInt(reader.readLine().trim());
                System.out.print("  Duration (ms): ");
                int dur = Integer.parseInt(reader.readLine().trim());
                jobs[i] = new JobScheduler.Job(i + 1, prio, dur);
            }
        } else {
            System.out.println("Using predefined batch text processing jobs.");
            jobs = new JobScheduler.Job[] {
                new JobScheduler.Job(1, 10, 5),
                new JobScheduler.Job(2, 20, 2),
                new JobScheduler.Job(3, 10, 2),
                new JobScheduler.Job(4, 5, 10)
            };
        }

        QueryResult res = QueryService.executeQuery(QueryService.QueryType.JOB_SCHEDULING, (Object) jobs);
        
        System.out.println("---- Scheduling Result ----");
        System.out.print("Execution Job Order: ");
        com.team7.texthackplus.structures.array.DynamicArray order = (com.team7.texthackplus.structures.array.DynamicArray) res.getAnswer();
        for (int i = 0; i < order.size(); i++) {
            System.out.print(order.get(i) + " ");
        }
        System.out.println();
        System.out.println("Algorithm:           " + res.getAlgorithm());
        System.out.println("Complexity:          " + res.getComplexity());
        System.out.println("Time (ms):           " + res.getTimeMs());
        System.out.println("Operations:          " + res.getOperations());
        System.out.println("---------------------------");
    }

    private static void handlePrimalityTest() throws IOException {
        System.out.println("--- Primality Test & Hash Table Sizing (Miller-Rabin) ---");
        System.out.println("1. Test single number for primality");
        System.out.println("2. Compute Optimal Next Prime Capacity for CustomHashMap");
        System.out.print("Choice [1/2]: ");
        String mode = reader.readLine().trim();

        if ("2".equals(mode)) {
            System.out.print("Enter expected number of keys (e.g., 500): ");
            try {
                long keys = Long.parseLong(reader.readLine().trim());
                long target = keys * 2 + 1; // 50% load factor threshold
                long candidate = target % 2 == 0 ? target + 1 : target;

                long start = System.nanoTime();
                while (true) {
                    QueryResult res = QueryService.executeQuery(QueryService.QueryType.PRIMALITY_TEST, candidate, 5);
                    if ((Boolean) res.getAnswer()) {
                        break;
                    }
                    candidate += 2;
                }
                long end = System.nanoTime();

                System.out.println("---- Hash Table Capacity Recommendation ----");
                System.out.println("Expected Key Count:     " + keys);
                System.out.println("Optimal Prime Capacity: " + candidate + " buckets");
                System.out.println("Load Factor Target:     < 50% (uniform probing)");
                System.out.println("Algorithm:              Miller-Rabin Primality Test");
                System.out.println("Computation Time (ms):  " + String.format("%.3f", (end - start) / 1_000_000.0));
                System.out.println("---------------------------------------------");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
            }
        } else {
            System.out.print("Enter a number to test for primality: ");
            try {
                long n = Long.parseLong(reader.readLine().trim());
                QueryResult res = QueryService.executeQuery(QueryService.QueryType.PRIMALITY_TEST, n, 5);
                printResult(res);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
            }
        }
    }
}

