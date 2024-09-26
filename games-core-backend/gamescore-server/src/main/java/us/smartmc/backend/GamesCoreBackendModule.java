package us.smartmc.backend;

import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.listener.GetCuboidListener;
import us.smartmc.backend.listener.SaveCuboidListener;
import us.smartmc.backend.module.IModulePluginInfo;
import us.smartmc.backend.module.ModulePlugin;

@IModulePluginInfo(name = "games-core")
public class GamesCoreBackendModule extends ModulePlugin {

    private static GamesCoreBackendModule module;

    @Override
    public void onEnable() {
        module = this;
        ConnectionInputManager.registerListeners(new SaveCuboidListener(), new GetCuboidListener());
    }

    public static GamesCoreBackendModule getModule() {
        return module;
    }
}
