package us.smartmc.backend.module;

import lombok.Getter;

import java.io.File;
import java.util.List;

@Getter
public class ModulePluginInfo {

    private final File file;
    private final String mainClass;

    private final List<String> dependencies;

    public ModulePluginInfo(File file, String mainClass, List<String> dependencies) {
        this.file = file;
        this.mainClass = mainClass;
        this.dependencies = dependencies;
    }

}
