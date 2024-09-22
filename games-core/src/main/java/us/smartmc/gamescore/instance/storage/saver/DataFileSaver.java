package us.smartmc.gamescore.instance.storage.saver;

import java.io.File;

public abstract class DataFileSaver implements IDataSaver {

    protected File file;

    public DataFileSaver(File file) {
        this.file = file;
    }
}
