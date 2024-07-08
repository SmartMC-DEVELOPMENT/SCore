package us.smartmc.backend.listener;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.handler.FileRegistrarManager;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.instance.filetransfer.FileTransferType;
import us.smartmc.backend.protocol.FileDownloadRegistrar;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FileDownloadListener extends BackendObjectListener<FileDownloadRegistrar> {

    @Override
    public void onReceive(ConnectionHandler connection, FileDownloadRegistrar registrar) {
        FileRegistrarManager.register(registrar);
        try {
            Method method = ConnectionHandler.class.getDeclaredMethod("sendFileWithId", long.class, File.class, FileTransferType.class, String.class);
            method.setAccessible(true);
            method.invoke(connection, registrar.getId(), new File(registrar.getOriginPath()), registrar.getType(), registrar.getDestinationPath());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
