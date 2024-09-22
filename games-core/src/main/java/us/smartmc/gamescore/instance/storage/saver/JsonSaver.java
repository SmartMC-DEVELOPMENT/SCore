package us.smartmc.gamescore.instance.storage.saver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class JsonSaver extends DataFileSaver {

    public JsonSaver(File file) {
        super(file);
    }

    @Override
    public void save(boolean async, Map<String, Object> data) {

        Runnable action = () -> {
            Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(file)) { // try-with-resources para cerrar el escritor automáticamente
                gson.toJson(data, writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        if (async) {
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
