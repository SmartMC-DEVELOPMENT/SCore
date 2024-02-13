package us.smartmc.moderation.staffmode;

import org.bukkit.plugin.java.JavaPlugin;

public final class StaffModeMain extends JavaPlugin {

    private static StaffModeMain plugin;

    @Override
    public void onEnable() {
        plugin = this;
    }

    @Override
    public void onDisable() {

    }
}
