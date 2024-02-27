package us.smartmc.serverhandler.instance;

import lombok.Getter;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StartupCreation {

    private final Map<String, String> values = new HashMap<>();

    @Getter
    private final File startupDir, destinationDir;
    private final int port;
    private final String name, id;

    public StartupCreation(File startupDir, File destinationDir, int port, String name, String id) {
        this.startupDir = startupDir;
        this.destinationDir = destinationDir;
        this.port = port;
        this.name = name;
        this.id = id;
        for (File file : Objects.requireNonNull(startupDir.listFiles())) {
            if (!file.getName().equals("server.properties")) continue;
            readFileAndValues(file);
        }
    }

    public void completeAndCopy() {
        File file = new File(destinationDir, "server.properties");
        try {
            file.getParentFile().mkdirs();
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        registerDefaults();
        writeFileAndValues(file);
    }

    private void writeFileAndValues(File file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            StringBuilder newContent = new StringBuilder();
            for (String key : values.keySet()) {
                String value = values.get(key);
                newContent.append(key).append("=").append(value).append("\n");
            }
            writer.write(newContent.toString());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readFileAndValues(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains("=")) continue;
                String[] args = line.split("=");
                String key = args[0];
                String value = args.length == 1 ? "" : args[1];
                values.put(key, value);
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public StartupCreation register(String key, String value) {
        values.put(key, value);
        return this;
    }

    private void registerDefaults() {
        register("server-port", String.valueOf(port));
        register("server-name", name);
        register("server-id", id);
    }

}
