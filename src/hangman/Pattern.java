package hangman;

import java.util.TreeSet;

public class Pattern implements Comparable<Pattern> {

    private String content;
    private int relativeMatches, indexes = 0;
    private TreeSet<String> group = new TreeSet<>();

    // Initialize pattern
    public Pattern(String pat) {
        this.content = pat;
        this.relativeMatches = pat.length();

        for (int i = pat.length() - 1; i >= 0; --i) {
            if (pat.charAt(i) != '-') {
                --relativeMatches;
                indexes += i * i;
            }
        }
    }

    public void add(String word) {
        group.add(word);
    }

    public int getRelativeMatches() { return relativeMatches; }

    public int getIndexes() { return indexes; }

    public TreeSet<String> getGroup() { return group; }

    // Sort patterns based on Group size > Matches > Index Locations
    @Override public int compareTo(Pattern o) {
        int score = (10000 * o.getGroup().size()) - (10000 * group.size()) +
                    (1000 * o.getRelativeMatches()) - (1000 * relativeMatches) +
                    (10 * o.getIndexes()) - (10 * indexes);
        return score;
    }

    @Override public String toString() {
        return content;
    }

    // Convert a string to a pattern string
    public static String toPattern(String word, char guess) {
        StringBuilder pattern = new StringBuilder();
        for (int i = 0; i < word.length(); ++i) {
            if (word.charAt(i) == guess) pattern.append(guess);
            else pattern.append('-');
        }

        return pattern.toString();
    }
}
