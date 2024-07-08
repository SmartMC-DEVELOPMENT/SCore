package us.smartmc.backend.connection;

import lombok.Getter;
import us.smartmc.backend.handler.FileRegistrarManager;
import us.smartmc.backend.instance.filetransfer.FileTransferType;
import us.smartmc.backend.protocol.IFileRegistrar;

import java.io.Serializable;

@Getter
public class FileTransferEnd implements Serializable, IFileRegistrar {

    private final long id;

    public FileTransferEnd(long id) {
        this.id = id;
    }

    @Override
    public String getOriginPath() {
        return FileRegistrarManager.get(id).getOriginPath();
    }

    @Override
    public String getDestinationPath() {
        return FileRegistrarManager.get(id).getDestinationPath();
    }

    @Override
    public String getFileName() {
        return FileRegistrarManager.get(id).getFileName();
    }

    @Override
    public FileTransferType getType() {
        return FileRegistrarManager.get(id).getType();
    }

    @Override
    public long getId() {
        return id;
    }
}
