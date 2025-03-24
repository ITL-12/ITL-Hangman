package dev.zanex.hangman;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.intellijthemes.FlatAllIJThemes;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDarkerIJTheme;

import javax.swing.*;
import java.awt.*;

public class MainApplication {

    public static void main(String[] args) {
        // Set up system properties for better macOS integration if needed
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.application.name", "Hangman");

        // Install FlatLaf themes
        installThemes();

        SwingUtilities.invokeLater(() -> {
            // Create the model and view
            HangmanModel model = new HangmanModel();
            HangmanView view = new HangmanView();

            // Create and initialize the controller
            HangmanController controller = new HangmanController(model, view);
            controller.initialize();
        });
    }

    private static void installThemes() {
        try {
            // Try to use the FlatDarculaLaf theme by default
            UIManager.setLookAndFeel(new FlatDarculaLaf());

            // Additional UI tweaks for consistency
            UIManager.put("Button.arc", 5);
            UIManager.put("Component.arc", 5);
            UIManager.put("ProgressBar.arc", 5);
            UIManager.put("TextComponent.arc", 5);

            // Set default font
            Font defaultFont = new Font("Segoe UI", Font.PLAIN, 12);
            UIManager.put("defaultFont", defaultFont);

        } catch (Exception e) {
            System.err.println("Failed to initialize LaF");
            e.printStackTrace();

            // Fall back to system look and feel if FlatLaf fails
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}