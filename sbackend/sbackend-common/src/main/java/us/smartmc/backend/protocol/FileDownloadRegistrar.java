package us.smartmc.backend.protocol;

import lombok.Getter;
import us.smartmc.backend.connection.FileTransferEnd;
import us.smartmc.backend.handler.FileRegistrarManager;
import us.smartmc.backend.instance.filetransfer.FileTransferType;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FileDownloadRegistrar implements Serializable, IFileRegistrar {

    private final long id;

    private final byte[] fileAbsolutePath;
    private final byte[] destinationPath;
    private final int typeIndex;

    public FileDownloadRegistrar(String originPath, FileTransferType type, String destinationPath) {
        this.fileAbsolutePath = originPath.getBytes(StandardCharsets.UTF_8);
        this.destinationPath = destinationPath.getBytes(StandardCharsets.UTF_8);
        id = (System.currentTimeMillis() / 1000L) * 10000 + new Random().nextInt(10000);
        this.typeIndex = type.ordinal();
        FileRegistrarManager.register(this);
    }

    public String getDestinationPath() {
        return new String(destinationPath, StandardCharsets.UTF_8);
    }

    public String getOriginPath() {
        return new String(fileAbsolutePath, StandardCharsets.UTF_8);
    }

    public String getFileName() {
        return new File(getOriginPath()).getName();
    }

    public String getFileAbsolutePath() {
        return new String(fileAbsolutePath, StandardCharsets.UTF_8);
    }

    public FileTransferType getType() {
        return FileTransferType.values()[typeIndex];
    }

    @Override
    public long getId() {
        return id;
    }

    public static FileDownloadRegistrar get(long id) {
        return (FileDownloadRegistrar) FileRegistrarManager.get(id);
    }

}
