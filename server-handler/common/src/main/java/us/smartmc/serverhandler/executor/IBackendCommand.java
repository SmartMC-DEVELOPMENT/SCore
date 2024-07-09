package us.smartmc.serverhandler.executor;

import us.smartmc.backend.connection.ConnectionHandler;

public interface IBackendCommand {

    void execute(ConnectionHandler connectionHandler, String label, String[] args);

    String getName();

}
