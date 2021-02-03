package hangman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {

    private SortedSet<Character> guessedLetters = new TreeSet<>();
    private char[] wordProgress;
    private TreeSet<String> possibleWords;

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        possibleWords = new TreeSet<>();
        wordProgress = new char[wordLength];
        Arrays.fill(wordProgress, '-');

        try (BufferedReader read = new BufferedReader(new FileReader(dictionary))) {
            for (String line; (line = read.readLine()) != null;) {
                if (line.length() == wordLength)
                    possibleWords.add(line);
            }
        }

        if (possibleWords.isEmpty() || wordLength < 1) throw new EmptyDictionaryException();
    }

    @Override
    public TreeSet<String> makeGuess(char c) throws GuessAlreadyMadeException {
        char guess = Character.toLowerCase(c);
        if (guessedLetters.contains(guess)) throw new GuessAlreadyMadeException();
        else guessedLetters.add(guess);

        Set<String> patterns = new HashSet<>();
        HashMap<String, Pattern> wordPatterns = new HashMap<>();

        for (String s : possibleWords) {
            String pat = Pattern.toPattern(s, guess);
            patterns.add(pat);

            if (wordPatterns.get(pat) == null) wordPatterns.put(pat, new Pattern(pat));
            wordPatterns.get(pat).add(s);
        }

        Pattern bestPattern = findBestPattern(patterns, wordPatterns);
        possibleWords = bestPattern.getGroup();

        return possibleWords;
    }

    private Pattern findBestPattern(Set<String> patterns, HashMap<String, Pattern> wordPatterns) {
        TreeSet<Pattern> pat = new TreeSet<>();
        for (String s : patterns) {
            Pattern p = wordPatterns.get(s);
            pat.add(p);
        }
        return pat.first();
    }

    // Returns a string of all guessed letters in the word
    public String getCurrentWord() {
        String word = possibleWords.first();
        for (int i = 0; i < word.length(); ++i) {
            for (char c : guessedLetters) {
                if (word.charAt(i) == c) wordProgress[i] = c;
            }
        }
        return new String(wordProgress);
    }

    // Get first word if user runs out of guesses
    public String getFirstWord() {
        return possibleWords.first();
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return this.guessedLetters;
    }
}
