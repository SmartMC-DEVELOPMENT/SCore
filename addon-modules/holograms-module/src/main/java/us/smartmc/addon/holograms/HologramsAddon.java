package us.smartmc.addon.holograms;

import lombok.Getter;
import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import us.smartmc.addon.holograms.adapter.HologramAdapter1_8;
import us.smartmc.addon.holograms.commands.HologramsCommand;
import us.smartmc.addon.holograms.instance.config.MainConfig;
import us.smartmc.addon.holograms.instance.hologram.HologramHolder;
import us.smartmc.addon.holograms.listener.EssentialListeners;
import us.smartmc.addon.holograms.manager.HologramUpdaterManager;
import us.smartmc.addon.holograms.util.IHologramAdapter;
import us.smartmc.smartaddons.plugin.AddonInfo;
import us.smartmc.smartaddons.plugin.AddonPlugin;

import java.io.File;
import java.util.Objects;

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

    }
}
