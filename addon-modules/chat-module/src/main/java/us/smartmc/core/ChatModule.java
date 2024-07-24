package us.smartmc.core;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import lombok.Getter;
import me.imsergioh.pluginsapi.handler.VariablesHandler;
import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;
import me.imsergioh.pluginsapi.instance.VariableListener;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import us.smartmc.core.command.ReloadChatCommand;
import us.smartmc.core.newvariables.DateVariables;
import us.smartmc.core.newvariables.LuckPermsVariables;
import us.smartmc.core.newvariables.PlayerMainVariables;
import us.smartmc.smartaddons.plugin.AddonInfo;
import us.smartmc.smartaddons.plugin.AddonPlugin;
import us.smartmc.smartaddons.spigot.SmartAddonsSpigot;

import java.io.File;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

@AddonInfo(name = "chat-module", version = "DEV")
public class ChatModule extends AddonPlugin {

    @Getter
    private static ChatModule module;

    @Getter
    private static SpigotYmlConfig config;

    @Override
    public void start() {
        module = this;
        getDataFolder().mkdirs();
        System.out.println("Started addon " + getInfo().name() + " v" + getInfo().version());
        checkAndRemoveLPC();
        checkAndRemoveAnnouncements();

        ProtocolLibrary.getProtocolManager().getPacketListeners().forEach(listener -> {
            if (!listener.getPlugin().equals(SmartAddonsSpigot.getPlugin())) return;
            ProtocolLibrary.getProtocolManager().removePacketListener(listener);
        });

        Bukkit.getScheduler().runTaskLater(SmartAddonsSpigot.getPlugin(), () -> {
            PacketListener packetListener = new PacketAdapter(SmartAddonsSpigot.getPlugin(), ListenerPriority.HIGHEST, PacketType.Play.Client.CHAT) {
                @Override
                public void onPacketReceiving(PacketEvent event) {
                    event.setCancelled(true);
                    String msgString = event.getPacket().getStrings().read(0);

                    Component message;

                    if (event.getPlayer().hasPermission("smartmc.vip")) {
                        // Vip >>
                        message = Component.join(JoinConfiguration.builder().build(),
                                ChatUtil.parse(config.getString("vipPrefix")), ChatUtil.parse(msgString));
                    } else {
                        // Not vip >>
                        message = Component.join(JoinConfiguration.builder().build(),
                                ChatUtil.parse(config.getString("defaultPrefix")), Component.text(msgString));
                    }

                    Component formattedMessage = ChatUtil.parse(event.getPlayer(), config.getString("format"), event.getPlayer().getName());

                    AsyncPlayerChatEvent chatEvent = new AsyncPlayerChatEvent(true, event.getPlayer(), msgString, new HashSet<>(Bukkit.getOnlinePlayers()));
                    Bukkit.getPluginManager().callEvent(chatEvent);
                    if (!chatEvent.isCancelled()) {
                        Bukkit.broadcast(Component.join(JoinConfiguration.builder().build(), formattedMessage, message));
                    }
                }
            };
            ProtocolLibrary.getProtocolManager().addPacketListener(packetListener);
        }, 10);

        VariablesHandler.register(new DateVariables());
        VariablesHandler.register(new PlayerMainVariables());
        registerDynamicVariables();

        loadConfig();

        registerCommand(new ReloadChatCommand("reloadchat", true, "*"));
    }

    private void loadConfig() {
        config = new SpigotYmlConfig(new File(getDataFolder(), "/config.yml"));
    }

    private void registerDynamicVariables() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                registerVariablesIfPluginIsEnabled("LuckPerms", LuckPermsVariables.class);
            }
        }, 250);
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
