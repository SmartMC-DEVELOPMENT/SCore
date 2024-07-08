package us.smartmc.backend.listener;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.connection.FileTransferEnd;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.instance.FileTransferJoiner;

public class FileTransferEndListener extends BackendObjectListener<FileTransferEnd> {

    @Override
    public void onReceive(ConnectionHandler connection, FileTransferEnd end) {
        long id = end.getId();
        FileTransferJoiner.getTransferJoiner(id).terminateTransferation();
        connection.completeDownload(end);
    }
}
