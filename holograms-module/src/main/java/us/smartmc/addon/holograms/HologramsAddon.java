package us.smartmc.addon.holograms;

import lombok.Getter;
import us.smartmc.smartaddons.plugin.AddonInfo;
import us.smartmc.smartaddons.plugin.AddonPlugin;

@AddonInfo(name = "holograms-module", version = "1.0-DEV")
public class HologramsAddon extends AddonPlugin {

    @Getter
    private static HologramsAddon plugin;

    @Override
    public void start() {
        plugin = this;
    }

    @Override
    public void stop() {

    }
}
