package us.smartmc.serverhandler.instance;

import lombok.Getter;

import java.io.*;
import java.util.*;

public class StartupCreation {

    private final Map<String, String> backendValues = new HashMap<>();
    private final Map<String, String> propertiesValues = new HashMap<>();

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
            if (!file.getName().equals("backend.properties")) continue;
            backendValues.putAll(readFileAndValues(file));
        }

        for (File file : Objects.requireNonNull(startupDir.listFiles())) {
            if (!file.getName().equals("server.properties")) continue;
            propertiesValues.putAll(readFileAndValues(file));
        }
    }

    public void completeAndCopy() {
        File file = new File(destinationDir, "backend.properties");
        try {
            file.getParentFile().mkdirs();
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        registerDefaults();
        writeFileAndValues(file, backendValues);

        file = new File(destinationDir, "server.properties");
        try {
            file.getParentFile().mkdirs();
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        writeFileAndValues(file, propertiesValues);
    }

    private void writeFileAndValues(File file, Map<String, String> map) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                    StringBuilder newContent = new StringBuilder();
                    for (String key : map.keySet()) {
                        String value = map.get(key);
                        newContent.append(key).append("=").append(value).append("\n");
                    }
                    writer.write(newContent.toString());
                    writer.flush();
                    writer.close();
                    System.out.println("Writed to " + file.getAbsolutePath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 500);
    }

    public Map<String, String> readFileAndValues(File file) {
        Map<String, String> map = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains("=")) continue;
                String[] args = line.split("=");
                String key = args[0];
                String value = args.length == 1 ? "" : args[1];
                map.put(key, value);
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    public void registerBackendValue(String key, String value) {
        backendValues.put(key, value);
    }

    public void registerServerValue(String key, String value) {
        propertiesValues.put(key, value);
    }

    private void registerDefaults() {
        registerServerValue("server-port", String.valueOf(port));
        registerBackendValue("server-name", name);
        registerBackendValue("server-id", id);
    }

}
