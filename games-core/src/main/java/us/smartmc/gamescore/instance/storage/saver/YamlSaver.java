package us.smartmc.gamescore.instance.storage.saver;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class YamlSaver extends DataFileSaver {

    public YamlSaver(File file) {
        super(file);
    }

    @Override
    public void save(boolean async, Map<String, Object> data) {

        Runnable action = () -> {
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);

            try (FileWriter fileWriter = new FileWriter(file)) {
                yaml.dump(data, fileWriter);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        if (async)  {
            CompletableFuture.runAsync(action).exceptionally(ex -> {
                // Manejo de excepciones
                ex.printStackTrace();
                return null;
            });
            return;
        }
        action.run();
    }
}
