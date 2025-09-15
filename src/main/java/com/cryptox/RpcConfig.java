package com.cryptox;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RpcConfig {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = RpcConfig.class.getResourceAsStream("/rpc_endpoints.properties")) {
            if (input == null) {
                throw new RuntimeException("rpc_endpoints.properties not found in resources!");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load RPC endpoints config", e);
        }
    }

    public static String getEndpoint(String chain) {
        return props.getProperty(chain);
    }
}
