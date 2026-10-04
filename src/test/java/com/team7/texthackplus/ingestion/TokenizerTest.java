package com.team7.texthackplus.ingestion;

import com.team7.texthackplus.structures.array.DynamicArray;

/**
 * Simple test harness for {@link Tokenizer} using built‑in Java assertions.
 * Run with the VM option -ea to enable assertions.
 */
public class TokenizerTest {
    public static void main(String[] args) {
        Tokenizer tokenizer = new Tokenizer();
        // English tokenisation test
        String english = "The quick, brown fox jumps!";
        DynamicArray<String> tokens = tokenizer.tokenize(english);
        assert tokens.size() == 5 : "Expected 5 tokens, got " + tokens.size();
        assert "the".equals(tokens.get(0));
        assert "quick".equals(tokens.get(1));
        assert "brown".equals(tokens.get(2));
        assert "fox".equals(tokens.get(3));
        assert "jumps".equals(tokens.get(4));

        // Hindi tokenisation test (dictionary words)
        String hindi = "भारतस्वास्थ्य"; // concatenated words from dictionary
        DynamicArray<String> hindiTokens = tokenizer.tokenize(hindi);
        assert hindiTokens.size() == 2 : "Expected 2 Hindi tokens, got " + hindiTokens.size();
        assert "भारत".equals(hindiTokens.get(0));
        assert "स्वास्थ्य".equals(hindiTokens.get(1));

        System.out.println("TokenizerTest passed.");
    }
}
