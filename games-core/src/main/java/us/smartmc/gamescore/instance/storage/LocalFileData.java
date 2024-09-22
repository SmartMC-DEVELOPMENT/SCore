package us.smartmc.gamescore.instance.storage;

import us.smartmc.gamescore.instance.storage.loader.IDataLoader;
import us.smartmc.gamescore.instance.storage.saver.DataFileSaver;

import java.io.File;
import java.nio.file.Path;

public abstract class LocalFileData<Loader extends IDataLoader<File>, Saver extends DataFileSaver> extends Data<Loader, Saver, File> implements ILocalFileConfig<Loader, Saver, File> {

    protected final File file;

    public LocalFileData(File file) {
        this.file = file;
    }

    @Override
    public Path getPath() {
        return file.getAbsoluteFile().toPath();
    }

    @Override
    public File getParentFile() {
        return file.getParentFile();
    }

    @Override
    public File getAbsoluteFile() {
        return file.getAbsoluteFile();
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public File getDataLoaderObject() {
        return file;
    }
}
