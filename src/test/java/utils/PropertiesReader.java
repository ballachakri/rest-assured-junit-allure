package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    public static Properties readProperties() throws IOException {

        Properties properties = new Properties();
        File file = new File("src/test/resources/test.properties");
        FileInputStream fis = new FileInputStream(file);
        properties.load(fis);
        return properties;

    }
}
