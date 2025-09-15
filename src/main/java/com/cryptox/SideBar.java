package com.cryptox;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

public class SideBar extends VBox {
    private boolean collapsed = false; // sidebar state
    private final double expandedWidth = 200;
    private final double collapsedWidth = 80;
    private final Duration animationDuration = Duration.millis(250);

    public SideBar() {
        setPadding(new Insets(20));
        setSpacing(15);
        setAlignment(Pos.TOP_LEFT);
        setPrefWidth(expandedWidth);
        setStyle("-fx-background-color: " + ThemeManager.BACKGROUND_COLOR + ";");

        // Toggle button (☰) to collapse/expand
        Button toggleBtn = new Button("☰");
        toggleBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + ThemeManager.TEXT_COLOR + "; -fx-font-size:16px;");
        toggleBtn.setOnAction(e -> toggle());

        // Sidebar buttons
        Button homeBtn = createSidebarButton("fas-home", "Home");
        Button marketsBtn = createSidebarButton("fas-chart-line", "Markets");
        Button walletBtn = createSidebarButton("fas-wallet", "Wallet");
        Button settingsBtn = createSidebarButton("fas-cog", "Settings");

        getChildren().addAll(toggleBtn, homeBtn, marketsBtn, walletBtn, settingsBtn);
    }

    private Button createSidebarButton(String iconLiteral, String text) {
        FontIcon icon = new FontIcon(iconLiteral);
        icon.setIconSize(20);
        icon.setIconColor(Color.web(ThemeManager.TEXT_COLOR));

        Button btn = new Button(text, icon);
        btn.setContentDisplay(ContentDisplay.LEFT);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + ThemeManager.TEXT_COLOR + "; -fx-font-size: 14px;");
        btn.setPrefWidth(Double.MAX_VALUE);

        Tooltip tooltip = new Tooltip(text);
        Tooltip.install(btn, tooltip);

        // Store tooltip reference so we can restore text after collapse
        btn.getProperties().put("tooltip-ref", tooltip);

        // Hover effect
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: " + ThemeManager.SELECTION_COLOR + "; -fx-text-fill: white; -fx-font-size: 14px;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + ThemeManager.TEXT_COLOR + "; -fx-font-size: 14px;"));

        return btn;
    }

    private void toggle() {
        collapsed = !collapsed;

        double startWidth = getWidth();
        double targetWidth = collapsed ? collapsedWidth : expandedWidth;

        // Animate sidebar width
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(prefWidthProperty(), startWidth)),
                new KeyFrame(animationDuration, new KeyValue(prefWidthProperty(), targetWidth))
        );
        timeline.play();

        // Update button labels after animation
        if (collapsed) {
            getChildren().forEach(node -> {
                if (node instanceof Button btn && btn.getGraphic() != null && !btn.getText().equals("☰")) {
                    btn.setText(""); // hide text
                }
            });
        } else {
            getChildren().forEach(node -> {
                if (node instanceof Button btn && btn.getGraphic() != null && btn.getText().isEmpty()) {
                    Tooltip tip = (Tooltip) btn.getProperties().get("tooltip-ref");
                    if (tip != null) btn.setText(tip.getText()); // restore label
                }
            });
        }
    }
}
