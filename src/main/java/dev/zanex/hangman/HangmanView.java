package dev.zanex.hangman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class HangmanView extends JFrame {
    private JPanel mainPanel;
    private JPanel imagePanel;
    private JLabel statusLabel;
    private JLabel wordLabel;
    private JTextField inputField;
    private JLabel historyLabel;
    private JButton newGameButton;
    private JLabel hangmanImage;
    private JMenuBar menuBar;

    // Menu items that need to be accessed
    private JMenu maxFailuresMenu;
    private JCheckBoxMenuItem showHistoryItem;
    private JRadioButtonMenuItem defaultWordsItem;
    private JRadioButtonMenuItem fileWordsItem;

    public HangmanView() {
        super("Hangman Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        initializeUI();
    }

    public void initializeUI() {
        createMenuBar();
        createMainPanel();

        add(mainPanel);
    }

    private void createMenuBar() {
        menuBar = new JMenuBar();

        // Game Menu
        JMenu gameMenu = new JMenu("Game");
        JMenuItem newGameItem = new JMenuItem("New Game");
        JMenuItem exitItem = new JMenuItem("Exit");

        // Options Menu
        JMenu optionsMenu = new JMenu("Options");

        // Max failures submenu
        maxFailuresMenu = new JMenu("Max Failures");
        ButtonGroup failureGroup = new ButtonGroup();

        for (int i = 5; i <= 12; i++) {
            JRadioButtonMenuItem failureItem = new JRadioButtonMenuItem(i + " attempts");
            failureGroup.add(failureItem);
            maxFailuresMenu.add(failureItem);

            if (i == 9) {
                failureItem.setSelected(true);
            }
        }

        // Word source submenu
        JMenu wordSourceMenu = new JMenu("Word Source");
        ButtonGroup wordSourceGroup = new ButtonGroup();

        defaultWordsItem = new JRadioButtonMenuItem("Default Words");
        defaultWordsItem.setSelected(true);

        fileWordsItem = new JRadioButtonMenuItem("From File");

        wordSourceGroup.add(defaultWordsItem);
        wordSourceGroup.add(fileWordsItem);
        wordSourceMenu.add(defaultWordsItem);
        wordSourceMenu.add(fileWordsItem);

        // History visibility checkbox
        showHistoryItem = new JCheckBoxMenuItem("Show Letter History");
        showHistoryItem.setSelected(true);

        // Add items to menus
        gameMenu.add(newGameItem);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);

        optionsMenu.add(maxFailuresMenu);
        optionsMenu.add(wordSourceMenu);
        optionsMenu.add(showHistoryItem);

        // Add menus to menu bar
        menuBar.add(gameMenu);
        menuBar.add(optionsMenu);

        setJMenuBar(menuBar);
    }

    private void createMainPanel() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel with status
        statusLabel = new JLabel("Attempts: 0/9");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Center panel with word and image
        JPanel centerPanel = new JPanel(new BorderLayout());

        wordLabel = new JLabel("");
        wordLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        wordLabel.setHorizontalAlignment(SwingConstants.CENTER);

        imagePanel = new JPanel(new BorderLayout());
        hangmanImage = new JLabel();
        hangmanImage.setHorizontalAlignment(SwingConstants.CENTER);
        imagePanel.add(hangmanImage, BorderLayout.CENTER);

        centerPanel.add(wordLabel, BorderLayout.NORTH);
        centerPanel.add(imagePanel, BorderLayout.CENTER);

        // Bottom panel with input and history
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));

        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        JLabel promptLabel = new JLabel("Enter a letter: ");
        inputField = new JTextField(1);

        inputPanel.add(promptLabel, BorderLayout.WEST);
        inputPanel.add(inputField, BorderLayout.CENTER);

        historyLabel = new JLabel("Guessed letters: ");

        newGameButton = new JButton("New Game");
        newGameButton.setVisible(false);

        bottomPanel.add(inputPanel, BorderLayout.NORTH);
        bottomPanel.add(historyLabel, BorderLayout.CENTER);
        bottomPanel.add(newGameButton, BorderLayout.SOUTH);

        // Add all panels to main panel
        mainPanel.add(statusLabel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    // Register action listeners
    public void registerNewGameAction(ActionListener listener) {
        newGameButton.addActionListener(listener);
        for (Component item : menuBar.getMenu(0).getMenuComponents()) {
            if (item instanceof JMenuItem && ((JMenuItem)item).getText().equals("New Game")) {
                ((JMenuItem)item).addActionListener(listener);
            }
        }
    }

    public void registerExitAction(ActionListener listener) {
        for (Component item : menuBar.getMenu(0).getMenuComponents()) {
            if (item instanceof JMenuItem && ((JMenuItem)item).getText().equals("Exit")) {
                ((JMenuItem)item).addActionListener(listener);
            }
        }
    }

    public void registerMaxFailuresAction(ActionListener listener) {
        for (Component item : maxFailuresMenu.getMenuComponents()) {
            if (item instanceof JMenuItem) {
                ((JMenuItem)item).addActionListener(listener);
            }
        }
    }

    public void registerWordSourceAction(ActionListener listener) {
        defaultWordsItem.addActionListener(listener);
        fileWordsItem.addActionListener(listener);
    }

    public void registerShowHistoryAction(ActionListener listener) {
        showHistoryItem.addActionListener(listener);
    }

    public void registerInputAction(ActionListener listener) {
        inputField.addActionListener(listener);
    }

    // Update UI methods
    public void updateWordLabel(char[] guessedWord) {
        StringBuilder sb = new StringBuilder();
        for (char c : guessedWord) {
            sb.append(c).append(" ");
        }
        wordLabel.setText(sb.toString());
    }

    public void updateHistoryLabel(ArrayList<Character> guessedLetters, boolean showHistory) {
        historyLabel.setVisible(showHistory);

        if (!showHistory) {
            return;
        }

        StringBuilder sb = new StringBuilder("Guessed letters: ");
        ArrayList<Character> sortedLetters = new ArrayList<>(guessedLetters);
        Collections.sort(sortedLetters);

        for (Character c : sortedLetters) {
            sb.append(c).append(" ");
        }

        historyLabel.setText(sb.toString());
    }

    public void updateStatusLabel(int failures, int maxFailures) {
        statusLabel.setText("Attempts: " + failures + "/" + maxFailures);
    }

    public void updateHangmanImage(int failures) {
        // In a real implementation, you would load the image based on failures
        hangmanImage.setText("Hangman Stage: " + failures);
    }

    public void showGameWon(String word) {
        JOptionPane.showMessageDialog(this, "Congratulations! You won!", "Victory", JOptionPane.INFORMATION_MESSAGE);
        statusLabel.setText("You Won! Word: " + word);
        endGame();
    }

    public void showGameOver(String word) {
        JOptionPane.showMessageDialog(this, "Game Over! The word was: " + word, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        statusLabel.setText("Game Over! Word: " + word);
        endGame();
    }

    public void showDuplicateGuessMessage() {
        JOptionPane.showMessageDialog(this, "You already tried this letter!", "Duplicate Guess", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showWordsLoadedMessage(int count) {
        JOptionPane.showMessageDialog(this, "Loaded " + count + " words successfully!", "Words Loaded", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showFileErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, "Error reading file: " + message, "File Error", JOptionPane.ERROR_MESSAGE);
    }

    public File showFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Words File");

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    public void resetForNewGame() {
        inputField.setText("");
        inputField.setEnabled(true);
        newGameButton.setVisible(false);
        inputField.requestFocus();
    }

    private void endGame() {
        inputField.setEnabled(false);
        newGameButton.setVisible(true);
    }

    public void display() {
        setVisible(true);
    }

    public int getSelectedMaxFailures() {
        for (Component item : maxFailuresMenu.getMenuComponents()) {
            if (item instanceof JRadioButtonMenuItem && ((JRadioButtonMenuItem)item).isSelected()) {
                String text = ((JRadioButtonMenuItem)item).getText();
                return Integer.parseInt(text.split(" ")[0]);
            }
        }
        return 9; // Default
    }

    public String getInputText() {
        return inputField.getText();
    }

    public void clearInput() {
        inputField.setText("");
    }
}