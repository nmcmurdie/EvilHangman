package spell;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SpellCorrector implements ISpellCorrector {

    private final Trie dictionary = new Trie();

    // Open dictionary and load it into the trie
    public void useDictionary(String dictionaryFileName) throws IOException {
        File f = new File(dictionaryFileName);
        Scanner scanner = new Scanner(f);
        scanner.useDelimiter("((#[^\\n]*\\n)|(\\s+))+");

        while(scanner.hasNext()) {
            dictionary.add(scanner.next());
        }
    }

    // Find closest word within trie
    public String suggestSimilarWord(String inputWord) {
        String normWord = inputWord.toLowerCase();
        if (dictionary.find(normWord) != null) return normWord;

        ArrayList<String> candidates = new ArrayList<>();
        ArrayList<String> wordsFound = new ArrayList<>();
        ArrayList<Integer> wordCounts = new ArrayList<>();
        candidates.add(normWord);

        findCandidates(candidates, wordsFound, wordCounts);
        if (wordsFound.isEmpty()) {
            candidates.remove(0);
            findCandidates(candidates, wordsFound, wordCounts);
        }
        if (wordsFound.isEmpty()) return null;

        Set<String> finalCandidates = new TreeSet<>();

        for (int i = 0, maxCount = Collections.max(wordCounts); i < wordsFound.size(); ++i) {
            if (wordCounts.get(i) == maxCount) finalCandidates.add(wordsFound.get(i));
        }

        return finalCandidates.iterator().next();
    }

    // Find all words within 1 edit distance away from input string
    private void findCandidates(ArrayList<String> candidates, ArrayList<String> wordsFound, ArrayList<Integer> wordCounts) {
        for (int i = 0, k = candidates.size(); i < k; ++i) {
            String input = candidates.get(i);
            deleteDistance(candidates, input);
            transDistance(candidates, input);
            altDistance(candidates, input);
            insertDistance(candidates, input);
        }

        for (String s : candidates) {
            Node n = dictionary.find(s);

            if (n != null) {
                wordsFound.add(s);
                wordCounts.add(n.getValue());
            }
        }
    }

    // Find all strings 1 deletion distance away from input string
    private void deleteDistance(ArrayList<String> words, String input) {
        for (int i = 0; i < input.length(); ++i) {
            StringBuilder newWord = new StringBuilder(input);
            String word = newWord.deleteCharAt(i).toString();
            words.add(word);
        }
    }

    // Find all strings 1 transposition distance away from input string
    private void transDistance(ArrayList<String> words, String input) {
        for (int i = 0; i < input.length() - 1; ++i) {
            StringBuilder newWord = new StringBuilder(input);
            newWord.setCharAt(i, input.charAt(i + 1));
            newWord.setCharAt(i + 1, input.charAt(i));
            String word = newWord.toString();
            if (!word.equals(input)) words.add(word);
        }
    }

    // Find all strings 1 alteration distance away from input string
    private void altDistance(ArrayList<String> words, String input) {
        for (int i = 0; i < input.length(); ++i) {
            StringBuilder newWord = new StringBuilder(input);

            for (int j = 0; j < 26; ++j) {
                if ('a' + j != input.charAt(i)) {
                    newWord.setCharAt(i, (char) ('a' + j));
                    words.add(newWord.toString());
                }
            }
        }
    }

    // Find all strings 1 insertion distance away from input string
    private void insertDistance(ArrayList<String> words, String input) {
        for (int i = 0; i <= input.length(); ++i) {
            StringBuilder newWord = new StringBuilder(input);
            newWord.insert(i, 'a');
            words.add(newWord.toString());

            for (int j = 1; j < 26; ++j) {
                newWord.setCharAt(i, (char) ('a' + j));
                words.add(newWord.toString());
            }
        }
    }
}
