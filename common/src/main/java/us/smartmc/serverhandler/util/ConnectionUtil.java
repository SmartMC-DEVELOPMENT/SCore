package us.smartmc.serverhandler.util;

import me.imsergioh.jbackend.api.ConnectionHandler;
import us.smartmc.serverhandler.instance.BackendCommandExecuteRequest;

public class ConnectionUtil {

    public static void sendCommand(ConnectionHandler handler, String cmdLine) {
        handler.send(new BackendCommandExecuteRequest(cmdLine));
    }

}
