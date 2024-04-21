package us.smartmc.backend.handler;

import us.smartmc.backend.command.UpdateServerInfoCmd;
import us.smartmc.backend.instance.handler.MapHandler;
import us.smartmc.backend.instance.server.ServerInfo;

public class ServersInfoHandler extends MapHandler<String, ServerInfo> {

    public ServersInfoHandler() {
        ConnectionInputManager.registerCommands(new UpdateServerInfoCmd());
    }

    @Override
    public ServerInfo getDefaultValue(String name, Object... args) {
        return new ServerInfo(name, (int) args[1], (int) args[2]);
    }
}
