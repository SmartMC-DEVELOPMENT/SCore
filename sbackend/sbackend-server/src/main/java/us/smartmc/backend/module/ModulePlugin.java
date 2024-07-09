package us.smartmc.backend.module;

import lombok.Getter;

@Getter
public abstract class ModulePlugin implements IModulePlugin {

    private final IModulePluginInfo info;

    public ModulePlugin() {
        // Intenta obtener la anotación de la clase concreta
        this.info = this.getClass().getDeclaredAnnotation(IModulePluginInfo.class);
        if (this.info == null) {
            throw new IllegalStateException("IModulePluginInfo annotation not found on " + this.getClass().getName());
        }
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
