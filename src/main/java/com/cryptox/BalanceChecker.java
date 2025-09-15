package com.cryptox;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class BalanceChecker {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Get ETH balance in a wallet (in Ether).
     */
    public static BigDecimal getEthBalance(String chain, String walletAddress) {
        try {
            String endpoint = RpcConfig.getEndpoint(chain);
            if (endpoint == null) {
                throw new RuntimeException("No endpoint for chain: " + chain);
            }

            // JSON-RPC payload
            String payload = "{"
                    + "\"jsonrpc\":\"2.0\","
                    + "\"method\":\"eth_getBalance\","
                    + "\"params\":[\"" + walletAddress + "\", \"latest\"],"
                    + "\"id\":1"
                    + "}";

            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes(StandardCharsets.UTF_8));
            }

            JsonNode response = mapper.readTree(conn.getInputStream());
            String resultHex = response.get("result").asText();

            BigInteger wei = new BigInteger(resultHex.substring(2), 16); // strip "0x"
            return new BigDecimal(wei).divide(BigDecimal.TEN.pow(18)); // wei → ETH
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    /**
     * Get ERC20 token balance (like USDC).
     */
    public static BigDecimal getTokenBalance(String chain, String walletAddress, String tokenAddress, int decimals) {
        try {
            String endpoint = RpcConfig.getEndpoint(chain);
            if (endpoint == null) {
                throw new RuntimeException("No endpoint for chain: " + chain);
            }

            // balanceOf(address) → 0x70a08231 + 32-byte padded address
            String methodId = "0x70a08231";
            String addressNoPrefix = walletAddress.startsWith("0x") ? walletAddress.substring(2) : walletAddress;
            String paddedAddress = String.format("%064x", new BigInteger(addressNoPrefix, 16));

            String data = methodId + paddedAddress;

            String payload = "{"
                    + "\"jsonrpc\":\"2.0\","
                    + "\"method\":\"eth_call\","
                    + "\"params\":[{"
                    + "\"to\":\"" + tokenAddress + "\","
                    + "\"data\":\"" + data + "\"},"
                    + "\"latest\"],"
                    + "\"id\":1"
                    + "}";

            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes(StandardCharsets.UTF_8));
            }

            JsonNode response = mapper.readTree(conn.getInputStream());
            String resultHex = response.get("result").asText();

            if (resultHex == null || resultHex.equals("0x")) return BigDecimal.ZERO;

            BigInteger raw = new BigInteger(resultHex.substring(2), 16);
            return new BigDecimal(raw).divide(BigDecimal.TEN.pow(decimals));
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    /**
     * Get balances for ETH + USDC.
     */
    public static Map<String, String> getBalances(String walletAddress) {
        Map<String, String> balances = new HashMap<>();

        // ETH (Base mainnet)
        BigDecimal ethBalance = getEthBalance("base", walletAddress);
        balances.put("ETH", ethBalance.toPlainString());

        // USDC (Base mainnet contract address)
        String usdcAddress = "0x833589fCD6EDb6E08f4c7C32D4f71b54bDA02913"; // USDC Base Mainnet
        BigDecimal usdcBalance = getTokenBalance("base", walletAddress, usdcAddress, 6);
        balances.put("USDC", usdcBalance.toPlainString());

        return balances;
    }
}
