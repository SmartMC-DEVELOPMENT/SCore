package us.smartmc.test;

import org.bukkit.plugin.java.JavaPlugin;

public class TestGameImplementation extends JavaPlugin {

    @Override
    public void onEnable() {
        new GameIntegration(this);
    }
}