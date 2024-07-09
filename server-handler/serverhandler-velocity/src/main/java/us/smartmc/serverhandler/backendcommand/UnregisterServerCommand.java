package us.smartmc.serverhandler.backendcommand;
;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;
import us.smartmc.serverhandler.request.ServerUnregisterRequest;

public class UnregisterServerCommand extends BackendCommandExecutor {


    public UnregisterServerCommand() {
        super("unregisterServer");
    }

    @Override
    public void onCommand(ConnectionHandler connectionHandler, String label, String[] args) {
        String name = args[0];
        new ServerUnregisterRequest(name);
    }
}
