package main.java.us.smartmc.serverhandler.util;

import main.java.us.smartmc.serverhandler.instance.BackendCommandExecuteRequest;
import me.imsergioh.jbackend.api.ConnectionHandler;

public class ConnectionUtil {

    public static void sendCommand(ConnectionHandler handler, String cmdLine) {
        handler.send(new BackendCommandExecuteRequest(cmdLine));
    }

}
