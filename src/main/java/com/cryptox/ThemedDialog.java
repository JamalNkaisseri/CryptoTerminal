package com.cryptox;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.concurrent.atomic.AtomicReference;

public class ThemedDialog {

    public enum Result {
        YES, NO, CANCEL
    }

    public static Result show(String titleText, String message, Stage owner,
                              String yesText, String noText, String cancelText) {
        AtomicReference<Result> userChoice = new AtomicReference<>(Result.CANCEL);

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        if (owner != null) {
            dialog.initOwner(owner);
        }
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setResizable(false);

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: " + ThemeManager.BACKGROUND_COLOR + ";" +
                "-fx-border-color: #444; -fx-border-width: 1px;" +
                "-fx-background-radius: 6; -fx-border-radius: 6;");

        Label title = new Label(titleText);
        title.setStyle("-fx-text-fill: " + ThemeManager.SELECTION_COLOR + ";" +
                "-fx-font-size: 16px; -fx-font-weight: bold;");

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-text-fill: " + ThemeManager.TEXT_COLOR + ";" +
                "-fx-font-size: 13px;");
        messageLabel.setWrapText(true);

        // Buttons
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        Button yesButton = styledButton(yesText, "#89B4FA");   // Blue
        Button noButton = styledButton(noText, "#F38BA8");     // Red
        Button cancelButton = styledButton(cancelText, "#A6ADC8"); // Gray

        yesButton.setOnAction(e -> {
            userChoice.set(Result.YES);
            dialog.close();
        });

        noButton.setOnAction(e -> {
            userChoice.set(Result.NO);
            dialog.close();
        });

        cancelButton.setOnAction(e -> {
            userChoice.set(Result.CANCEL);
            dialog.close();
        });

        dialog.setOnCloseRequest(e -> userChoice.set(Result.CANCEL));

        buttons.getChildren().addAll(cancelButton, noButton, yesButton);
        layout.getChildren().addAll(title, messageLabel, buttons);

        Scene scene = new Scene(layout);
        dialog.setScene(scene);

        // Center relative to owner if available
        if (owner != null) {
            dialog.setX(owner.getX() + (owner.getWidth() - 350) / 2);
            dialog.setY(owner.getY() + (owner.getHeight() - 150) / 2);
        }

        dialog.showAndWait();
        return userChoice.get();
    }

    private static Button styledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + ";" +
                "-fx-text-fill: #1E1E2E; -fx-font-weight: bold;" +
                "-fx-background-radius: 4px; -fx-padding: 6px 12px;" +
                "-fx-font-size: 12px;");

        String baseStyle = button.getStyle();
        button.setOnMouseEntered(e -> button.setStyle(baseStyle + "-fx-opacity: 0.85;"));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));

        return button;
    }
}
