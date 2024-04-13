package us.smartmc.core;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import me.imsergioh.pluginsapi.handler.VariablesHandler;
import me.imsergioh.pluginsapi.instance.VariableListener;
import me.imsergioh.pluginsapi.util.PaperChatUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import us.smartmc.core.newvariables.DateVariables;
import us.smartmc.core.newvariables.LuckPermsVariables;
import us.smartmc.core.newvariables.PlayerMainVariables;
import us.smartmc.smartaddons.plugin.AddonInfo;
import us.smartmc.smartaddons.plugin.AddonPlugin;
import us.smartmc.smartaddons.spigot.SmartAddonsSpigot;

@AddonInfo(name = "chat-module", version = "DEV")
public class ChatModule extends AddonPlugin {

    @Override
    public void start() {
        System.out.println("Started addon " + getInfo().name() + " v" + getInfo().version());
        checkAndRemoveLPC();
        checkAndRemoveAnnouncements();

        ProtocolLibrary.getProtocolManager().getPacketListeners().forEach(listener -> {
            if (!listener.getPlugin().equals(SmartAddonsSpigot.getPlugin())) return;
            ProtocolLibrary.getProtocolManager().removePacketListener(listener);
        });

        PacketListener packetListener = new PacketAdapter(SmartAddonsSpigot.getPlugin(), ListenerPriority.HIGHEST, PacketType.Play.Client.CHAT) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                event.setCancelled(true);
                String msgString = event.getPacket().getStrings().read(0);

                Component message;

                if (event.getPlayer().hasPermission("smartmc.vip")) {
                    message = PaperChatUtil.parse("<white>" + msgString);
                } else {
                    message = Component.join(JoinConfiguration.builder().build(), PaperChatUtil.parse("<gray>"), Component.text(msgString));
                }

                Component formattedMessage = PaperChatUtil.parse(event.getPlayer(), "<chat.prefix><reset><gray><name> &8&l»&7 ");

                Bukkit.broadcast(Component.join(JoinConfiguration.builder().build(), formattedMessage, message));
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

    private void checkAndRemoveAnnouncements() {
        Plugin lpcPlugin = Bukkit.getPluginManager().getPlugin("Infiniteannouncements");
        if (lpcPlugin == null) return;
        Bukkit.getPluginManager().disablePlugin(lpcPlugin);
    }

    @Override
    public void stop() {
        System.out.println("Stopped addon " + getInfo().name() + " v" + getInfo().version());
        ProtocolLibrary.getProtocolManager().removePacketListeners(SmartAddonsSpigot.getPlugin());
    }
}
