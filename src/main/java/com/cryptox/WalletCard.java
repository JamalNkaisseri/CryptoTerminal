package com.cryptox;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Map;

public class WalletCard extends VBox {
    private final String walletAddress;
    private final Label ethBalanceLabel = new Label("ETH: …");
    private final Label usdcBalanceLabel = new Label("USDC: …");
    private final Label maticBalanceLabel = new Label("MATIC: …");

    public WalletCard(String walletAddress) {
        this.walletAddress = walletAddress;

        setSpacing(10);
        setPadding(new Insets(15));
        setPrefWidth(250);
        setStyle(
                "-fx-background-color: #2A2A3D;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 8, 0, 2, 2);"
        );

        // Wallet address (shortened)
        Label addressLabel = new Label(shortenAddress(walletAddress));
        addressLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");

        // Style for balance labels
        for (Label lbl : new Label[]{ethBalanceLabel, usdcBalanceLabel, maticBalanceLabel}) {
            lbl.setStyle("-fx-text-fill: #E0E0E0; -fx-font-size: 13px;");
        }

        VBox balancesBox = new VBox(5, ethBalanceLabel, usdcBalanceLabel, maticBalanceLabel);

        getChildren().addAll(addressLabel, balancesBox);

        // 🔥 Load balances in background
        fetchBalances();
    }

    private void fetchBalances() {
        Thread fetchThread = new Thread(() -> {
            try {
                Map<String, String> balances = BalanceChecker.getBalances(walletAddress);

                Platform.runLater(() -> {
                    ethBalanceLabel.setText("ETH: " + balances.getOrDefault("ETH", "0"));
                    usdcBalanceLabel.setText("USDC: " + balances.getOrDefault("USDC", "0"));
                    maticBalanceLabel.setText("MATIC: " + balances.getOrDefault("MATIC", "0"));
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    ethBalanceLabel.setText("ETH: error");
                    usdcBalanceLabel.setText("USDC: error");
                    maticBalanceLabel.setText("MATIC: error");
                });
            }
        });

        fetchThread.setDaemon(true);
        fetchThread.start();
    }

    private String shortenAddress(String address) {
        if (address == null || address.length() <= 10) return address;
        return address.substring(0, 6) + "..." + address.substring(address.length() - 4);
    }
}
