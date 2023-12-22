package us.smartmc.core.pluginsapi.spigot.player;

import us.smartmc.core.pluginsapi.data.player.OfflineCorePlayer;
import us.smartmc.core.pluginsapi.handler.LanguagesHandler;
import us.smartmc.core.pluginsapi.instance.PlayerLanguages;
import us.smartmc.core.pluginsapi.language.Language;
import us.smartmc.core.pluginsapi.spigot.event.PlayerLanguageChangeEvent;
import us.smartmc.core.pluginsapi.spigot.event.PlayerLanguageChangedEvent;
import us.smartmc.core.pluginsapi.spigot.menu.CoreMenu;
import us.smartmc.core.pluginsapi.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.smartmc.core.pluginsapi.util.SyncUtil;

import java.util.*;

public class CorePlayer extends OfflineCorePlayer<Player> {

    private static final HashMap<UUID, CorePlayer> players = new HashMap<>();

    protected final UUID uuid;
    protected final Player player;

    protected final CorePlayerData playerData;

    private CoreMenu currentMenuOpen;
    private CoreMenu previousOpenMenu;
    private CoreMenu currentMenuSet;

    public CorePlayer(UUID uuid) {
        super(uuid);
        this.uuid = uuid;
        this.player = get();
        this.playerData = new CorePlayerData(this);
        players.put(uuid, this);
        load();
    }

    public void clearInventory() {
        player.getInventory().clear();
        currentMenuSet = null;
    }

    public void registerMenuHistory(CoreMenu coreMenu) {
        if (previousOpenMenu != null) {
            if (previousOpenMenu.getClass().getName().equals(coreMenu.getClass().getName())) return;
        }
        previousOpenMenu = coreMenu;
    }

    public void openPreviousMenu() {
        if (previousOpenMenu != null) {
            previousOpenMenu.open(player);
        }
    }


    @Override
    public void unload() {
        PlayerLanguages.unregister(uuid);
        playerData.save();
        super.unload();
    }

    public CorePlayerData getPlayerData() {
        return playerData;
    }

    public UUID getUUID() {
        return uuid;
    }

    @Override
    public Player get() {
        if (player != null) {
            return player;
        }
        return Bukkit.getPlayer(uuid);
    }

    public void setCurrentMenuOpen(CoreMenu currentMenuOpen) {
        this.currentMenuOpen = currentMenuOpen;
    }

    public void setCurrentMenuSet(CoreMenu currentMenuSet) {
        this.currentMenuSet = currentMenuSet;
    }

    public CoreMenu getCurrentMenuOpen() {
        return currentMenuOpen;
    }

    public CoreMenu getCurrentMenuSet() {
        return currentMenuSet;
    }

    public void setLanguage(Language language) {
        Language previousLang = getLanguage();
        if (previousLang.equals(language)) return;
        Bukkit.getPluginManager()
                .callEvent(new PlayerLanguageChangeEvent(this, previousLang, language));
        playerData.setData("lang", language.name());
        PlayerLanguages.register(uuid, language);
        sendLanguageMessage("general", "lang_change");
        if (player != null)
            SyncUtil.sync(() -> {
                Bukkit.getPluginManager()
                        .callEvent(new PlayerLanguageChangedEvent(this, previousLang, language));
            });
    }

    public void sendLanguageMessage(String holderName, String key, Object... vars) {
        if (player == null) return;
        player.sendMessage(getLanguageMessage(holderName, key, vars));
    }

    public String getLanguageMessage(String holderName, String key, Object... vars) {
        String message = null;
        try {
            message = LanguagesHandler.get(getLanguage())
                    .get(holderName).getString(key);
        } catch (Exception ignore) {
        }
        return message == null ? "undefined" : ChatUtil.parse(player, message, vars);
    }

    public Language getLanguage() {
        return PlayerLanguages.get(player.getUniqueId());
    }

    public static CorePlayer get(UUID uuid) {
        return players.get(uuid);
    }

    public static CorePlayer get(Player player) {
        return get(player.getUniqueId());
    }
}
