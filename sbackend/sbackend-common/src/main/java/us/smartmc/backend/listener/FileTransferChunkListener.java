package us.smartmc.backend.listener;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.instance.FileTransferJoiner;
import us.smartmc.backend.protocol.FileTransferChunk;
import us.smartmc.backend.protocol.FileTransferRegistrar;

public class FileTransferChunkListener extends BackendObjectListener<FileTransferChunk> {

    @Override
    public void onReceive(ConnectionHandler connection, FileTransferChunk chunk) {
        long id = chunk.getRegistrar().getId();
        FileTransferRegistrar registrar = FileTransferRegistrar.get(id);
        if (registrar == null) {
            System.out.println("File chunk received without registry! Id=" + id);
            return;
        }
        FileTransferJoiner joiner = FileTransferJoiner.getTransferJoiner(id);
        joiner.handleChunk(chunk);
    }
}
