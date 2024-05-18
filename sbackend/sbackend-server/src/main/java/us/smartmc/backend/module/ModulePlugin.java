package us.smartmc.backend.module;

import lombok.Getter;

@Getter
public abstract class ModulePlugin implements IModulePlugin {

    private final ModulePluginInfo info;

    public ModulePlugin() {
        info = getClass().getDeclaredAnnotation(ModulePluginInfo.class);
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
