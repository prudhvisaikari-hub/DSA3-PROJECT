# TextHack+ Complexity Analysis

This document provides a precise theoretical Big-O complexity analysis for the custom data structures and algorithms *exactly as they are implemented* in the TextHack+ project. (Note: `n` generally represents the size of the input collection or string length, `V` is vertices, and `E` is edges).

## 1. Custom Data Structures

| Data Structure | Operation | Time Complexity | Space Complexity | Notes |
|---|---|---|---|---|
| **DynamicArray** | Append | Amortized `O(1)` | `O(n)` | Resizes by a factor of 2 when full. |
| **DynamicArray** | Get / Set | `O(1)` | - | Direct array index access. |
| **SinglyLinkedList** | Add First | `O(1)` | `O(n)` | Prepending is constant time. |
| **SinglyLinkedList** | Get (by index) | `O(n)` | - | Requires sequential traversal. |
| **Queue** (Linked) | Enqueue / Dequeue | `O(1)` | `O(n)` | Implemented using front and rear pointers. |
| **Stack** (Linked) | Push / Pop | `O(1)` | `O(n)` | Operations performed exclusively at the head. |
| **CustomHashMap** | Insert / Get | Expected `O(1)` | `O(n)` | Uses chaining. Worst-case is `O(n)` if all keys hash to the same bucket (though resizing helps mitigate this). |
| **MaxHeap** | Insert / Extract Max | `O(log n)` | `O(n)` | Maintains heap property using sift-up/sift-down on an underlying `DynamicArray`. |
| **Trie** | Insert / Search | `O(L)` | `O(N * L)` | Where `L` is the word length and `N` is the number of words. Lookups traverse character by character. |
| **AdjListGraph** | Add Edge | `O(1)` | `O(V + E)` | Uses `SinglyLinkedList` for constant time prepending to adjacency lists. |

## 2. Core Algorithms

| Algorithm / Feature | Time Complexity | Space Complexity | Description |
|---|---|---|---|
| **Exact Match**<br>(Knuth-Morris-Pratt) | `O(n + m)` | `O(m)` | Where `n` is text length and `m` is pattern length. Achieved by precomputing an LPS (Longest Prefix Suffix) array of size `m`. |
| **Fuzzy Search**<br>(Levenshtein Distance) | `O(n * m)` | `O(n * m)` | Dynamic programming matrix approach. (Optimized versions can reduce space to `O(min(n, m))`, but full matrix is `O(n * m)`). |
| **Document Similarity**<br>(Suffix Array + LCP) | `O(n^2 log n)` | `O(n)` | Constructs a Suffix Array using string-comparison QuickSort `O(n^2 log n)`. Generates the LCP array via Kasai's algorithm `O(n)`. Scans arrays in `O(n)`. Total time is bottlenecked by the suffix array sorting step. |
| **Citation-Flow Analysis**<br>(Edmonds-Karp) | `O(V * E^2)` | `O(V^2)` | Uses BFS to find augmenting paths in the residual capacity matrix. |
| **Task Scheduling**<br>(Priority Queue / MaxHeap) | `O(k log k)` | `O(k)` | Where `k` is the number of jobs. Inserting all jobs takes `O(k log k)`, and extracting them in order takes `O(k log k)`. |
| **Primality Test**<br>(Miller-Rabin) | `O(i * log^3 n)` | `O(1)` | Where `i` is the number of iterations and `n` is the number tested. Includes modular exponentiation. |
