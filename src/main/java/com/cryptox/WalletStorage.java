package com.cryptox;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class WalletStorage {
    private static final String FILE_PATH = "wallets.txt";

    public static void saveWallets(Map<String, String> wallets) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_PATH))) {
            for (Map.Entry<String, String> entry : wallets.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, String> loadWallets() {
        Map<String, String> wallets = new LinkedHashMap<>();
        Path path = Paths.get(FILE_PATH);
        if (Files.exists(path)) {
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", 2);
                    if (parts.length == 2) {
                        wallets.put(parts[0].trim(), parts[1].trim());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return wallets;
    }
}
