package us.smartmc.smartcore.proxy.manager;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.smartmc.smartcore.proxy.SmartCoreBungeeCord;
import java.io.File;
import java.util.*;

public class TabHandler extends FilePluginConfig {

    private static final SmartCoreBungeeCord plugin = SmartCoreBungeeCord.getPlugin();

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
        super(new File(plugin.getDataFolder(), "tab_" + language.name() + ".json"));
        handlers.put(language, this);
        load();
        registerDefault(HEADER_KEY, "Linea1");
        registerDefault(FOOTER_KEY, "Linea2");
        save();
    }

    public void sendTab(ProxiedPlayer player, BaseComponent header, BaseComponent footer) {
        player.setTabHeader(header, footer);
    }

    public int getMaxSizeOfList(String path) {
        return getList(path, String.class).size();
    }

    public BaseComponent getCurrentHeader() {
        String value = get(HEADER_KEY, String.class);
        return new TextComponent(ChatUtil.parse(LegacyComponentSerializer.legacySection().deserialize(value).content()));
    }

    public BaseComponent getCurrentFooter() {
        String value = get(FOOTER_KEY, String.class);
        return new TextComponent(ChatUtil.parse(LegacyComponentSerializer.legacySection().deserialize(value).content()));
    }

    public static Collection<TabHandler> getTabHandlers() {
        return handlers.values();
    }

}
