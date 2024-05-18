package us.smartmc.backend.module;

public abstract class ModulePlugin implements IModulePlugin {

    private ModulePluginInfo info;

    @Override
    public void onEnable() {
        System.out.println("[MODULE - " + info.name() + "] Has been enabled!");
    }

    @Override
    public void onDisable() {
        System.out.println("[MODULE - " + info.name() + "] Has been disabled!");
    }

    public ModulePluginInfo getInfo() {
        if (info != null) return info;
        info = ModuleClassLoader.readPluginInfo(getClass());
        return info;
    }
}
