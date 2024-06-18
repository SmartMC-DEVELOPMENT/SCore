package us.smartmc.bmotd;

import com.google.inject.Inject;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;
import us.smartmc.bmotd.commands.bmotdCMD;
import us.smartmc.bmotd.commands.bwhitelistCMD;
import us.smartmc.bmotd.commands.maintenanceCMD;
import us.smartmc.bmotd.listener.JoinEvent;
import us.smartmc.bmotd.listener.PingEvent;
import us.smartmc.bmotd.manager.MOTDManager;
import us.smartmc.bmotd.manager.MOTDUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Plugin(
        id = "bmotd-velocity",
        name = "BMotd-Velocity",
        version = "1.0-SNAPSHOT"
)
public class BMotdVelocity {

    @Inject
    private Logger logger;

    @Getter
    private static BMotdVelocity plugin;

    @Getter
    private final ProxyServer proxy;

    @Getter
    private MOTDManager motdManager;

    @Getter
    private MOTDUtil motdUtil;

    @Getter
    private final Path dataDirectory;

    @Inject
    public BMotdVelocity(@DataDirectory Path path, ProxyServer proxy) {
        this.dataDirectory = path;
        this.proxy = proxy;
        initDataFolder();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        plugin = this;
        motdManager = new MOTDManager();
        motdUtil = new MOTDUtil();

        registerCommand("bmotd", bmotdCMD.class);
        registerCommand("maintenance", bmotdCMD.class);
        registerCommand("bwhitelist", bwhitelistCMD.class);

        getProxy().getCommandManager().register("bmotd", new bmotdCMD());
        getProxy().getCommandManager().register("maintenance", new maintenanceCMD());
        getProxy().getCommandManager().register("bwhitelist", new bwhitelistCMD());

        regListener(new PingEvent());
        regListener(new JoinEvent());
    }

    private void regListener(Object o) {
        getProxy().getEventManager().register(this, o);
    }

    private void registerCommand(String name, Class<? extends SimpleCommand> command) {
        try {
            getProxy().getCommandManager().register(name, command.newInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void reloadBMotd(){
        motdManager = new MOTDManager();
        motdUtil = new MOTDUtil();
    }

    public File getDataFolder() {
        return dataDirectory.toFile();
    }

    private void initDataFolder() {
        if (!java.nio.file.Files.exists(dataDirectory)) {
            try {
                java.nio.file.Files.createDirectories(dataDirectory);
            } catch (IOException e) {
                // Manejar la excepción como creas conveniente. Por ejemplo, podrías loguear un error.
                e.printStackTrace();
            }
        }
    }
}
