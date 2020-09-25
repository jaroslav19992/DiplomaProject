package TestMaker.LoginWindow.NetworkSettings;

import java.io.*;
import java.util.Properties;

public class NetworkSettingsConfigsReader {
    private static Properties properties;

    public NetworkSettingsConfigsReader() throws IOException {
        properties = new Properties();
        properties.load(new FileInputStream(new File("src/TestMaker/Assets/configs/configs.ini")));
    }

    /**
     * Get some config from file
     * @param key config name
     * @return config data
     */
    public String getConfig(String key) {
        return properties.getProperty(key);
    }

    /**
     * Set some config
     * @param key config name
     * @param value config data
     */
    public void setConfig(String key, String value) {
        properties.setProperty(key, value);
        System.out.println("properties. setProperty " + key + " " + value);
    }

    /**
     * Write TestMaker.Assets.configs into configurations file
     */
    public void writeConfigs(){
        try {
            properties.store(new FileOutputStream("src/TestMaker.Assets.configs/TestMaker.Assets.configs.ini"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
