package com.texthackplus.ingestion;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.texthackplus.structures.array.DynamicArray;
import com.texthackplus.ingestion.Document;
import com.texthackplus.ingestion.DocumentStore;
import com.texthackplus.ingestion.Tokenizer;

/**
 * Loads plain‑text corpus files, tokenises them and registers the documents
 * in a {@link DocumentStore}. All files under the resources directory
 * <code>src/main/resources/corpora</code> are treated as separate documents.
 */
public class CorpusLoader {
    private final Tokenizer tokenizer;
    private final DocumentStore store;
    private int nextId;

    public CorpusLoader() {
        this.tokenizer = new Tokenizer();
        this.store = new DocumentStore();
        this.nextId = 1;
    }

    /** Loads every *.txt file in the corpora resource folder. */
    public void loadAll() throws IOException {
        // Resolve the absolute path to the resources folder.
        String userDir = System.getProperty("user.dir");
        Path corporaDir = Paths.get(userDir, "src", "main", "resources", "corpora");
        if (!Files.isDirectory(corporaDir)) {
            throw new IOException("Corpus directory not found: " + corporaDir);
        }
        Files.list(corporaDir)
                .filter(p -> p.toString().endsWith(".txt"))
                .forEach(this::loadFile);
    }

    /** Loads a single file, creates a Document and adds it to the store. */
    private void loadFile(Path filePath) {
        try {
            String content = Files.readString(filePath, StandardCharsets.UTF_8);
            String title = filePath.getFileName().toString();
            DynamicArray<String> tokens = tokenizer.tokenize(content);
            Document doc = new Document(nextId++, title, tokens);
            store.addDocument(doc);
        } catch (IOException e) {
            // For demo purposes we simply print the stack trace – a real system
            // would surface the error to the user interface.
            e.printStackTrace();
        }
    }

    /** Returns the populated DocumentStore after loading. */
    public DocumentStore getStore() {
        return store;
    }
}
