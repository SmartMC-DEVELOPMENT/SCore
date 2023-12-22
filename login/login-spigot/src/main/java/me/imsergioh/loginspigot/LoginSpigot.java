package me.imsergioh.loginspigot;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class LoginSpigot extends JavaPlugin implements Listener, CommandExecutor {

    boolean enabled = true;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("toggle").setExecutor(this);
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
    }

    @EventHandler
    public void message(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onChat(PlayerCommandPreprocessEvent event) {
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
}
