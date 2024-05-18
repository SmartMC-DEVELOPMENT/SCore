package us.smartmc.backend.command;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.handler.ServicesManager;
import us.smartmc.backend.instance.BackendCommandExecutor;
import us.smartmc.backend.instance.service.IBackendService;

public class ServiceCommand extends BackendCommandExecutor {

    public ServiceCommand() {
        super("service");
    }

    @Override
    public void onCommand(ConnectionHandler connection, String label, String[] args) {
        if (args.length == 0) return;
        String id = args[1];
        switch (args[0].toLowerCase()) {
            case "load": load(id);
            case "unload": unload(id);
        }
    }

    private void load(String className) {
        try {
            ServicesManager.load((Class<? extends IBackendService>) Class.forName(className));
        } catch (ClassNotFoundException e) {
            System.out.println("Not found service with class of " + className);
        }
    }

    private void unload(String className) {
        try {
            ServicesManager.unload((Class<? extends IBackendService>) Class.forName(className));
        } catch (ClassNotFoundException e) {
            System.out.println("Not found service with class of " + className);
        }
    }

}
