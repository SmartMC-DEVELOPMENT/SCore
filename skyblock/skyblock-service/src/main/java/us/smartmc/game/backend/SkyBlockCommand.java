package us.smartmc.game.backend;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;
import us.smartmc.game.instance.server.ServerInfo;
import us.smartmc.game.manager.ServersInfoManager;
import us.smartmc.skyblock.instance.SkyBlockServerType;

public class SkyBlockCommand extends BackendCommandExecutor {

    public SkyBlockCommand() {
        super("skyblock");
    }

    @Override
    public void onCommand(ConnectionHandler connection, String label, String[] args) {
        if (args.length == 0) return;

        if (args[0].equals("registerserver")) {
            if (args.length < 4) {
                System.out.println("SkyBlock invalid command registerserver -> " + label);
                return;
            }
            String id = args[1];
            SkyBlockServerType serverType = SkyBlockServerType.valueOf(args[2]);
            int online = Integer.parseInt(args[3]);
            ServerInfo serverInfo = new ServerInfo(id, serverType);
            serverInfo.setOnlineCount(online);
            ServersInfoManager.register(serverInfo);
            System.out.println("SkyBlock registeredserver -> " + id + " type of " + serverType + " with " + online);
        }

        if (args[0].equals("unregisterserver")) {
            if (args.length < 2) {
                System.out.println("SkyBlock invalid command unregisterserver -> " + label);
                return;
            }
            String id = args[1];
            ServerInfo info = ServersInfoManager.unregister(id, ServerInfo.class);
            if (info == null) return;
            System.out.println("SkyBlock unregisterserver -> " + info.getId() + " type of " + info.getModeType().name() + " with " + info.getOnlineCount());
        }

        if (args[0].equals("setonline")) {
            if (args.length < 3) {
                System.out.println("SkyBlock invalid command setonline -> " + label);
                return;
            }
            String id = args[1];
            int count = Integer.parseInt(args[2]);
            ServerInfo info = ServersInfoManager.getServerInfo(id, ServerInfo.class);
            if (info == null) {
                System.out.println("SkyBlock invalid serverId -> " + id);
                return;
            }
            info.setOnlineCount(count);
            System.out.println("SkyBlock set online -> " + id + " to " + count);
        }
    }
}
