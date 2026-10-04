# TextHack+ — Progress Notes (First 50%)

This covers the "algorithm engine" and "corpus ingestion" layers described in the
abstract, plus a CLI to exercise every query type end to end. Everything is
written from scratch — no `java.util.*` collections, comparators, or `Random`,
and no `java.math.BigInteger`. Only `java.io` (file reading) and `java.lang`
are used outside the custom code.

## What's implemented

**Custom data structures** (`texthack.datastructures`)
- `MyArrayList` — dynamic array with resizing + a from-scratch quicksort
- `MyLinkedList` — singly linked list (backs the hash map's buckets)
- `MyHashMap` — separate-chaining hash map
- `MyStack`, `MyQueue` — used by graph algorithms
- `MyTrie` — prefix tree over the corpus vocabulary

**Query algorithm families** (`texthack.algorithms`)
- `pattern.KMPMatcher` — exact pattern matching (Knuth-Morris-Pratt, O(n+m))
- `pattern.EditDistance` — Levenshtein DP + fuzzy search over the vocabulary
- `similarity.SuffixArray` — suffix array (prefix-doubling, custom merge sort) + Kasai's LCP
- `similarity.DocumentSimilarity` — longest-common-substring-based similarity score
- `graph.Graph` + `graph.MaxFlow` — Edmonds-Karp max-flow for citation-flow analysis
- `scheduling.JobScheduler` — job-sequencing-with-deadlines: polynomial greedy **and**
  an exponential brute-force solver, side by side, to literally show the
  "NP-hard-style" cost the abstract references
- `primality.MillerRabin` — probabilistic primality test with a hand-rolled
  PRNG and overflow-safe modular multiplication

**Corpus + query layer** (`texthack.corpus`, `texthack.query`)
- `CorpusIngestor` — reads `.txt` files from a folder into `Document` objects, tokenizes text
- `QueryService` — builds the vocabulary trie + inverted index, dispatches
  each query type to its algorithm family

**CLI** (`texthack.Main`) — menu-driven demo of all six query types against
a small sample corpus in `sample_corpus/`.

## How to run

```bash
javac -d bin $(find src -name "*.java")
java -cp bin texthack.Main
```

Verified compiling and running cleanly on OpenJDK 21 (menu tested end to end:
exact search, fuzzy search, similarity, max-flow, scheduling, primality all
produce correct results — brute-force and greedy scheduling agree on the
sample job set, and max-flow/primality outputs were hand-checked).

## What's left for the remaining ~50%

1. **Multi-lingual support** — Indian-language tokenization (the tokenizer
   currently assumes Latin-script `Character.isLetterOrDigit`, which does
   handle Unicode letters, but needs testing/tuning against real Indic-script
   Wikipedia dumps — stemming/normalization is not yet handled).
2. **Real corpus integration** — a downloader/parser for Indian-language
   Wikipedia dumps (or a subset), replacing the toy `sample_corpus/` folder.
3. **Domain search mode** (medicine/domain applications) — a filtering or
   weighting layer over the inverted index for domain-specific vocab.
4. **Benchmarking harness** — timing/memory comparisons of the custom data
   structures vs. `java.util` equivalents, to back up the "why custom
   structures matter" claim in the abstract, plus complexity write-ups.
5. **UI** — you said CLI is fine for now, but a Swing GUI (or simple web
   front end) is worth adding if the final report wants screenshots.
6. **Report writing** — methodology, complexity analysis, and results
   sections tying each module back to its theoretical algorithm.
7. **Edge-case hardening & unit tests** — e.g. JobScheduler's brute force is
   O(2^n) and will only be usable for demos with small n; document that
   limit explicitly in the report, and add a JUnit-free test harness
   (custom asserts, to stay consistent with the "from scratch" theme) for
   each algorithm.

Happy to tackle any of these next — just say which one.
