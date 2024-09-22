package us.smartmc.test;

import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import org.bukkit.plugin.java.JavaPlugin;

public class TestGameImplementation extends JavaPlugin {
    
    @Override
    public void onEnable() {
        SpigotPluginsAPI.setup(this);
    }
}