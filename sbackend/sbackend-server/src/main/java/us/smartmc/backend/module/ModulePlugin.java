package us.smartmc.backend.module;

import lombok.Getter;

public abstract class ModulePlugin implements IModulePlugin {

    @Getter
    private final ModulePluginInfo info;

    public ModulePlugin() {
        info = ModuleClassLoader.readPluginInfo(getClass());
    }

    @Override
    public void onEnable() {
        System.out.println("[MODULE - " + info.name() + "] Has been enabled!");
    }

    @Override
    public void onDisable() {
        System.out.println("[MODULE - " + info.name() + "] Has been disabled!");
    }
}
