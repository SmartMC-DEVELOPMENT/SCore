package us.smartmc.bmotd.instance;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginYMLConfig {

    private final File file;
    private Map<String, Object> config;
    private final Yaml yaml;

    public PluginYMLConfig(File file) {
        this.file = file;
        // Configura SnakeYAML para cargar y guardar la configuración
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yaml = new Yaml(options);

        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Error al crear el archivo de configuración", e);
            }
        }
        load();
    }

    public int getInt(String path) {
        return (int) get(path);
    }

    public Map<String, Object> getSection(String path) {
        String[] parts = path.split("\\.");
        Map<String, Object> currentSection = config;

        for (int i = 0; i < parts.length; i++) {
            Object part = currentSection.get(parts[i]);

            if (i == parts.length - 1) {
                return (Map<String, Object>) part;
            } else if (part instanceof Map<?, ?>) {
                Map<String, Object> mapPart = (Map<String, Object>) part;
                currentSection = mapPart;
            } else {
                return null;
            }
        }
        return null;
    }

    public Object get(String path) {
        String[] parts = path.split("\\.");
        Map<String, Object> currentSection = config;

        for (int i = 0; i < parts.length; i++) {
            Object part = currentSection.get(parts[i]);

            if (i == parts.length - 1) {
                return part;
            } else if (part instanceof Map<?, ?>) {
                Map<String, Object> mapPart = (Map<String, Object>) part;
                currentSection = mapPart;
            } else {
                return null;
            }
        }
        return null;
    }

    public boolean getBoolean(String path) {
        return (boolean) get(path);
    }

    public String getString(String path) {
        return (String) get(path);
    }

    public List<String> getStringList(String path) {
        return (List<String>) get(path);
    }

    public void set(String key, Object value) {
        String[] parts = key.split("\\.");
        Map<String, Object> currentSection = config;

        for (int i = 0; i < parts.length - 1; i++) {
            currentSection = (Map<String, Object>) currentSection.computeIfAbsent(parts[i], k -> new HashMap<String, Object>());
        }

        currentSection.put(parts[parts.length - 1], value);
    }


    public boolean contains(String path) {
        return get(path) != null;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(file)) {
            yaml.dump(config, writer);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo de configuración", e);
        }
    }

    @SuppressWarnings("unchecked")
    private void load() {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            config = yaml.loadAs(inputStream, Map.class);
            if (config == null) { // Si el archivo está vacío, inicializar config
                config = Map.of();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar el archivo de configuración", e);
        }
    }
}
