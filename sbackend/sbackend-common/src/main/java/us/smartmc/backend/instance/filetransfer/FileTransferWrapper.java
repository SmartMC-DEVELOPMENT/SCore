package us.smartmc.backend.instance.filetransfer;

import lombok.Getter;
import us.smartmc.backend.handler.FileRegistrarManager;
import us.smartmc.backend.instance.cache.CacheFile;

import java.io.File;

public class FileTransferWrapper {

    @Getter
    private final long id;

    @Getter
    private final String originPath;
    private final String destinationPath;

    public FileTransferWrapper(long id, String originPath, String destinationPath) {
        this.id = id;
        this.originPath = originPath;
        this.destinationPath = destinationPath;
    }

    public FileTransferType getType() {
        return FileRegistrarManager.getTypeOf(id);
    }

    public CacheFile getCacheFile() {
        if (getType() == FileTransferType.CACHE) return CacheFile.get(destinationPath);
        return null;
    }

    public File getFile() {
        if (getType() == FileTransferType.PERMANENT) return new File(destinationPath);
        return null;
    }

    public boolean isFile() {
        return getType().equals(FileTransferType.PERMANENT);
    }

    public boolean isCacheFile() {
        return getType().equals(FileTransferType.CACHE);
    }
}
