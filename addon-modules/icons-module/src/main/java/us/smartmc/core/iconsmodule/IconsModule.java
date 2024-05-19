package us.smartmc.core.iconsmodule;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.smartaddons.plugin.AddonInfo;
import us.smartmc.smartaddons.plugin.AddonPlugin;

@AddonInfo(name = "icons-module", version = "1.0")
public final class IconsModule extends AddonPlugin {

    @Getter
    private static IconsModule module;

    @Override
    public void start() {
        module = this;
    }

    @Override
    public void stop() {

    }
}
