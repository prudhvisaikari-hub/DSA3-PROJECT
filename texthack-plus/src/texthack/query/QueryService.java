package texthack.query;

import texthack.algorithms.pattern.EditDistance;
import texthack.algorithms.pattern.KMPMatcher;
import texthack.algorithms.similarity.DocumentSimilarity;
import texthack.corpus.CorpusIngestor;
import texthack.corpus.Document;
import texthack.datastructures.MyArrayList;
import texthack.datastructures.MyHashMap;
import texthack.datastructures.MyTrie;

/**
 * Query service layer: sits above the algorithm engine and below the UI.
 * Holds the ingested corpus, a vocabulary trie (for fuzzy search
 * candidates), and dispatches each query type to its algorithm family,
 * as described in the abstract's layered architecture.
 */
public class QueryService {

    private final MyArrayList<Document> documents = new MyArrayList<>();
    private final MyTrie vocabularyTrie = new MyTrie();
    private final MyArrayList<String> vocabulary = new MyArrayList<>();
    // inverted index: word -> list of document IDs containing it
    private final MyHashMap<String, MyArrayList<String>> invertedIndex = new MyHashMap<>();

    public void loadDocuments(MyArrayList<Document> docs) {
        for (int i = 0; i < docs.size(); i++) {
            Document doc = docs.get(i);
            documents.add(doc);
            index(doc);
        }
    }

    public void addDocument(Document doc) {
        documents.add(doc);
        index(doc);
    }

    private void index(Document doc) {
        MyArrayList<String> tokens = CorpusIngestor.tokenize(doc.content);
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (!vocabularyTrie.search(token)) {
                vocabularyTrie.insert(token);
                vocabulary.add(token);
            }
            MyArrayList<String> postings = invertedIndex.get(token);
            if (postings == null) {
                postings = new MyArrayList<>();
                invertedIndex.put(token, postings);
            }
            if (!postings.contains(doc.id)) postings.add(doc.id);
        }
    }

    public MyArrayList<Document> allDocuments() { return documents; }

    public Document getDocument(String id) {
        for (int i = 0; i < documents.size(); i++) {
            if (documents.get(i).id.equals(id)) return documents.get(i);
        }
        return null;
    }

    /** Exact pattern matching query: which documents contain the exact substring, and where. */
    public MyHashMap<String, MyArrayList<Integer>> exactSearch(String pattern) {
        MyHashMap<String, MyArrayList<Integer>> results = new MyHashMap<>();
        for (int i = 0; i < documents.size(); i++) {
            Document doc = documents.get(i);
            MyArrayList<Integer> matches = KMPMatcher.search(doc.content.toLowerCase(), pattern.toLowerCase());
            if (matches.size() > 0) results.put(doc.id, matches);
        }
        return results;
    }

    /** Fuzzy search query: vocabulary words within maxDistance of the query term. */
    public MyArrayList<EditDistance.FuzzyMatch> fuzzySearch(String term, int maxDistance) {
        return EditDistance.fuzzySearch(term.toLowerCase(), vocabulary, maxDistance);
    }

    /** Document similarity query: score between two documents by ID. */
    public double documentSimilarity(String docId1, String docId2) {
        Document d1 = getDocument(docId1);
        Document d2 = getDocument(docId2);
        if (d1 == null || d2 == null) return -1;
        return DocumentSimilarity.similarity(d1.content, d2.content);
    }

    public MyArrayList<String> lookupWord(String word) {
        MyArrayList<String> postings = invertedIndex.get(word.toLowerCase());
        return postings == null ? new MyArrayList<>() : postings;
    }

    public int vocabularySize() { return vocabulary.size(); }
    public MyArrayList<String> vocabulary() { return vocabulary; }
}
