package com.cryptox;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainWindow {
    private final Stage stage;

    public MainWindow(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        // Use BorderPane so we can attach the sidebar
        BorderPane root = new BorderPane();

        // Add sidebar on the left
        SideBar sidebar = new SideBar();
        root.setLeft(sidebar);

        // Example: main content area
        StackPane mainContent = new StackPane();
        mainContent.setStyle("-fx-background-color: #252537;"); // just to differentiate
        root.setCenter(mainContent);

        // Scene
        Scene scene = new Scene(root, 1200, 800);
        ThemeManager.applyTheme(scene);

        // Stage setup
        stage.setTitle("CryptoX");
        stage.setScene(scene);
        stage.show();

        // Set opacity after stage init
        Platform.runLater(() -> {
            stage.setOpacity(0.95);
            System.out.println("Opacity set to: " + stage.getOpacity());
        });
    }
}
