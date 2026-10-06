package com.texthackplus.ingestion;

import com.texthackplus.structures.map.CustomHashMap;
import com.texthackplus.structures.array.DynamicArray;
import com.texthackplus.structures.list.SinglyLinkedList;

/**
 * In‑memory store for {@link Document} objects and a simple inverted index.
 * <p>
 *   • {@code docById} – fast lookup of a document given its integer ID.
 *   • {@code postings} – maps each token (String) to a list of document IDs that
 *     contain the token. Implemented with {@code CustomHashMap<String, DynamicArray<Integer>>}.
 * </p>
 * This design mirrors classic information‑retrieval systems while complying
 * with the “no java.util” constraint.
 */
public class DocumentStore {
    private final CustomHashMap<Integer, Document> docById;
    private final CustomHashMap<String, DynamicArray<Integer>> postings;

    public DocumentStore() {
        docById = new CustomHashMap<>();
        postings = new CustomHashMap<>();
    }

    /** Adds a document and updates the inverted index. */
    public void addDocument(Document doc) {
        int docId = doc.getId();
        docById.put(docId, doc);
        // Index each token.
        DynamicArray<String> toks = doc.getTokens();
        for (int i = 0; i < toks.size(); i++) {
            String token = toks.get(i);
            DynamicArray<Integer> list = postings.get(token);
            if (list == null) {
                list = new DynamicArray<>();
                postings.put(token, list);
            }
            // Avoid duplicate doc IDs for the same token in the same doc.
            // Simple linear check – acceptable for small demo corpora.
            boolean already = false;
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j) == docId) {
                    already = true;
                    break;
                }
            }
            if (!already) {
                list.add(docId);
            }
        }
    }

    /** Retrieves a document by its unique ID, or {@code null} if missing. */
    public Document getDocument(int id) {
        return docById.get(id);
    }

    /** Returns the posting list (document IDs) for a given token. */
    public DynamicArray<Integer> getPostingList(String token) {
        DynamicArray<Integer> list = postings.get(token);
        return list == null ? new DynamicArray<>() : list;
    }

    /** Returns total number of documents stored. */
    public int documentCount() {
        return docById.size();
    }

    /** Returns all unique vocabulary tokens indexed in the store. */
    public DynamicArray<String> getAllTokens() {
        return postings.keySet();
    }

    /** Returns internal postings map. */
    public CustomHashMap<String, DynamicArray<Integer>> getPostings() {
        return postings;
    }
}
