package com.texthackplus.ingestion;

/**
 * Represents a single document loaded into the system.
 * Holds a unique integer id, a human‑readable name, and an array of token strings.
 * All fields are immutable after construction to simplify concurrent use.
 */
public class Document {
    private final int id;
    private final String title;
    private final com.texthackplus.structures.array.DynamicArray<String> tokens;

    public Document(int id, String title, com.texthackplus.structures.array.DynamicArray<String> tokens) {
        this.id = id;
        this.title = title;
        this.tokens = tokens;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public com.texthackplus.structures.array.DynamicArray<String> getTokens() {
        return tokens;
    }
}
