package com.team7.texthackplus.ingestion;

import com.team7.texthackplus.structures.array.DynamicArray;

/**
 * Simple test harness for DocumentStore and CorpusLoader using built‑in assertions.
 * Run with JVM option -ea to enable assertions.
 */
public class IngestionTest {
    public static void main(String[] args) throws Exception {
        // DocumentStore add & retrieve test
        DocumentStore store = new DocumentStore();
        DynamicArray<String> tokens = new DynamicArray<>();
        tokens.add("sample");
        tokens.add("text");
        Document doc = new Document(1, "doc1", tokens);
        store.addDocument(doc);
        assert store.documentCount() == 1 : "Expected 1 document, got " + store.documentCount();
        assert store.getDocument(1) == doc : "Retrieved document mismatch";
        DynamicArray<Integer> posting = store.getPostingList("sample");
        assert posting.size() == 1 : "Posting list size should be 1";
        assert posting.get(0) == 1 : "Posting list should contain doc id 1";

        // CorpusLoader loads all sample corpora (3 files)
        CorpusLoader loader = new CorpusLoader();
        loader.loadAll();
        DocumentStore loadedStore = loader.getStore();
        assert loadedStore.documentCount() >= 3 : "Expected at least 3 documents, got " + loadedStore.documentCount();

        System.out.println("IngestionTest passed.");
    }
}
