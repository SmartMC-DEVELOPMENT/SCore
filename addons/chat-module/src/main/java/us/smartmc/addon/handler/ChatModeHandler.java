package us.smartmc.addon.handler;

import us.smartmc.addon.listener.ChatModeListener;
import us.smartmc.core.pluginsapi.instance.FilePluginConfig;
import us.smartmc.smartaddons.plugin.AddonPlugin;

import java.io.File;

public class ChatModeHandler {

    private final FilePluginConfig config;

    public ChatModeHandler(AddonPlugin plugin) {
        this.config = new FilePluginConfig(new File(plugin.getDataFolder(), "chatMode.json")).load();
        config.registerDefault("format", "<chat.prefix><name> &8&l»&r %s");
        config.save();
        plugin.registerListeners(new ChatModeListener(this));
    }

    public String getFormat() {
        return config.getString("format");
    }
}
