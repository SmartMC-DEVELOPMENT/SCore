package us.smartmc.backend.connection;

import lombok.Getter;
import us.smartmc.backend.command.ServiceCommand;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.instance.cache.CacheCommand;
import us.smartmc.backend.instance.cache.CacheCommandType;
import us.smartmc.backend.instance.filetransfer.FileTransferType;
import us.smartmc.backend.instance.filetransfer.FileTransferWrapper;
import us.smartmc.backend.instance.messaging.MessageCommand;
import us.smartmc.backend.listener.*;
import us.smartmc.backend.protocol.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@Getter
public class ConnectionHandler implements Runnable {

    private static boolean defaultListenersAlreadyRegistered;

    @Getter
    protected Socket connection;

    protected final ConnectionOutputStream outputStream;
    protected final ConnectionInputStream inputStream;

    private final Map<String, Set<Consumer<FileTransferWrapper>>> downloadsRunning = new HashMap<>();

    public ConnectionHandler(Socket socket) throws IOException {
        this.connection = socket;
        this.outputStream = new ConnectionOutputStream(this, connection.getOutputStream());
        this.inputStream = new ConnectionInputStream(this, connection.getInputStream());
        registerDefaultListeners();
        registerDefaultCommands();
    }

    private static void registerDefaultCommands() {
        ConnectionInputManager.registerCommands(new ServiceCommand());
    }

    private static void registerDefaultListeners() {
        if (defaultListenersAlreadyRegistered) return;
        ConnectionInputManager
                .registerListeners(
                        new CommandHandlerListener(),
                        new CacheCommandListener(),
                        new MessagingChannelListener(),
                        new FileTransferRegistrarListener(),
                        new FileTransferChunkListener(),
                        new FileTransferEndListener(),
                        new FileDownloadListener());
        defaultListenersAlreadyRegistered = true;
    }

    @Override
    public void run() {
        try {
            while (!connection.isClosed()) {
                Object object = inputStream.readObject();
                if (object instanceof CommandRequest request) {
                    ConnectionInputManager.performCommand(this, request);
                    continue;
                }
                ConnectionInputManager.performListener(this, object);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void downloadFile(String originPath, FileTransferType type, String destinationPath, Consumer<FileTransferWrapper>... consumers) {
        try {
            FileDownloadRegistrar registrar = new FileDownloadRegistrar(originPath, type, destinationPath);
            System.out.println("DOWNLOAD FILE ID " + registrar.getId());
            outputStream.writeObject(registrar);
            downloadsRunning.put(destinationPath, Set.of(consumers));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void completeDownload(FileTransferEnd registrar) {
        FileTransferWrapper wrapper = new FileTransferWrapper(registrar.getId(), registrar.getOriginPath(), registrar.getDestinationPath());
        Set<Consumer<FileTransferWrapper>> consumers = downloadsRunning.get(registrar.getDestinationPath());
        if (consumers == null) return;
        consumers.forEach(c -> c.accept(wrapper));
    }

    private void sendFileWithId(long id, File file, FileTransferType type, String destionationPath) {
        try {
            long start = System.currentTimeMillis();
            // Enviar el registrador primero
            ObjectOutputStream oos = outputStream.getOut();
            System.out.println(start);

            FileTransferRegistrar registrar = id == -1 ? new FileTransferRegistrar(file, type, destionationPath) :
                    new FileTransferRegistrar(id, file, type, destionationPath);

            oos.writeObject(registrar);

            try (FileInputStream fis = new FileInputStream(file.getAbsolutePath())) {
                byte[] buffer = new byte[1024 * 64];
                int bytesRead;

                while ((bytesRead = fis.read(buffer)) != -1) {
                    // Crear una copia del buffer con los datos leídos
                    byte[] chunkData = new byte[bytesRead];
                    System.arraycopy(buffer, 0, chunkData, 0, bytesRead);

                    FileTransferChunk chunk = new FileTransferChunk(registrar.getId(), chunkData, bytesRead);
                    oos.writeObject(chunk);
                }

                // Indicar el fin de la transferencia
                oos.writeObject(new FileTransferEnd(registrar.getId()));
                long end = System.currentTimeMillis();
                System.out.println("File transfer completed.");
                System.out.println("DIFF " + (end - start));
            }
        } catch (IOException e) {
            System.out.println("Error while trying to sendFile method at writing registry!");
            throw new RuntimeException(e);
        }
    }

    public void sendFile(File file, FileTransferType type, String destionationPath) {
        sendFileWithId(-1, file, type, destionationPath);
    }

    public void publishMessage(String id, String message) {
        sendObject(new MessageCommand(id, message));
    }

    public void registerCache(String key, Object value) {
        sendObject(new CacheCommandRequest(CacheCommand.build(CacheCommandType.REGISTER, key).value(value)));
    }

    // TODO: HACER METODO PARA OBTENER Y TRABAJAR CON LO OBTENIDO (de momento solo envia instruccion y en segundo plano obtiene valor a local)
    public void getCache(String key, Consumer<Object> consumer) {
        sendObject(new CacheCommandRequest(CacheCommand.build(CacheCommandType.GET, key)));
    }

    public void setCache(String key, Object value) {
        sendObject(new CacheCommandRequest(CacheCommand.build(CacheCommandType.SET, key).value(value)));
    }

    public void removeCache(String key) {
        sendObject(new CacheCommandRequest(CacheCommand.build(CacheCommandType.REMOVE, key)));
    }

    public void handleException(Exception e) {
        try {
            inputStream.close();
            outputStream.close();
            connection.close();
        } catch (IOException ex) {
            handleException(e);
        }
    }

    public void sendCommand(String command) {
        try {
            outputStream.writeCommand(command);
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void sendObject(Object object) {
        try {
            outputStream.writeObject(object);
        } catch (IOException e) {
            handleException(e);
        }
        new AAAa();
    }

    private class AAAa {

    }

}
