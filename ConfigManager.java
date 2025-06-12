import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private static final String CONFIG_FILE = "config.properties";
    private Properties properties;

    public ConfigManager() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
            // Set default values
            properties.setProperty("server.url", "http://max.ge/q45/69384751/index.php");
            properties.setProperty("chatbot.name", "JavaChatBot");
        }
    }

    public String getServerUrl() {
        return properties.getProperty("server.url");
    }

    public String getChatBotName() {
        return properties.getProperty("chatbot.name");
    }
}