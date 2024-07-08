package us.smartmc.backend.protocol;

import java.io.*;

public class FileTransferChunk implements Serializable {

    private final long id;
    private final byte[] buffer;
    private final int bytesRead;

    public FileTransferChunk(long id, byte[] buffer, int bytesRead) {
        this.id = id;
        this.buffer = buffer;
        this.bytesRead = bytesRead;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public int getBytesRead() {
        return bytesRead;
    }

    public FileTransferRegistrar getRegistrar() {
        return FileTransferRegistrar.get(id);
    }
}
