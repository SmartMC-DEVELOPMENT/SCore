package us.smartmc.backend.protocol;

import lombok.Getter;
import us.smartmc.backend.handler.FileRegistrarManager;
import us.smartmc.backend.instance.filetransfer.FileTransferType;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class FileTransferRegistrar implements Serializable, IFileRegistrar {

    private long id;

    @Getter
    private final long length;
    private final byte[] originAbsolutePath;
    private final byte[] fileName;
    private final int typeIndex;

    private byte[] destinationPath;

    public FileTransferRegistrar(File file, FileTransferType type) {
        this.length = file.length();
        this.originAbsolutePath = file.getAbsolutePath().getBytes(StandardCharsets.UTF_8);
        this.fileName = file.getName().getBytes(StandardCharsets.UTF_8);
        id = (System.currentTimeMillis() / 1000L) * 10000 + new Random().nextInt(10000);
        this.typeIndex = type.ordinal();
        destinationPath = null;
        FileRegistrarManager.register(this);
    }

    public FileTransferRegistrar(File file, FileTransferType type, String destionationPath) {
        this(file, type);
        this.destinationPath = destionationPath.getBytes(StandardCharsets.UTF_8);;
    }

    public FileTransferRegistrar(long id, File file, FileTransferType type, String destionationPath) {
        this(file, type);
        this.id = id;
        System.out.println("CALLED WITH ID REGISTRAR " + id);
        this.destinationPath = destionationPath.getBytes(StandardCharsets.UTF_8);;
    }



    public String getDestinationPath() {
        if (destinationPath == null) return null;
        return new String(destinationPath, StandardCharsets.UTF_8);
    }

    public String getFileName() {
        return new String(fileName, StandardCharsets.UTF_8);
    }

    public String getOriginPath() {
        return new String(originAbsolutePath, StandardCharsets.UTF_8);
    }

    public FileTransferType getType() {
        return FileTransferType.values()[typeIndex];
    }

    @Override
    public long getId() {
        return id;
    }

    public static FileTransferRegistrar get(long id) {
        return (FileTransferRegistrar) FileRegistrarManager.get(id);
    }

}
