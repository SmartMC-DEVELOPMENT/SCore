package us.smartmc.backend.listener;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.handler.CacheManager;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.instance.FileTransferJoiner;
import us.smartmc.backend.instance.cache.CacheCommand;
import us.smartmc.backend.protocol.CacheCommandRequest;
import us.smartmc.backend.protocol.FileTransferChunk;
import us.smartmc.backend.protocol.FileTransferRegistrar;

public class FileTransferChunkListener extends BackendObjectListener<FileTransferChunk> {

    @Override
    public void onReceive(ConnectionHandler connection, FileTransferChunk chunk) {
        int transferId = chunk.getTransferId();
        FileTransferRegistrar registrar = FileTransferRegistrar.get(transferId);
        if (registrar == null) {
            System.out.println("File chunk received without registry! Id=" + transferId);
            return;
        }
        FileTransferJoiner joiner = FileTransferJoiner.get(transferId);
        joiner.handleChunk(chunk);
    }
}
