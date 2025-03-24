package dev.zanex.hangman;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

public class HangmanController {
    private HangmanModel model;
    private HangmanView view;

    public HangmanController(HangmanModel model, HangmanView view) {
        this.model = model;
        this.view = view;
    }

    public void initialize() {
        registerEventHandlers();
        startNewGame();
        view.display();
    }

    private void registerEventHandlers() {
        view.registerNewGameAction(e -> startNewGame());
        view.registerExitAction(e -> System.exit(0));
        view.registerMaxFailuresAction(this::handleMaxFailuresChange);
        view.registerWordSourceAction(this::handleWordSourceChange);
        view.registerShowHistoryAction(this::handleShowHistoryChange);
        view.registerInputAction(this::handleInputAction);
    }

    private void startNewGame() {
        model.startNewGame();
        updateView();
        view.resetForNewGame();
    }

    private void handleMaxFailuresChange(ActionEvent e) {
        int maxFailures = view.getSelectedMaxFailures();
        model.setMaxFailures(maxFailures);
        updateView();
    }

    private void handleWordSourceChange(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("From File")) {
            loadWordsFromFile();
        }
    }

    private void handleShowHistoryChange(ActionEvent e) {
        boolean showHistory = ((JCheckBoxMenuItem) e.getSource()).isSelected();
        model.setShowHistory(showHistory);
        updateView();
    }

    private void handleInputAction(ActionEvent e) {
        String input = view.getInputText();
        view.clearInput();

        if (input == null || input.isEmpty()) {
            return;
        }

        char letter = input.charAt(0);
        boolean validGuess = model.processGuess(letter);

        if (!validGuess) {
            view.showDuplicateGuessMessage();
            return;
        }

        updateView();
        checkGameStatus();
    }

    private void loadWordsFromFile() {
        File file = view.showFileChooser();
        if (file != null) {
            try {
                model.loadWordsFromFile(file);
                view.showWordsLoadedMessage(model.getCurrentWord().length());
                startNewGame();
            } catch (FileNotFoundException e) {
                view.showFileErrorMessage(e.getMessage());
            } catch (IllegalArgumentException e) {
                view.showFileErrorMessage("No valid words found in the file.");
            }
        }
    }

    private void checkGameStatus() {
        if (model.isWordGuessed()) {
            view.showGameWon(model.getCurrentWord());
        } else if (model.isGameOver()) {
            view.showGameOver(model.getCurrentWord());
        }
    }

    private void updateView() {
        view.updateWordLabel(model.getGuessedWord());
        view.updateHistoryLabel(model.getGuessedLetters(), model.isShowHistory());
        view.updateStatusLabel(model.getFailures(), model.getMaxFailures());
        view.updateHangmanImage(model.getFailures());
    }
}