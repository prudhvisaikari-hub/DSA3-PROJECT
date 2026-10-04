package com.team7.texthackplus.structures.trie;

/**
 * Basic Trie (prefix tree) for Unicode strings. No java.util collections are used.
 * Each node stores a fixed array of child references for the basic multilingual
 * plane (BMP) code points (0‑65535). To keep memory reasonable we lazily allocate
 * child arrays only when needed.
 *
 * This structure is used by the tokenizer for Indian‑language corpora where
 * word‑boundary detection can be performed via longest‑prefix matching.
 */
public class Trie {
    private static final int CHILDREN_SIZE = 65536; // BMP size

    private static class Node {
        boolean isWord;
        Node[] children; // allocated lazily
    }

    private final Node root;

    public Trie() {
        root = new Node();
    }

    /** Inserts a word into the trie. */
    public void insert(String word) {
        Node cur = root;
        for (int i = 0; i < word.length(); i++) {
            int cp = word.charAt(i); // BMP code point
            if (cur.children == null) {
                cur.children = new Node[CHILDREN_SIZE];
            }
            if (cur.children[cp] == null) {
                cur.children[cp] = new Node();
            }
            cur = cur.children[cp];
        }
        cur.isWord = true;
    }

    /** Returns true if the exact word exists in the trie. */
    public boolean search(String word) {
        Node cur = root;
        for (int i = 0; i < word.length(); i++) {
            int cp = word.charAt(i);
            if (cur.children == null || cur.children[cp] == null) {
                return false;
            }
            cur = cur.children[cp];
        }
        return cur != null && cur.isWord;
    }

    /**
     * Returns true if there exists any word in the trie that starts with the given prefix.
     */
    public boolean startsWith(String prefix) {
        Node cur = root;
        for (int i = 0; i < prefix.length(); i++) {
            int cp = prefix.charAt(i);
            if (cur.children == null || cur.children[cp] == null) {
                return false;
            }
            cur = cur.children[cp];
        }
        return true;
    }
}
