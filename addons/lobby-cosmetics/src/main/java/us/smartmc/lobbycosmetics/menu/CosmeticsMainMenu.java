package us.smartmc.lobbycosmetics.menu;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.ConfigurableMenu;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.language.LanguageHolder;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.lobbycosmetics.LobbyCosmetics;
import us.smartmc.lobbycosmetics.instance.helper.CosmeticLanguageInfo;

import java.io.File;
import java.util.List;

public class CosmeticsMainMenu extends CosmeticsAddonMenu {

    private static final LobbyCosmetics addon = LobbyCosmetics.getInstance();

    public CosmeticsMainMenu(Player player) {
        super(player, "cosmetics_main");
    }

    @Override
    public void registerDefaults() {
        registerMenuItem("hats", 12);
        registerMenuItem("pets", 14);
    }

}

