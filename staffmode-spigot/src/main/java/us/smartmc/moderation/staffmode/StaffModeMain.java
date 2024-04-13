package us.smartmc.moderation.staffmode;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class StaffModeMain extends JavaPlugin {

    @Getter
    private static StaffModeMain plugin;

    @Override
    public void onEnable() {
        plugin = this;
    }

    @Override
    public void onDisable() {

    }
}
