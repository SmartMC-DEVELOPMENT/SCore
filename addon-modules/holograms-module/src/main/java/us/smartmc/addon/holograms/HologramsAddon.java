package us.smartmc.addon.holograms;

import lombok.Getter;
import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import me.imsergioh.pluginsapi.handler.VariablesHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import us.smartmc.addon.holograms.adapter.HologramAdapter1_8;
import us.smartmc.addon.holograms.commands.HologramsCommand;
import us.smartmc.addon.holograms.instance.config.MainConfig;
import us.smartmc.addon.holograms.instance.hologram.Hologram;
import us.smartmc.addon.holograms.instance.hologram.HologramHolder;
import us.smartmc.addon.holograms.instance.hologram.IHologram;
import us.smartmc.addon.holograms.listener.EssentialListeners;
import us.smartmc.addon.holograms.manager.HologramUpdaterManager;
import us.smartmc.addon.holograms.util.IHologramAdapter;
import us.smartmc.addon.holograms.variable.TickVariable;
import us.smartmc.core.SmartCore;
import us.smartmc.smartaddons.plugin.AddonInfo;
import us.smartmc.smartaddons.plugin.AddonPlugin;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

@AddonInfo(name = "holograms-module", version = "1.0-DEV")
public class HologramsAddon extends AddonPlugin {

    @Getter
    private static HologramsAddon plugin;

    private static File holdersDirectory;

    @Getter
    private MainConfig config;

    @Getter
    private IHologramAdapter hologramAdapter;

    @Override
    public void start() {
        plugin = this;
        getDataFolder().mkdirs();
        hologramAdapter = new HologramAdapter1_8();
        config = new MainConfig();

        holdersDirectory = new File(plugin.getDataFolder() + "/holders");

        holdersDirectory.mkdirs();

        loadHolders();

        registerListeners(SpigotPluginsAPI.getPlugin(), new EssentialListeners());

        HologramUpdaterManager.startRunnable();

        registerCommand(new HologramsCommand());

        VariablesHandler.register(new TickVariable());

        Hologram hologram = new Hologram("test", new Location(Bukkit.getWorlds().get(0), 0, 73, 0));
        HologramHolder.getOrCreate("main")
                .registerHologram(hologram);
        hologram.addLine("FUUAAA");
        hologram.addLine("Linea 2");
        hologram.addLine("Linea 3");
    }

    private void loadHolders() {
        for (File file : Objects.requireNonNull(holdersDirectory.listFiles())) {
            if (!file.getName().endsWith(".yml")) continue;
            String name = file.getName().replace(".yml", "");
            HologramHolder.getOrCreate(name);
        }
    }

    @Override
    public void stop() {
        HologramHolder.forEachHolder(holder -> {
            holder.forEachHologram(IHologram::removeAllStands);
        });

    }
}
