package dev.zanex.hangman;

import java.io.*;
import java.util.*;

public class HangmanModel {
    private String currentWord;
    private char[] guessedWord;
    private ArrayList<Character> guessedLetters;
    private int failures;
    private int maxFailures;
    private boolean showHistory;
    private String[] words;
    private Random random;

    public HangmanModel() {
        maxFailures = 9;
        showHistory = true;
        words = new String[]{"COMPUTER", "PROGRAMMING", "JAVA", "DEVELOPER", "APPLICATION"};
        random = new Random();
        guessedLetters = new ArrayList<>();
    }

    public void startNewGame() {
        failures = 0;
        guessedLetters.clear();
        currentWord = words[random.nextInt(words.length)];

        guessedWord = new char[currentWord.length()];
        for (int i = 0; i < currentWord.length(); i++) {
            guessedWord[i] = '_';
        }
    }

    public boolean processGuess(char letter) {
        letter = Character.toUpperCase(letter);

        if (guessedLetters.contains(letter)) {
            return false;
        }

        guessedLetters.add(letter);

        boolean found = false;
        for (int i = 0; i < currentWord.length(); i++) {
            if (currentWord.charAt(i) == letter) {
                guessedWord[i] = letter;
                found = true;
            }
        }

        if (!found) {
            failures++;
        }

        return true;
    }

    public boolean isWordGuessed() {
        for (char c : guessedWord) {
            if (c == '_') {
                return false;
            }
        }
        return true;
    }

    public boolean isGameOver() {
        return failures >= maxFailures;
    }

    public void loadWordsFromFile(File file) throws FileNotFoundException {
        ArrayList<String> loadedWords = new ArrayList<>();
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String word = scanner.nextLine().trim().toUpperCase();
            if (!word.isEmpty()) {
                loadedWords.add(word);
            }
        }

        scanner.close();

        if (!loadedWords.isEmpty()) {
            words = loadedWords.toArray(new String[0]);
        } else {
            throw new IllegalArgumentException("No valid words found in file");
        }
    }

    // Getters and setters
    public String getCurrentWord() {
        return currentWord;
    }

    public char[] getGuessedWord() {
        return guessedWord;
    }

    public ArrayList<Character> getGuessedLetters() {
        return guessedLetters;
    }

    public int getFailures() {
        return failures;
    }

    public int getMaxFailures() {
        return maxFailures;
    }

    public void setMaxFailures(int maxFailures) {
        this.maxFailures = maxFailures;
    }

    public boolean isShowHistory() {
        return showHistory;
    }

    public void setShowHistory(boolean showHistory) {
        this.showHistory = showHistory;
    }
}