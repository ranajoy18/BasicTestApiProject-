package com.api.testing.Common;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties = new Properties();

    static {
        try (InputStream is =
                 ConfigReader.class
                     .getClassLoader()
                     .getResourceAsStream("src/main/test/java/com/api/resources/config.properties")) {

            if (is == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }

            properties.load(is);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}


