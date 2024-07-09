package us.smartmc.serverhandler.backendcommand;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.handler.CacheManager;
import us.smartmc.backend.instance.BackendCommandExecutor;
import us.smartmc.serverhandler.instance.CommonServerInfo;
import us.smartmc.serverhandler.instance.ServerInfo;
import us.smartmc.serverhandler.instance.ServerStatus;
import us.smartmc.serverhandler.manager.ServerManager;

public class ServerStatusCommand extends BackendCommandExecutor {

    public ServerStatusCommand() {
        super("serverStatus");
    }

    // EXAMPLE: serverStatus <active, idle...> <name>
    @Override
    public void onCommand(ConnectionHandler connection, String label, String[] args) {
        if(executeStatus(args, "active", ServerStatus.ACTIVE)) {
            ServerInfo serverInfo = ServerManager.get(args[1]);
            System.out.println("New server connected -> " + serverInfo.getName() + "!");
            ServerManager.registerAlive(serverInfo.getName());
            return;
        }

        if (executeStatus(args, "idle", ServerStatus.IDLE)) {
            String name = args[1];
            ServerManager.deleteIfNotTemporalAndUnregister(name);
            CacheManager.getMainCacheManager().remove("online." + name);
        }
    }

    private boolean executeStatus(String[] args, String argCmd, ServerStatus status) {
        if (args[0].equals(argCmd)) {
            String name = args[1];
            CommonServerInfo serverInfo = ServerManager.get(name);
            serverInfo.setStatus(status);
            return true;
        }
        return false;
    }
}
