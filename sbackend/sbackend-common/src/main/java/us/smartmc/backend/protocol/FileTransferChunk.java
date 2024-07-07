package us.smartmc.backend.protocol;

import java.io.*;

public class FileTransferChunk implements Serializable {

    private final int transferId;
    private final byte[] buffer;
    private final int bytesRead;

    public FileTransferChunk(int transferId, byte[] buffer, int bytesRead) {
        this.transferId = transferId;
        this.buffer = buffer;
        this.bytesRead = bytesRead;
    }

    public int getTransferId() {
        return transferId;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public int getBytesRead() {
        return bytesRead;
    }

    public FileTransferRegistrar getRegistrar() {
        return FileTransferRegistrar.get(transferId);
    }
}
