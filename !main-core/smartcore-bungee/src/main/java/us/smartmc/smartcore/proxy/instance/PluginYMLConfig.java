package us.smartmc.smartcore.proxy.instance;

import net.md_5.bungee.api.ProxyServer;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import us.smartmc.smartcore.proxy.SmartCoreBungeeCord;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class PluginYMLConfig {

    private final File file;
    private Map<String, Object> config;
    private final Yaml yaml;

    public PluginYMLConfig(ProxyServer server, String fileName) {
        this.file = new File(SmartCoreBungeeCord.getPlugin().getDataFolder(), fileName);

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

    public Map<String, Object> getConfig() {
        return config;
    }
}
