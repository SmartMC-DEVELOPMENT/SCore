package us.smartmc.gamescore.instance.storage;

import us.smartmc.gamescore.instance.storage.loader.JsonLoader;
import us.smartmc.gamescore.instance.storage.saver.JsonSaver;

import java.io.File;

public class JsonData extends LocalFileData<JsonLoader, JsonSaver> {

    public JsonData(File file) {
        super(file);
    }

    @Override
    public JsonSaver getSaverInstance() {
        return new JsonSaver(file);
    }

    @Override
    public JsonLoader getLoaderInstance() {
        return new JsonLoader();
    }
}
