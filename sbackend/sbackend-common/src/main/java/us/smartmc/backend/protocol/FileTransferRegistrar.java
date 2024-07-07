package us.smartmc.backend.protocol;

import lombok.Getter;
import us.smartmc.backend.instance.filetransfer.FileTransferType;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class FileTransferRegistrar implements Serializable {

    private static final Map<Integer, FileTransferRegistrar> registries = new HashMap<>();

    private static int currentId = 0;

    @Getter
    private final int transferId;

    @Getter
    private final long length;
    private final byte[] fileAbsolutePath;
    private final byte[] fileName;
    private final int typeIndex;

    private byte[] destinationPath;

    public FileTransferRegistrar(File file, FileTransferType type) {
        this.length = file.length();
        this.fileAbsolutePath = file.getAbsolutePath().getBytes(StandardCharsets.UTF_8);
        this.fileName = file.getName().getBytes(StandardCharsets.UTF_8);
        transferId = currentId;
        this.typeIndex = type.ordinal();
        currentId++;
        destinationPath = null;
        registries.put(transferId, this);
    }

    public FileTransferRegistrar(File file, FileTransferType type, String destionationPath) {
        this(file, type);
        this.destinationPath = destionationPath.getBytes(StandardCharsets.UTF_8);;
    }

    public String getDestinationPath() {
        if (destinationPath == null) return null;
        return new String(destinationPath, StandardCharsets.UTF_8);
    }

    public String getFileName() {
        return new String(fileName, StandardCharsets.UTF_8);
    }

    public String getFileAbsolutePath() {
        return new String(fileAbsolutePath, StandardCharsets.UTF_8);
    }

    public FileTransferType getType() {
        return FileTransferType.values()[typeIndex];
    }

    public File getDestinationFile() {
        if (getType() == FileTransferType.CACHE) {
            return new File(System.getProperty("java.io.tmpdir"), getFileName());
        } else {
            return new File(getDestinationPath());
        }
    }

    public static void register(FileTransferRegistrar registrar) {
        registries.put(registrar.transferId, registrar);
    }

    public static FileTransferRegistrar get(int id) {
        return registries.get(id);
    }

}
