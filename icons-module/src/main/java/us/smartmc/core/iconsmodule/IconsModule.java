package us.smartmc.core.iconsmodule;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.smartaddons.plugin.AddonInfo;

@AddonInfo(name = "icons-module", version = "1.0")
public final class IconsModule extends JavaPlugin {

    @Getter
    private static IconsModule module;

    @Override
    public void onEnable() {
        module = this;
    }

    @Override
    public void onDisable() {

    }
}
