package texthack.datastructures;

/**
 * Trie (prefix tree) from scratch. Supports insert, exact search, prefix
 * search, and collecting all words under a prefix - used for fast
 * autocomplete-style exact pattern lookups over the corpus vocabulary.
 */
public class MyTrie {

    private static class TrieNode {
        MyHashMap<Character, TrieNode> children = new MyHashMap<>();
        boolean isEndOfWord = false;
    }

    private final TrieNode root = new TrieNode();

    public void insert(String word) {
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            TrieNode next = node.children.get(c);
            if (next == null) {
                next = new TrieNode();
                node.children.put(c, next);
            }
            node = next;
        }
        node.isEndOfWord = true;
    }

    public boolean search(String word) {
        TrieNode node = findNode(word);
        return node != null && node.isEndOfWord;
    }

    public boolean startsWith(String prefix) {
        return findNode(prefix) != null;
    }

    private TrieNode findNode(String s) {
        TrieNode node = root;
        for (int i = 0; i < s.length(); i++) {
            node = node.children.get(s.charAt(i));
            if (node == null) return null;
        }
        return node;
    }

    /** Collects all words stored in the trie that start with the given prefix. */
    public MyArrayList<String> wordsWithPrefix(String prefix) {
        MyArrayList<String> results = new MyArrayList<>();
        TrieNode node = findNode(prefix);
        if (node != null) collect(node, new StringBuilder(prefix), results);
        return results;
    }

    private void collect(TrieNode node, StringBuilder path, MyArrayList<String> results) {
        if (node.isEndOfWord) results.add(path.toString());
        for (Character c : node.children.keys().toArray(new Character[0])) {
            TrieNode child = node.children.get(c);
            path.append((char) c);
            collect(child, path, results);
            path.deleteCharAt(path.length() - 1);
        }
    }
}
