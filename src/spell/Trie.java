package spell;

public class Trie implements ITrie {

    private final Node root = new Node();
    private int nodes = 1, words = 0;
    private static final char STRING_SEPARATOR = '\n';

    // Add a word to the trie
    public void add(String word) {
        String norm = word.toLowerCase();
        Node currentNode = root;

        for (int i = 0; i < norm.length(); ++i) {
            Node[] children = currentNode.getChildren();
            int charIndex = norm.charAt(i) - 'a';

            if (children[charIndex] == null) {
                children[charIndex] = new Node();
                ++nodes;
            }

            currentNode = children[charIndex];
            if (i == norm.length() - 1) {
                currentNode.incrementValue();
                if (currentNode.getValue() == 1) ++words;
            }
        }
    }

    // Find a word within the trie
    public Node find(String word) {
        String norm = word.toLowerCase();
        Node currentNode = root;

        for (int i = 0; i < word.length(); ++i) {
            int charIndex = norm.charAt(i) - 'a';
            Node[] children = currentNode.getChildren();

            if (children[charIndex] != null) currentNode = children[charIndex];
            else break;

            if (i == word.length() - 1 && currentNode.getValue() > 0) return currentNode;
        }
        return null;
    }

    public int getWordCount() {
        return words;
    }

    public int getNodeCount() {
        return nodes;
    }

    public Node getRoot() {
        return root;
    }

    // Return all strings in trie through recursive descent
    private void traverseString(StringBuilder words, StringBuilder currentWord, Node node) {
        Node[] children = node.getChildren();
        for (int i = 0; i < children.length; ++i) {
            if (children[i] != null) {
                currentWord.append((char)('a' + i));
                if (children[i].getValue() > 0) words.append(currentWord);
                traverseString(words, currentWord, children[i]);
                currentWord.setLength(currentWord.length() - 1);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder words = new StringBuilder();
        StringBuilder currentWord = new StringBuilder();

        currentWord.append(STRING_SEPARATOR);
        traverseString(words, currentWord, root);

        return words.toString().trim();
    }

    @Override
    public int hashCode() {
        int hash = nodes * 31;
        hash = 31 * hash + words;
        Node[] children = root.getChildren();

        for (int i = 0; i < 26; ++i) {
            if (children[i] != null) hash = 31 * hash + i;
        }

        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Trie)) return false;
        return compareTrie(((Trie) o).getRoot(), root);
    }

    // Recursively search both tries (Depth first) to check for equality
    private boolean compareTrie(Node compare, Node original) {
        if (compare == null || original == null || compare.getValue() != original.getValue()) return false;
        Node[] cNodes = compare.getChildren(), oNodes = original.getChildren();

        for (int i = 0; i < 26; ++i) {
            if (!(cNodes[i] == null && oNodes[i] == null) && !compareTrie(cNodes[i], oNodes[i])) return false;
        }

        return true;
    }
}
