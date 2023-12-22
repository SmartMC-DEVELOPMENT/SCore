package us.smartmc.core.pluginsapi.language;

import us.smartmc.core.pluginsapi.instance.FilePluginConfig;

public class LanguageMessagesHolder extends FilePluginConfig {

    private final String name;

    public LanguageMessagesHolder(String name, LanguageHolder holder) {
        super(holder.getDir() + "/" + name + ".json");
        this.name = name;
        load();
    }

    public String getName() {
        return name;
    }
}
