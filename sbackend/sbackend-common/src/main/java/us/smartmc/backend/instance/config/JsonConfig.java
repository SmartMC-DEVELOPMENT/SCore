package us.smartmc.backend.instance.config;


import com.google.common.reflect.TypeToken;
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
            Map<String, Object> dataMap = gson.fromJson(reader, new TypeToken<HashMap<String, Object>>(){}.getType());
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
        System.out.println("Saving " + data);
        try (FileWriter writer = new FileWriter(file.getAbsolutePath())) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
