package texthack.corpus;

import texthack.datastructures.MyArrayList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Reads a folder of .txt files into Document objects. Each file becomes
 * one document; the file name (without extension) is used as the ID/title.
 * This is the "corpus ingestion" layer of the architecture described in
 * the abstract, sitting below the algorithm engine and query services.
 */
public class CorpusIngestor {

    public static MyArrayList<Document> ingestFolder(String folderPath, String language) throws IOException {
        MyArrayList<Document> documents = new MyArrayList<>();
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        if (files == null) return documents;

        for (File file : files) {
            String content = readFile(file);
            String id = file.getName().replaceFirst("\\.txt$", "");
            documents.add(new Document(id, id, content, language));
        }
        return documents;
    }

    private static String readFile(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (!first) sb.append('\n');
                sb.append(line);
                first = false;
            }
        }
        return sb.toString();
    }

    /** Splits document text into lowercase word tokens (simple whitespace/punctuation split). */
    public static MyArrayList<String> tokenize(String text) {
        MyArrayList<String> tokens = new MyArrayList<>();
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                word.append(Character.toLowerCase(c));
            } else if (word.length() > 0) {
                tokens.add(word.toString());
                word.setLength(0);
            }
        }
        if (word.length() > 0) tokens.add(word.toString());
        return tokens;
    }
}
