package us.smartmc.backend.instance.config;


import com.google.gson.Gson;

import java.io.*;
import java.util.HashMap;

public class JsonConfig extends FileConfig {

    private final File file;

    public JsonConfig(File file) {
        super(file);
        this.file = file;
    }

    @Override
    public void load() {
        Gson gson = new Gson();
        try {
            data = gson.fromJson(new BufferedReader(new FileReader(file.getAbsolutePath())), HashMap.class);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        Gson gson = new Gson();
        try {
            gson.toJson(data, new FileWriter(file.getAbsolutePath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
