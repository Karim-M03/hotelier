package shared;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private Properties serverProperties;
    private Properties clientProperties;

    public ConfigManager() {
        serverProperties = new Properties();
        clientProperties = new Properties();
        loadProperties("./src/main/resources/configServer.properties", serverProperties);
        loadProperties("./src/main/resources/configClient.properties", clientProperties);
    }

    private void loadProperties(String fileName, Properties properties) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + fileName);
                return;
            }

            // Load a properties file from class path, inside static method
            properties.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getServerProperty(String key) {
        return serverProperties.getProperty(key);
    }

    public String getClientProperty(String key) {
        return clientProperties.getProperty(key);
    }

    public String getServerHost() {
        return serverProperties.getProperty("server.host");
    }

    public int getServerPort() {
        return Integer.parseInt(serverProperties.getProperty("server.port"));
    }

    public int getServerThreads() {
        return Integer.parseInt(serverProperties.getProperty("server.threads"));
    }

    public int getAwaitTime() {
        return Integer.parseInt(serverProperties.getProperty("server.awaitTime"));
    }

    public String getClientUsername() {
        return clientProperties.getProperty("client.username");
    }

    public String getClientPassword() {
        return clientProperties.getProperty("client.password");
    }
}
