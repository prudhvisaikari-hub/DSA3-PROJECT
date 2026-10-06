# TextHack+

TextHack+ is an intelligent language query system built for a Data Structures & Algorithms course project. It implements a complete, layered architecture from scratch, strictly adhering to the constraint of **not using any built-in `java.util.*` collections or algorithm classes**.

## Architecture

The system is organized into four distinct modules/layers:

### 1. Data Structures Layer (`com.texthackplus.structures`)
Custom-built, dependency-free foundational collections, including:
- **DynamicArray**: A resizable array (similar to `ArrayList`).
- **SinglyLinkedList**: Used for graph adjacency lists.
- **CustomHashMap**: Hash table with chaining for O(1) expected lookups.
- **Queue / Stack**: Standard FIFO and LIFO structures.
- **MaxHeap**: Priority queue for task scheduling.
- **AdjListGraph**: Directed graph representation for flow networks.
- **Trie**: Prefix tree used in the ingestion layer.

### 2. Ingestion Layer (`com.texthackplus.ingestion`)
Handles the loading and indexing of text corpora. Features include:
- Support for multiple languages (English, Hindi, and Medical datasets).
- **DocumentStore**: Organizes loaded documents.
- **Tokenizer**: Processes raw strings into searchable words.
- **CorpusLoader**: Manages file I/O using basic `java.io`.

### 3. Algorithm Engine Layer (`com.texthackplus.algorithms` and `.service`)
Implements core algorithms mapped to specific project requirements:
- **Exact Match (KMPMatcher)**: O(n + m) exact string matching using Knuth-Morris-Pratt.
- **Fuzzy Search (Levenshtein)**: Dynamic programming edit distance algorithm.
- **Document Similarity (SuffixTreeSimilarity)**: Longest common substring extraction using a custom-built Suffix Array and LCP (Longest Common Prefix) array constructed via Kasai's algorithm.
- **Citation-Flow Analysis (MaxFlowCitation)**: Network bottleneck analysis using the Edmonds-Karp max-flow algorithm.
- **Task Scheduling Demo (JobScheduler)**: Priority-based job scheduling using a custom MaxHeap.
- **Primality Test (MillerRabinPrimality)**: Probabilistic prime checking for custom hashing analysis.

The **QueryService** class sits on top of this layer, acting as a unified query dispatcher that routes commands to the appropriate algorithm and uniformly tracks execution time and operation counts.

### 4. User Interface Layer (`com.texthackplus.cli`)
A menu-driven Command Line Interface (`CLI.java`) that allows the user to:
- Select and load active corpora.
- Toggle "Domain Mode" (Medical corpus vs General corpora).
- Execute any of the 6 query algorithms interactively.
- View answers alongside metadata (algorithm used, Big-O complexity, runtime in milliseconds, and operations).

## How to Build and Run

To compile the project strictly without external dependencies (no Maven/JUnit required for the runtime):

1. **Compile all sources** into a `bin` directory:
   ```cmd
   mkdir bin
   cmd /c "dir /b /s src\main\java\*.java src\test\java\*.java > allsources.txt"
   javac -d bin @allsources.txt
   ```

2. **Run the CLI**:
   ```cmd
   java -cp bin com.texthackplus.cli.CLI
   ```

3. **Run the Test Harnesses** (requires the `-ea` flag to enable Java assertions):
   ```cmd
   java -ea -cp bin com.texthackplus.service.QueryServiceTest
   ```
   *(You can replace `QueryServiceTest` with any of the specific test classes in `src/test/java/...` to verify individual components).*
