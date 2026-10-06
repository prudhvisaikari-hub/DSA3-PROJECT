package com.texthackplus.ingestion;

import com.texthackplus.structures.array.DynamicArray;
import com.texthackplus.structures.trie.Trie;

/**
 * Tokenizer that converts raw text into a sequence of normalized tokens.
 * <p>
 *   • For Latin scripts (English) we split on any non‑letter character and
 *     lower‑case the result.
 *   • For Devanagari/Hindi we perform longest‑prefix matching against a
 *     small built‑in dictionary using the custom {@link Trie}. This demonstrates
 *     how a Unicode‑aware prefix tree can replace {@code String.split} without
 *     relying on java.util collections.
 * </p>
 */
public class Tokenizer {
    private final Trie hindiDictionary;

    public Tokenizer() {
        hindiDictionary = new Trie();
        // Tiny dictionary sufficient for demo corpus – real projects would load a large lexicon.
        String[] hindiWords = {
            "भारत", "स्वास्थ्य", "विकास", "विश्व", "समाधान",
            "अनुसंधान", "डेटा", "सिस्टम", "शिक्षा", "प्रौद्योगिकी"
        };
        for (String w : hindiWords) {
            hindiDictionary.insert(w);
        }
    }

    /**
     * Tokenizes the given input string.
     * @param text raw document text (UTF‑8).
     * @return DynamicArray of token strings.
     */
    public DynamicArray<String> tokenize(String text) {
        DynamicArray<String> tokens = new DynamicArray<>();
        if (text == null || text.isEmpty()) {
            return tokens;
        }
        // Detect script by checking first character code point.
        char first = text.charAt(0);
        if (isDevanagari(first)) {
            // Hindi/Devanagari tokenisation via Trie longest‑prefix.
            int i = 0;
            while (i < text.length()) {
                int longest = 0;
                String match = null;
                // Try every possible substring starting at i (bounded by remaining length).
                for (int j = i + 1; j <= text.length(); j++) {
                    String sub = text.substring(i, j);
                    if (hindiDictionary.search(sub)) {
                        longest = j - i;
                        match = sub;
                    }
                }
                if (longest > 0) {
                    tokens.add(match);
                    i += longest;
                } else {
                    // No dictionary match – treat single character as token.
                    tokens.add(String.valueOf(text.charAt(i)));
                    i++;
                }
            }
        } else {
            // Simple English tokenisation: split on non‑letters, lower‑case.
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (Character.isLetter(c)) {
                    sb.append(Character.toLowerCase(c));
                } else {
                    if (sb.length() > 0) {
                        tokens.add(sb.toString());
                        sb.setLength(0);
                    }
                }
            }
            if (sb.length() > 0) {
                tokens.add(sb.toString());
            }
        }
        return tokens;
    }

    /** Simple check for Devanagari block (U+0900 to U+097F). */
    private boolean isDevanagari(char ch) {
        return ch >= '\u0900' && ch <= '\u097F';
    }
}
