package us.smartmc.smartcore.smartcorevelocity.manager;

import com.velocitypowered.api.proxy.Player;
import lombok.Getter;
import me.imsergioh.pluginsapi.instance.MongoDBPluginConfig;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.manager.VelocityPluginsAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bson.Document;
import us.smartmc.smartcore.smartcorevelocity.instance.PlayerLanguages;

import java.util.*;

public class TabHandler extends MongoDBPluginConfig {

    @Getter
    private static final HashMap<Language, TabHandler> handlers = new HashMap<>();

    private int currentHeaderIndex, currentFooterIndex;

    public static void register() {
        for (Language language : Language.values()) {
            new TabHandler(language);
        }
    }

    private final String HEADER_KEY = "header";
    private final String FOOTER_KEY = "footer";

    private final Timer timer;

    public TabHandler(Language language) {
        super("proxy_data", "tab_handler", new Document("lang", language.name().toUpperCase()));
        handlers.put(language, this);
        load();
        registerDefault(HEADER_KEY, List.of("Hello world!\nLine2!?", "HOLAAAAAA HEADEEEER", "RATATATAA"));
        registerDefault(FOOTER_KEY, List.of("LINE1", "LINE2", "LINE3"));
        save();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int maxHeader = getMaxSizeOfList(HEADER_KEY);
                int maxFooter = getMaxSizeOfList(FOOTER_KEY);
                int nextHeaderIndex = (currentHeaderIndex >= maxHeader -1) ? 0 : (++currentHeaderIndex);
                int nextFooterIndex = (currentFooterIndex >= maxFooter -1) ? 0 : (++currentFooterIndex);
                currentHeaderIndex = nextHeaderIndex;
                currentFooterIndex = nextFooterIndex;
                Component header = getCurrentHeader();
                Component footer = getCurrentFooter();
                VelocityPluginsAPI.proxy.getAllPlayers().forEach(p -> {
                    if (!PlayerLanguages.getLanguage(p.getUniqueId()).equals(language)) return;
                    sendTab(p, header, footer);
                });
            }
        }, 0, getInteger("interval"));
    }

    public void suspendTimer() {
        timer.purge();
    }

    public void sendTab(Player player, Component header, Component footer) {
        player.sendPlayerListHeaderAndFooter(header, footer);
    }

    public int getMaxSizeOfList(String path) {
        return getList(path, String.class).size();
    }

    public Component getCurrentHeader() {
        String value = getList(HEADER_KEY, String.class).get(currentHeaderIndex);
        MiniMessage miniMessage = MiniMessage.miniMessage();
        return miniMessage.deserialize(LegacyComponentSerializer.legacySection().deserialize(value).content());
    }

    public Component getCurrentFooter() {
        String value = getList(FOOTER_KEY, String.class).get(currentFooterIndex);
        MiniMessage miniMessage = MiniMessage.miniMessage();
        return miniMessage.deserialize(LegacyComponentSerializer.legacySection().deserialize(value).content());
    }

    public static Collection<TabHandler> getTabHandlers() {
        return handlers.values();
    }

}
