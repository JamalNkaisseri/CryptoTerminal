package com.cryptox;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ThemeManager {

    // ====== Core Colors (for programmatic use) ======
    public static final String BACKGROUND_COLOR = "#1E1E2E";
    public static final String TEXT_COLOR = "#E0E0E0";
    public static final String SELECTION_COLOR = "#BB86FC";
    public static final String CARET_COLOR = "#FF79C6";

    // ====== CSS File Path ======
    private static final String CSS_FILE = "/styles/theme.css";

    // ====== Main Theme Application Method ======

    /**
     * Apply the CSS theme to a Scene
     * Make sure to place theme.css in src/main/resources/styles/theme.css
     */
    public static void applyTheme(Scene scene) {
        try {
            // Clear any existing stylesheets
            scene.getStylesheets().clear();

            // Load the CSS file from resources
            String cssResource = ThemeManager.class.getResource("/styles/theme.css").toExternalForm();
            scene.getStylesheets().add(cssResource);

            System.out.println("Theme CSS loaded successfully from: " + cssResource);

        } catch (Exception e) {
            System.err.println("Failed to load CSS theme: " + e.getMessage());
            System.err.println("Make sure theme.css is located at: src/main/resources/styles/theme.css");

            // Fallback: apply basic styling programmatically
            scene.getRoot().setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        }
    }


    /**
     * Add a style class to any node
     */
    public static void addStyleClass(javafx.scene.Node node, String styleClass) {
        node.getStyleClass().add(styleClass);
    }



}