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
        try {
            Map dataMap = gson.fromJson(new BufferedReader(new FileReader(file.getAbsolutePath())), HashMap.class);
            if (dataMap != null) data = (HashMap<String, Object>) dataMap;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        System.out.println("Saving " + data);
        try {
            gson.toJson(data, new FileWriter(file.getAbsolutePath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
