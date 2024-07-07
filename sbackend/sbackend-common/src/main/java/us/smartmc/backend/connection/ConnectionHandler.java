package us.smartmc.backend.connection;

import lombok.Getter;
import us.smartmc.backend.command.ServiceCommand;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.instance.cache.CacheCommand;
import us.smartmc.backend.instance.cache.CacheCommandType;
import us.smartmc.backend.instance.filetransfer.FileTransferType;
import us.smartmc.backend.instance.messaging.MessageCommand;
import us.smartmc.backend.listener.*;
import us.smartmc.backend.protocol.CacheCommandRequest;
import us.smartmc.backend.protocol.CommandRequest;
import us.smartmc.backend.protocol.FileTransferChunk;
import us.smartmc.backend.protocol.FileTransferRegistrar;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

@Getter
public class ConnectionHandler implements Runnable {

    private static boolean defaultListenersAlreadyRegistered;

    @Getter
    protected Socket connection;

    protected final ConnectionOutputStream outputStream;
    protected final ConnectionInputStream inputStream;

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
                        new FileTransferChunkListener());
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

    public void sendFile(File file, FileTransferType type, String destionationPath) {
        FileTransferRegistrar registrar = new FileTransferRegistrar(file, type, destionationPath);
        try {
            outputStream.writeObject(registrar);
        } catch (IOException e) {
            System.out.println("Error while trying to sendFile method at writing registry!");
            throw new RuntimeException(e);
        }

        ObjectOutputStream oos = outputStream.getOut();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file.getAbsolutePath());
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                FileTransferChunk chunk = new FileTransferChunk(registrar.getTransferId(), buffer, bytesRead);
                oos.writeObject(chunk);
                System.out.println("CHUNK -> " + chunk);
            }
            // Indicate finish of transferId
            oos.writeObject(new FileTransferChunk(registrar.getTransferId(), new byte[]{}, 0));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
