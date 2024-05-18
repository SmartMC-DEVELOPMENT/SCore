package us.smartmc.survivaladdon.fixescore;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import lombok.Getter;
import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.handler.PubSubConnectionHandler;
import me.imsergioh.pluginsapi.handler.VariablesHandler;
import me.imsergioh.pluginsapi.instance.VariableListener;
import me.imsergioh.pluginsapi.util.PaperChatUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.smartaddons.plugin.AddonInfo;
import us.smartmc.smartaddons.plugin.AddonPlugin;
import us.smartmc.smartaddons.spigot.SmartAddonsSpigot;
import us.smartmc.survivaladdon.fixescore.command.ExecuteAtBungeeCommand;
import us.smartmc.survivaladdon.fixescore.listener.OnlineCountListener;
import us.smartmc.survivaladdon.fixescore.newvariables.DateVariables;
import us.smartmc.survivaladdon.fixescore.newvariables.LuckPermsVariables;
import us.smartmc.survivaladdon.fixescore.newvariables.PlayerMainVariables;

@AddonInfo(name = "fixes-core")
public class FixesCore extends AddonPlugin {

    @Getter
    private static FixesCore plugin;

    @Override
    public void start() {
        plugin = this;

        new Thread(() -> {
            RedisConnection.mainConnection = new RedisConnection("localhost", 6379);
            PubSubConnectionHandler.register(new ServerConnectionsHandler());
        }).start();

        registerCommand(new ExecuteAtBungeeCommand());

        registerListeners(SmartAddonsSpigot.getProvidingPlugin(SmartAddonsSpigot.class), new OnlineCountListener());

        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(SmartAddonsSpigot.getPlugin(), "BungeeCord");

        VariablesHandler.register(new DateVariables());
        VariablesHandler.register(new PlayerMainVariables());
        registerVariablesIfPluginIsEnabled("LuckPerms", LuckPermsVariables.class);

        registerChatProtocol();

        removePluginByName("sleep-most");
    }

    private void removePluginByName(String name) {
        JavaPlugin plugin = (JavaPlugin) Bukkit.getPluginManager().getPlugin(name);
        if (plugin != null && plugin.isEnabled()) {
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
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

    private void registerChatProtocol() {
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

        try {
            for (PacketListener listener : ProtocolLibrary.getProtocolManager().getPacketListeners()) {
                if (listener.getPlugin().equals(SmartAddonsSpigot.getPlugin()) && !listener.equals(packetListener)) {
                    ProtocolLibrary.getProtocolManager().removePacketListener(listener);
                }
            }
        } catch (Exception e) {e.printStackTrace();}
    }

    @Override
    public void stop() {

    }
}
