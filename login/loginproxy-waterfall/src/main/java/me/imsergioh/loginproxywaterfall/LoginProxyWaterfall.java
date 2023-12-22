package me.imsergioh.loginproxywaterfall;

import me.imsergioh.loginproxywaterfall.command.LoginCMD;
import me.imsergioh.loginproxywaterfall.command.RegisterCMD;
import me.imsergioh.loginproxywaterfall.listener.AuthPlayersListeners;
import me.imsergioh.loginproxywaterfall.listener.LoginPlayersFactoryListeners;
import me.imsergioh.loginproxywaterfall.manager.PluginCommandsHandler;
import me.imsergioh.loginproxywaterfall.scheduler.AuthAnnouncer;
import me.imsergioh.smartcorewaterfall.instance.PluginYMLConfig;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class LoginProxyWaterfall extends Plugin {

    private static LoginProxyWaterfall plugin;

    private PluginYMLConfig config;

    @Override
    public void onEnable() {
        plugin = this;
        config = new PluginYMLConfig(new File(getDataFolder(), "config.yml"));
        registerConfigDefault("authServers", Arrays.asList("auth", "auth2"));
        registerConfigDefault("lobbyServers", Arrays.asList("lobby", "lobby2"));
        config.save();

        getProxy().getPluginManager().registerListener(plugin, new LoginPlayersFactoryListeners());
        getProxy().getPluginManager().registerListener(plugin, new AuthPlayersListeners());
        getProxy().getPluginManager().registerListener(plugin, new PluginCommandsHandler());

        PluginCommandsHandler.register("login", new LoginCMD());
        PluginCommandsHandler.register("register", new RegisterCMD());
        AuthAnnouncer.start();
    }

    @Override
    public void onDisable() {
        AuthAnnouncer.stop();
    }

    private void registerConfigDefault(String path, Object value) {
        if (config.getConfig().contains(path)) return;
        config.getConfig().set(path, value);
    }

    public List<String> getServers(String prefix) {
        List<String> list = new ArrayList<>();
        List<String> configList = config.getConfig().getStringList(prefix + "Servers");

        for (String configName : configList) {
            if (!configName.endsWith("*")) {
                list.add(configName);
                continue;
            }

            String formattedPrefixServerName = configName.substring(0, configName.length() - 1);
            for (String serverName : LoginProxyWaterfall.getPlugin().getProxy().getServersCopy().keySet()) {
                if (!serverName.startsWith(formattedPrefixServerName)) continue;
                list.add(serverName);
            }
        }
        return list;
    }

    public static LoginProxyWaterfall getPlugin() {
        return plugin;
    }
}
