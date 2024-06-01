package us.smartmc.serverhandler.consolecommand;

import us.smartmc.serverhandler.OrchestratorMain;
import us.smartmc.serverhandler.executor.ConsoleCommand;
import us.smartmc.serverhandler.executor.ConsoleCommandInfo;
import us.smartmc.serverhandler.manager.ServerManager;
import us.smartmc.serverhandler.registration.ConfigRegistration;
import us.smartmc.serverhandler.util.CommandUtilities;

@ConsoleCommandInfo(
        name = "reload",
        description = "Reload all registrations",
        usage = "/reload"
)
public class ReloadCommand extends ConsoleCommand {

    @Override
    public void execute(String label, String[] args) {
        new ConfigRegistration().register();
    }
}
