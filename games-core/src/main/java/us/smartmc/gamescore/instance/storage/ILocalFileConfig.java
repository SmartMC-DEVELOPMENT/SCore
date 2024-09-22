package us.smartmc.gamescore.instance.storage;

import us.smartmc.gamescore.instance.storage.loader.IDataLoader;
import us.smartmc.gamescore.instance.storage.saver.IDataSaver;

import java.io.File;
import java.nio.file.Path;

public interface ILocalFileConfig<Loader extends IDataLoader<DataLoaderObject>, Saver extends IDataSaver, DataLoaderObject> extends IData<Loader, Saver, DataLoaderObject> {

    Path getPath();

    File getParentFile();
    File getAbsoluteFile();
}

