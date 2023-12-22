package us.smartmc.core.pluginsapi.util;

import org.bson.Document;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@SuppressWarnings("unused")
public class ConfigUtil {

    public static Document convertFileToDocument(String filePath) {
        try {
            // READ FILE
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();

            // CONVERT TO DOCUMENT
            String fileContent = stringBuilder.toString();
            return Document.parse(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
