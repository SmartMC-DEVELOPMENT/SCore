package us.smartmc.backend.handler;

import lombok.Getter;
import us.smartmc.backend.BackendServerMain;
import us.smartmc.backend.module.ModuleClassLoader;

import java.io.File;
import java.util.Objects;

@Getter
public class ModulesHandler {

    @Getter
    public static final ModulesHandler mainModulesHandler = new ModulesHandler();
    @Getter
    private static File modulesDir;

    public ModulesHandler() {
        modulesDir = new File(BackendServerMain.getParentDirectory() + "/modules");
        if (!modulesDir.exists())
            modulesDir.mkdirs();
    }

    public void loadModulesJars() {
        for (File file : Objects.requireNonNull(modulesDir.listFiles())) {
            if (!file.getName().endsWith(".jar")) continue;
            ModuleClassLoader.loadPluginJar(file);
        }
    }

}
