package us.smartmc.game;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.core.SmartCore;
import us.smartmc.game.manager.ConfigsManager;
import us.smartmc.skyblock.ISkyBlockAPI;
import us.smartmc.skyblock.instance.SkyBlockServerType;

public class SkyBlockPlugin extends JavaPlugin implements ISkyBlockAPI {

    @Getter
    private static SkyBlockPlugin plugin;

    private static SmartCore core;

    @Getter
    private SpigotYmlConfig mainConfig;

    @Override
    public void onEnable() {
        plugin = this;
        core = SmartCore.getPlugin();
        ConfigsManager.load();
    }

    @Override
    public void onDisable() {

    }

    @Override
    public SkyBlockServerType getBlockModeType() {
        return SkyBlockServerType.valueOf(ConfigsManager.getConfig().getString("serverType"));
    }
}
