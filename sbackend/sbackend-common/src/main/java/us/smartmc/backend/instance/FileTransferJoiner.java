package us.smartmc.backend.instance;

import lombok.Getter;
import us.smartmc.backend.handler.FileRegistrarManager;
import us.smartmc.backend.instance.cache.CacheFile;
import us.smartmc.backend.instance.filetransfer.FileTransferType;
import us.smartmc.backend.protocol.FileDownloadRegistrar;
import us.smartmc.backend.protocol.FileTransferChunk;
import us.smartmc.backend.protocol.FileTransferRegistrar;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileTransferJoiner {

    private static final Map<Long, FileTransferJoiner> joiners = new HashMap<>();

    @Getter
    private final long id;

    private final StringBuilder stringBuilder = new StringBuilder();
    private FileOutputStream fos;
    @Getter
    private boolean terminated = false;

    @Getter
    private final FileTransferType type;

    public FileTransferJoiner(long id, String destionationPath) {
        this.id = id;
        type = FileRegistrarManager.getTypeOf(id);
        if (type == FileTransferType.PERMANENT) {
            try {
                new File(destionationPath).getParentFile().mkdirs();
                this.fos = new FileOutputStream(destionationPath);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        joiners.put(id, this);
    }

    public void terminateTransferation() {
        terminated = true;
        System.out.println("FILE TRANSFER COMPLETED! At = " + FileTransferRegistrar.get(id).getDestinationPath() + " with type " + type);
        if (fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            String fileName = FileTransferRegistrar.get(id).getFileName();
            System.out.println("FileName=" + fileName);
            new CacheFile(FileTransferRegistrar.get(id).getFileName(), stringBuilder.toString());
        }
    }

    public void handleChunk(FileTransferChunk chunk) {
        if (type == FileTransferType.PERMANENT) {
            try {
                fos.write(chunk.getBuffer(), 0, chunk.getBytesRead());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            for (byte b : chunk.getBuffer()) {
                stringBuilder.append(b);
            }
        }
    }

    public static FileTransferJoiner getTransferJoiner(long id) {
        if (!joiners.containsKey(id))
            return new FileTransferJoiner(id, FileTransferRegistrar.get(id).getDestinationPath());
        return joiners.get(id);
    }

    public static FileTransferJoiner getDownloadJoiner(long id) {
        if (!joiners.containsKey(id))
            return new FileTransferJoiner(id, FileDownloadRegistrar.get(id).getDestinationPath());
        return joiners.get(id);
    }

}
