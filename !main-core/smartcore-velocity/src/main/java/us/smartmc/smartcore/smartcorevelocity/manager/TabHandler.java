package us.smartmc.smartcore.smartcorevelocity.manager;

import com.velocitypowered.api.proxy.Player;
import lombok.Getter;
import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.manager.VelocityPluginsAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import us.smartmc.smartcore.smartcorevelocity.SmartCoreVelocity;
import us.smartmc.smartcore.smartcorevelocity.instance.PlayerLanguages;

import java.io.File;
import java.util.*;

public class TabHandler extends FilePluginConfig {

    private static final SmartCoreVelocity plugin = SmartCoreVelocity.getPlugin();

    @Getter
    private static final HashMap<Language, TabHandler> handlers = new HashMap<>();

    public static void register() {
        for (Language language : Language.values()) {
            new TabHandler(language);
        }
    }

    private final String HEADER_KEY = "header";
    private final String FOOTER_KEY = "footer";

    public TabHandler(Language language) {
        super(new File(plugin.getDataDirectory().toFile(), "tab_" + language.name() + ".json"));
        handlers.put(language, this);
        load();
        registerDefault(HEADER_KEY, "Linea1");
        registerDefault(FOOTER_KEY, "Linea2");
        save();
    }

    public void sendTab(Player player, Component header, Component footer) {
        player.sendPlayerListHeaderAndFooter(header, footer);
    }

    public int getMaxSizeOfList(String path) {
        return getList(path, String.class).size();
    }

    public Component getCurrentHeader() {
        String value = get(HEADER_KEY, String.class);
        MiniMessage miniMessage = MiniMessage.miniMessage();
        return miniMessage.deserialize(LegacyComponentSerializer.legacySection().deserialize(value).content());
    }

    public Component getCurrentFooter() {
        String value = get(FOOTER_KEY, String.class);
        MiniMessage miniMessage = MiniMessage.miniMessage();
        return miniMessage.deserialize(LegacyComponentSerializer.legacySection().deserialize(value).content());
    }

    public static Collection<TabHandler> getTabHandlers() {
        return handlers.values();
    }

}
