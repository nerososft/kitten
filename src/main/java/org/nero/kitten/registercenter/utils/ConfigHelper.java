package org.nero.kitten.registercenter.utils;

import com.sun.javafx.fxml.PropertyNotFoundException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/15
 * Time   上午10:49
 */
public class ConfigHelper {


    public static Properties loadProperties(String configName) {
        Properties properties = new Properties();

        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = new FileInputStream(System.getProperty("user.dir")+"/src/main/resources/"+configName);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            String[] lines;
            while ((line = bufferedReader.readLine()) != null) {

                if ("".equals(line)) continue;
                if (line.startsWith("#")) continue;

                line = line.trim();
                lines = line.split("=");

                properties.setProperty(lines[0], lines[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStreamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        return properties;
    }

}
