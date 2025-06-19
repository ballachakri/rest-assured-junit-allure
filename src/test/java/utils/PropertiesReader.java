package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    public static Properties readProperties() throws IOException {

        Properties properties = new Properties();

        FileInputStream fis = new FileInputStream("src/test/resources/test.properties");
        properties.load(fis);
        return properties;

    }
}
