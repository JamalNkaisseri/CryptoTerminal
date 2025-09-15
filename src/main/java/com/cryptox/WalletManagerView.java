package com.cryptox;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class WalletManagerView extends VBox {

    private final Map<String, String> walletMap = new LinkedHashMap<>();
    private final FlowPane walletGrid = new FlowPane();

    public WalletManagerView() {
        setSpacing(20);
        setPadding(new Insets(20));
        setStyle("-fx-background-color: #1E1E2E;");

        // Title
        Label title = new Label("Wallet Manager");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        // Load wallets from storage
        walletMap.putAll(WalletStorage.loadWallets());

        // Inputs
        TextField nameInput = new TextField();
        nameInput.setPromptText("Wallet name");

        TextField addressInput = new TextField();
        addressInput.setPromptText("Wallet address (0x...)");

        Button addButton = new Button("Add Wallet");
        addButton.getStyleClass().add("primary-btn");

        addButton.setOnAction(e -> {
            String name = nameInput.getText().trim();
            String addr = addressInput.getText().trim();

            if (name.isEmpty() || !addr.startsWith("0x") || addr.length() != 42) {
                ThemedDialog.show(
                        "Invalid Input",
                        "Please enter a valid wallet name and address.",
                        null,
                        "OK",
                        null,
                        null
                );
                return;
            }

            walletMap.put(name, addr);
            WalletStorage.saveWallets(walletMap);
            addWalletCard(name, addr);

            nameInput.clear();
            addressInput.clear();
        });

        HBox inputRow = new HBox(10, nameInput, addressInput, addButton);
        inputRow.setAlignment(Pos.CENTER_LEFT);

        // Wallet cards grid
        walletGrid.setHgap(20);
        walletGrid.setVgap(20);
        walletGrid.setAlignment(Pos.TOP_LEFT);

        // Load existing wallets into cards
        walletMap.forEach(this::addWalletCard);

        getChildren().addAll(title, inputRow, walletGrid);
    }

    private void addWalletCard(String name, String address) {
        WalletCard card = new WalletCard(address);
        card.setPrefWidth(220);

        // Wallet title row with delete button
        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 13px; -fx-font-weight: bold;");

        Button deleteBtn = new Button("✖");
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: red; -fx-font-size: 14px;");

        // Wrapper for the whole wallet card (declare first so we can reference in delete action)
        VBox wrapper = new VBox(5);
        wrapper.setAlignment(Pos.TOP_LEFT);

        deleteBtn.setOnAction(e -> {
            ThemedDialog.Result result = ThemedDialog.show(
                    "Confirm Delete",
                    "Are you sure you want to delete wallet \"" + name + "\"?",
                    null,
                    "Delete",
                    "Keep",
                    "Cancel"
            );

            if (result == ThemedDialog.Result.YES) {
                walletMap.remove(name);
                WalletStorage.saveWallets(walletMap);
                walletGrid.getChildren().remove(wrapper);
            }
        });

        HBox header = new HBox(5, nameLabel, deleteBtn);
        header.setAlignment(Pos.CENTER_LEFT);

        wrapper.getChildren().addAll(header, card);
        walletGrid.getChildren().add(wrapper);
    }
}
