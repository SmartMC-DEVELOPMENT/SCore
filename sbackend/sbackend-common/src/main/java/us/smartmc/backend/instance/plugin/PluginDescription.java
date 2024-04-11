package us.smartmc.backend.instance.plugin;

import lombok.Getter;

@Getter
public class PluginDescription {

    private final String name, mainClass;

    public PluginDescription(String name, String mainClass) {
        this.name = name;
        this.mainClass = mainClass;
    }

}
