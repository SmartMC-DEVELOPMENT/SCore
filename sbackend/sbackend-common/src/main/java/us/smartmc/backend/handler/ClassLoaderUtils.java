package us.smartmc.backend.handler;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import us.smartmc.smartaddons.plugin.PluginDescription;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ClassLoaderUtils {

    public static PluginDescription read(InputStream inputStream) {
        if (inputStream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                String json = content.toString();
                Gson gson = new Gson();
                LinkedTreeMap<String, Object> map = gson.fromJson(json, LinkedTreeMap.class);
                return new PluginDescription((String) map.get("name"), (String) map.get("main"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No se pudo encontrar el archivo JSON dentro del JAR.");
        }
        return null;
    }

}
