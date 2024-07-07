package us.smartmc.backend.instance;

import lombok.Getter;
import us.smartmc.backend.instance.cache.CacheFile;
import us.smartmc.backend.instance.filetransfer.FileTransferType;
import us.smartmc.backend.protocol.FileTransferChunk;
import us.smartmc.backend.protocol.FileTransferRegistrar;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileTransferJoiner {

    private static final Map<Integer, FileTransferJoiner> joiners = new HashMap<>();

    @Getter
    private final int transferId;

    private final StringBuilder stringBuilder = new StringBuilder();
    private final FileOutputStream fos;
    @Getter
    private boolean terminated = false;

    @Getter
    private final FileTransferType type;

    public FileTransferJoiner(int transferId, String destionationPath) {
        this.transferId = transferId;
        try {
            this.fos = new FileOutputStream(destionationPath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        type = FileTransferRegistrar.get(transferId).getType();
        joiners.put(transferId, this);
    }

    public void terminateTransferation() {
        terminated = true;
        System.out.println("FILE TRANSFER COMPLETED! At = " + FileTransferRegistrar.get(transferId).getDestinationPath() + " with type " + type);

        if (type == FileTransferType.PERMANENT) {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            new CacheFile(FileTransferRegistrar.get(transferId).getFileAbsolutePath(), stringBuilder.toString());
        }
    }

    public void handleChunk(FileTransferChunk chunk) {
        if (chunk.getBuffer().length == 0 && chunk.getBytesRead() == 0) {
            terminateTransferation();
            return;
        }

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

    public static FileTransferJoiner get(int id) {
        if (!joiners.containsKey(id))
            return new FileTransferJoiner(id, FileTransferRegistrar.get(id).getDestinationPath());
        return joiners.get(id);
    }

}
