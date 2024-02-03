package us.smartmc.serverhandler.backendcommand;

import me.imsergioh.jbackend.api.ConnectionHandler;
import us.smartmc.serverhandler.executor.BackendCommand;
import us.smartmc.serverhandler.instance.CommonServerInfo;
import us.smartmc.serverhandler.instance.ServerStatus;
import us.smartmc.serverhandler.manager.ServerManager;

public class ServerStatusCommand extends BackendCommand {

    public ServerStatusCommand() {
        super("serverStatus");
    }

    // EXAMPLE: serverStatus <active, idle...> <name>

    @Override
    public void execute(ConnectionHandler handler, String label, String[] args) {
        if(executeStatus(args, "active", ServerStatus.ACTIVE)) {
            System.out.println("Server " + args[1] + " online! LES GO");
            return;
        }

        if (executeStatus(args, "idle", ServerStatus.IDLE)) {
            String name = args[1];
            ServerManager.deleteIfNotTemporalAndUnregister(name);
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
