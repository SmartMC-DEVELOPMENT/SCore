package us.smartmc.gamescore.instance.storage;

import us.smartmc.gamescore.instance.storage.loader.YamlLoader;
import us.smartmc.gamescore.instance.storage.saver.YamlSaver;

import java.io.File;

public class YamlData extends LocalFileData<YamlLoader, YamlSaver> {

    public YamlData(File file) {
        super(file);
    }

    @Override
    public YamlSaver getSaverInstance() {
        return new YamlSaver(file);
    }

    @Override
    public YamlLoader getLoaderInstance() {
        return new YamlLoader();
    }
}
