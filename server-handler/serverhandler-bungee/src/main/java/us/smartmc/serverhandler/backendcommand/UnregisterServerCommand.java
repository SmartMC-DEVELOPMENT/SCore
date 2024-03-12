package us.smartmc.serverhandler.backendcommand;

import me.imsergioh.jbackend.api.ConnectionHandler;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.smartmc.serverhandler.executor.BackendCommand;
import us.smartmc.serverhandler.request.ServerUnregisterRequest;

public class UnregisterServerCommand extends BackendCommand {


    public UnregisterServerCommand() {
        super("unregisterServer");
    }

    @Override
    public void execute(ConnectionHandler connectionHandler, String label, String[] args) {
        String name = args[0];
        new ServerUnregisterRequest(name);
    }
}
