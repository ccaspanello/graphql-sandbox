package io.javamonkey.github;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;

public class TokenUtil {

    public static String findToken() {
        try {
            InputStream testProperties = Main.class.getClassLoader().getResourceAsStream("test.properties");
            Properties properties = new Properties();
            properties.load(testProperties);
//            String tokenProperty = properties.get("github.token").toString();
//            return new String(Base64.getDecoder().decode(Base64.getDecoder().decode(tokenProperty)));
            return properties.get("github.token").toString();
        } catch (IOException e) {
            throw new RuntimeException("Unexpected error reading token.", e);
        }
    }

}
