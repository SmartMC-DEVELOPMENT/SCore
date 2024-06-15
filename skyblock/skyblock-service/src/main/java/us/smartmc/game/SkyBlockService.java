package us.smartmc.game;

import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.instance.service.BackendService;
import us.smartmc.backend.module.ModulePlugin;
import us.smartmc.backend.module.ModulePluginInfo;
import us.smartmc.game.backend.SkyBlockCommand;

@ModulePluginInfo(name = "SmartSkyBlock-Service", version = "1.0-DEV")
public class SkyBlockService extends ModulePlugin {

    @Override
    public void onEnable() {
        super.onEnable();
        ConnectionInputManager.registerCommands(new SkyBlockCommand());
    }
}
