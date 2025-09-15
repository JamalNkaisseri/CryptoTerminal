package com.cryptox;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainWindow {
    private final Stage stage;
    private BorderPane root;  // keep reference so sidebar can swap center content

    public MainWindow(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        // Use BorderPane so we can attach the sidebar
        root = new BorderPane();

        // Add sidebar on the left (pass reference to MainWindow for callbacks)
        SideBar sidebar = new SideBar(this);
        root.setLeft(sidebar);

        // Default main content (welcome screen or empty)
        StackPane welcome = new StackPane();
        welcome.setStyle("-fx-background-color: #252537;");
        root.setCenter(welcome);

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

    // === Called by sidebar ===
    public void showWalletManager() {
        root.setCenter(new WalletManagerView());
    }
}
