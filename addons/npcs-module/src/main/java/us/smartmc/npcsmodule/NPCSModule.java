package us.smartmc.npcsmodule;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import me.imsergioh.pluginsapi.handler.VariablesHandler;
import us.smartmc.npcsmodule.instance.NPCSConfig;
import us.smartmc.npcsmodule.listener.NPCListeners;
import us.smartmc.npcsmodule.listener.PlayerClickListener;
import us.smartmc.npcsmodule.manager.NPCCommandManager;
import us.smartmc.npcsmodule.manager.NPCManager;
import us.smartmc.npcsmodule.messages.PluginMessages;
import us.smartmc.npcsmodule.npccommand.ServerCommand;
import us.smartmc.npcsmodule.variables.NPCEmbedsVariables;
import us.smartmc.smartaddons.plugin.AddonInfo;
import us.smartmc.smartaddons.plugin.AddonPlugin;

@AddonInfo(name = "npcs-module", version = "DEV-TESTING")
public class NPCSModule extends AddonPlugin {

    private static NPCSModule addon;

    public static NPCSConfig config;

    private PacketAdapter playerClickListener;

    @Override
    public void start() {
        addon = this;
        new NPCManager();
        config = new NPCSConfig();
        new PluginMessages();
        registerListeners(new NPCListeners());
        playerClickListener = new PlayerClickListener();
        NPCCommandManager.register("server", new ServerCommand());

        VariablesHandler.register(new NPCEmbedsVariables());
    }

    @Override
    public void stop() {
        ProtocolLibrary.getProtocolManager().removePacketListener(playerClickListener);
    }

    public static NPCSModule getAddon() {
        return addon;
    }
}
