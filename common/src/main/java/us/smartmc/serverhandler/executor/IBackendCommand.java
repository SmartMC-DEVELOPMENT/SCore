package us.smartmc.serverhandler.executor;

import me.imsergioh.jbackend.api.ConnectionHandler;

public interface IBackendCommand {

    void execute(ConnectionHandler connectionHandler, String label, String[] args);

    String getName();

}
