package us.smartmc.survival;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import me.imsergioh.pluginsapi.handler.VariablesHandler;
import me.imsergioh.pluginsapi.instance.VariableListener;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import us.smartmc.smartaddons.spigot.SmartAddonsSpigot;
import us.smartmc.survival.handler.ChatModeHandler;
import us.smartmc.smartaddons.plugin.AddonInfo;
import us.smartmc.smartaddons.plugin.AddonPlugin;
import us.smartmc.survival.variable.DateVariables;
import us.smartmc.survival.variable.LuckPermsVariables;
import us.smartmc.survival.variable.PlayerMainVariables;

@AddonInfo(name = "chat-module", version = "DEV")
public class ChatModule extends AddonPlugin {

    @Override
    public void start() {
        System.out.println("Started addon " + getInfo().name() + " v" + getInfo().version());
        //new ChatModeHandler(this);
        checkAndRemoveLPC();

        PacketListener packetListener = new PacketAdapter(SmartAddonsSpigot.getPlugin(), ListenerPriority.HIGHEST, PacketType.Play.Client.CHAT) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                event.setCancelled(true);
                String message = event.getPacket().getStrings().read(0);
                Bukkit.broadcastMessage(ChatUtil.color(event.getPlayer().getName() + "&f:&7 " + message));
            }
        };
        ProtocolLibrary.getProtocolManager().addPacketListener(packetListener);

        VariablesHandler.register(new DateVariables());
        VariablesHandler.register(new PlayerMainVariables());
        registerVariablesIfPluginIsEnabled("LuckPerms", LuckPermsVariables.class);
    }

    private void registerVariablesIfPluginIsEnabled(String name, Class<? extends VariableListener<?>> variablesClass) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
        if (plugin == null) return;
        try {
            VariablesHandler.register(variablesClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkAndRemoveLPC() {
        Plugin lpcPlugin = Bukkit.getPluginManager().getPlugin("LPC");
        if (lpcPlugin == null) return;
        Bukkit.getPluginManager().disablePlugin(lpcPlugin);
    }

    @Override
    public void stop() {
        System.out.println("Stopped addon " + getInfo().name() + " v" + getInfo().version());
        ProtocolLibrary.getProtocolManager().removePacketListeners(SmartAddonsSpigot.getPlugin());
    }
}
