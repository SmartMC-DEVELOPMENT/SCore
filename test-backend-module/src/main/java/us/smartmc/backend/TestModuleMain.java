package us.smartmc.backend;

import us.smartmc.backend.module.IModulePluginInfo;
import us.smartmc.backend.module.ModulePlugin;

@IModulePluginInfo(name = "test-module", version = "DEV")
public class TestModuleMain extends ModulePlugin {

    @Override
    public void onEnable() {
        System.out.println("IS VALID TEST-MODULE-MAIN!");
    }

    @Override
    public void onDisable() {

    }
}
