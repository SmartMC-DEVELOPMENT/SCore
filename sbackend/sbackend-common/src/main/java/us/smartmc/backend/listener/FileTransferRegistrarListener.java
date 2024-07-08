package us.smartmc.backend.listener;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.handler.FileRegistrarManager;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.protocol.FileTransferRegistrar;

public class FileTransferRegistrarListener extends BackendObjectListener<FileTransferRegistrar> {

    @Override
    public void onReceive(ConnectionHandler connection, FileTransferRegistrar registrar) {
        System.out.println("Received FileTransferRegistrar " + registrar);
        FileRegistrarManager.register(registrar);
    }
}
