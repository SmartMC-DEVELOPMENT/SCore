package us.smartmc.backend.instance.config;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class JsonConfig extends FileConfig {

    public JsonConfig(File file) {
        super(file);
    }

    @Override
    public void load() {
        Gson gson = new Gson();
        try (BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            // Leemos el JSON como un Map sin TypeToken
            Map<String, Object> dataMap = gson.fromJson(reader, Map.class);
            if (dataMap != null) {
                data = new HashMap<>(dataMap);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        try (FileWriter writer = new FileWriter(file.getAbsolutePath())) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
