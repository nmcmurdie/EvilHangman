package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangman {

    private int guessesLeft;

    public static void main(String[] args) {
        new EvilHangman(args);
    }

    public EvilHangman(String[] args) {
        if (args.length < 3) {
            System.out.println("ERROR: Missing Arguments. Correct usage is EvilHangman <dictionary> <word length> <guesses>");
            return;
        }
        File dictionary = new File(args[0]);

        int wordLength = Integer.parseInt(args[1]);
        this.guessesLeft = Integer.parseInt(args[2]);

        EvilHangmanGame game = new EvilHangmanGame();
        Scanner userInput = new Scanner(System.in);

        try {
            game.startGame(dictionary, wordLength);
        }
        catch (IOException ex) {
            System.out.println("ERROR: Unable to open Dictionary file");
            return;
        }
        catch (EmptyDictionaryException ex) {
            System.out.println("ERROR: Dictionary does not contain words with the given word length");
            return;
        }

        while (guessesLeft > 0) gameLoop(game, userInput);
    }

    // Handle and verify user input and pass it to EvilHangmanGame
    private void gameLoop(EvilHangmanGame game, Scanner userInput) {
        System.out.printf("You have %d guess%s left\nUsed letters:", guessesLeft, (guessesLeft == 1) ? "" : "es");

        SortedSet<Character> guessedLetters = game.getGuessedLetters();
        for (Character c : guessedLetters) System.out.printf(" %c", c);

        System.out.printf("\nWord: %s\nEnter guess: ", game.getCurrentWord());
        char guess = Character.toLowerCase(userInput.next().charAt(0));

        if (!Character.isLetter(guess)) System.out.println("Guess is not a letter, please enter a valid guess");
        else {
            try {
                TreeSet<String> words = game.makeGuess(guess);
                printResult(words, guess);
            } catch (GuessAlreadyMadeException ex) {
                System.out.println("You've already guessed this letter, try again\n");
                return;
            }
        }

        if (!game.getCurrentWord().contains("-")) {
            System.out.println("You Win!");
            guessesLeft = 0;
        }
        else if (guessesLeft > 0) System.out.println();
        else System.out.printf("You lose!\nThe word was: %s\n", game.getFirstWord());
    }

    // Tell user how many characters were correctly guessed
    private void printResult(TreeSet<String> words, char guess) {
        String firstWord = words.first();

        int letterUsage = 0;
        for (int i = 0; i < firstWord.length(); ++i) {
            if (firstWord.charAt(i) == guess) ++letterUsage;
        }

        switch (letterUsage) {
            default -> System.out.printf("Yes, there are %d %c's\n", letterUsage, guess);
            case 0 -> {
                System.out.printf("Sorry, there are no %c's\n", guess);
                --guessesLeft;
            }
            case 1 -> System.out.printf("Yes, there is 1 %c\n", guess);
        }
    }

}
