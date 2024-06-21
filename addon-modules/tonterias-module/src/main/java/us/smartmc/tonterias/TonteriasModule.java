package us.smartmc.tonterias;

import lombok.Getter;
import us.smartmc.smartaddons.plugin.AddonInfo;
import us.smartmc.smartaddons.plugin.AddonPlugin;
import us.smartmc.smartaddons.spigot.SmartAddonsSpigot;
import us.smartmc.tonterias.listener.WhitelistListener;
import us.smartmc.tonterias.manager.WhitelistManager;

@AddonInfo(name = "tonterias-module", version = "JAJA")
public class TonteriasModule extends AddonPlugin {

    @Getter
    private static TonteriasModule module;

    @Override
    public void start() {
        module = this;
        getDataFolder().mkdirs();

        WhitelistManager.load();
        registerListeners(SmartAddonsSpigot.getProvidingPlugin(SmartAddonsSpigot.class), new WhitelistListener());
    }

    @Override
    public void stop() {

    }
}
