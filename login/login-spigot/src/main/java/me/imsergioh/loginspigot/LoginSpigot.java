package me.imsergioh.loginspigot;

import com.github.games647.fastlogin.bukkit.FastLoginBukkit;
import com.github.games647.fastlogin.core.shared.FastLoginCore;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import me.imsergioh.loginspigot.command.LoginCMD;
import me.imsergioh.loginspigot.command.RegisterCMD;
import me.imsergioh.loginspigot.listener.AuthPlayersListeners;
import me.imsergioh.loginspigot.listener.LoginPlayersFactoryListeners;
import me.imsergioh.loginspigot.scheduler.AuthAnnouncer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class LoginSpigot extends JavaPlugin implements Listener, CommandExecutor {

    private static LoginSpigot plugin;

    boolean enabled = true;

    private static FastLoginCore<Player, CommandSender, FastLoginBukkit> fastLoginCore;
    private MongoClient mongoClient;

    @Override
    public void onEnable() {
        plugin = this;
        getDataFolder().mkdirs();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "smartlogin");

        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("toggle").setExecutor(this);

        getCommand("login").setExecutor(new LoginCMD());
        getCommand("register").setExecutor(new RegisterCMD());

        LoginSpigotAuthPlugin authPlugin = new LoginSpigotAuthPlugin();
        fastLoginCore = FastLoginBukkit.getPlugin(FastLoginBukkit.class).getCore();
        fastLoginCore.setAuthPluginHook(authPlugin);

        registerDefaultConfig();

        Bukkit.getPluginManager().registerEvents(new AuthPlayersListeners(), this);
        Bukkit.getPluginManager().registerEvents(new LoginPlayersFactoryListeners(), this);

        mongoClient = new MongoClient(new MongoClientURI(getConfig().getString("mongo")));

        AuthAnnouncer.start();
    }

    private void registerDefaultConfig() {
        File file = new File(getDataFolder(), "config.yml");
        if (file.exists()) return;
        try {
            getConfig().options().copyDefaults(true);
            saveConfig();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("*")) return true;
        enabled = !enabled;
        System.out.println("Set to " + enabled);
        return true;
    }

    @EventHandler
    public void message(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        event.getPlayer().teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
    }

    @EventHandler
    public void message(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onChat(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage();
        if (command.startsWith("/")) command = command.replaceFirst("/", "");
        if (command.startsWith("login") || command.startsWith("register")) return;
        if(!enabled) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void interact(EntityDamageEvent event) {
        if(!enabled) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void interact(PlayerInteractEvent event) {
        if(!enabled) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if(!enabled) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void block(BlockPlaceEvent event) {
        if(!enabled) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void block(BlockBreakEvent event) {
        if(!enabled) return;
        event.setCancelled(true);
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public static FastLoginCore<Player, CommandSender, FastLoginBukkit> getFastLoginCore() {
        return fastLoginCore;
    }

    public static LoginSpigot getPlugin() {
        return plugin;
    }
}
