package us.smartmc.lobbymodule.messages;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.language.LangMessagesInfo;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;
import org.bukkit.Material;
import us.smartmc.lobbymodule.instance.PlayerVisibility;

import java.util.Arrays;
import java.util.List;

@LangMessagesInfo(name = LobbyMessages.NAME)
public class LobbyMessages extends MultiLanguageRegistry {

    public static final String NAME = "lobby";

    @Override
    public void load(LanguageMessagesHolder holder) {
        holder.load();
        holder.registerDefaultOrMigrate("items.right_click", "items.right_click", "&8(Right-Click)");

        holder.registerDefaultOrMigrate("items.lobby_name", "items.lobby.name", "&aLobby");
        holder.registerDefaultOrMigrate("items.lobby_description", "items.lobby.description", "&7Connect to lobby ");

        holder.remove("connecting_to");
        holder.registerDefaultOrMigrate("title_lobbies_menu", "title.lobbies_menu", "Lobbies");
        holder.registerDefaultOrMigrate("title_settings_menu", "title.lobbies_menu", "Settings");

        holder.registerDefault("no_store_benefit", "&aAcquire a package from the store that contains this feature.");

        holder.registerDefaultOrMigrate("fly_enabled", "fly.enabled", "&aFlight mode has been enabled.");
        holder.registerDefaultOrMigrate("fly_disabled", "fly.disabled","&cFlight mode has been disabled.");

        holder.registerDefault("not_vip", "&cYou are not vip do this!");

        holder.remove("join_title");
        holder.remove("join_subtitle");
        holder.registerDefault("join_message", "&r{0} &6has joined the lobby!");

        holder.registerDefault("lore_fly_enabled", "&7Disable your flight mode");
        holder.registerDefault("lore_fly_disabled", "&7Enable your flight mode");
        holder.registerDefault("lore_fly_toggle", "&7Toggle your flight mode in all the available lobbies");
        holder.registerDefault("not_available_message", "\n&c&lThis game isn't available!\n\n&7We are working on it. Stay tuned!\n\n&bsmartmc.us\n");

        holder.registerDefault("unknown_command", "&cUnknown command. Type &e/help &cfor help.");

        holder.registerDefault("main_lobby_name", "&aLobby principal #{0}");
        holder.registerDefault("sg_lobby_name", "&aLobby #{0} de SnowGames");

        holder.registerDefault("lobby_name_prefix", "&a");
        holder.registerDefault("current_lobby_name_prefix", "&c");

        holder.registerDefault("already_connected", "&cAlready connected!");

        holder.registerDefault("feature_in_development", "&cThis feature is currently in development.");

        holder.registerDefault("menu_link_socials_title", "Link socials");
        holder.registerDefault("menu_show_socials_title", "Show socials");
        holder.registerDefaultOrMigrate("link_socials_invalid_input", "link_socials.invalid_input", "You introduced an invalid input!");
        holder.registerDefaultOrMigrate("link_socials_linked_correctly", "link_socials.linked_correctly",  "You have successfully linked the social network!");
        holder.registerDefaultOrMigrate("link_socials_introduce_url", "link_socials.introduce_url", "&8➤ &bPlease introduce your social network link in the chat");
        holder.registerDefaultOrMigrate("link_socials_socials_cmd.usage", "link_socials.socials_cmd.usage", "&cCorrect usage: /socials <name>");
        holder.registerDefaultOrMigrate("link_socials_description_show", "link_socials.description_show",  "&7Here you have the link of {0}:\n\n&fLink:&e {1}\n\n&a▶ Click to open");
        holder.registerDefaultOrMigrate("link_socials_no_linked", "link_socials.no_linked",  "&cThis user does not have an account already linked on that social network!");
        holder.registerDefaultOrMigrate("link_socials_show_message", "link_socials.show_message", "&bClick here to open {0}'s {1}");
        holder.registerDefaultOrMigrate("link_socials_target_not_found", "link_socials.target_not_found", "&cPlayer not found!");
        holder.registerDefaultOrMigrate("link_socials_unlinked_correctly", "link_socials.unlinked_correctly", "&cYou have unlinked the social media correctly!");

        item(holder, "link_social_network", "{0}",
                "&7Here you have the link of {0}&7:\n\n&fUsername:&e {1}\n&fExample:&e {2}\n\n&a▶ Click to set\n&c▶ Right-Click to unlink");

        item(holder, "link_socials", "&aLink your social networks", "&7You can link your social networks to SmartMC\nto promote your personal brand");

        for (PlayerVisibility visibility : PlayerVisibility.values()) {
            holder.registerDefault("visibility_" + visibility.name() + "_name", "visibility");
        }

        item(holder, "language",
                "&aChange language",
                "&7Change your language at the entire network");

        item(holder, "minigames", "&aMinigames <lang.lobby.items.right_click>", "&7Open minigames menu to play");
        item(holder, "settings", "&aSettings <lang.lobby.items.right_click>", "&7Adjust your custom settings");
        item(holder, "lobbies", "&aLobbies <lang.lobby.items.right_click>", "&7Connect to another lobbies instances");
        item(holder, "visibility", "&bChange visibility", "");
        item(holder, "flying", "&bToggle flying", "");
        item(holder, "not_available", "&c&lNot available", "&7This game is in development and\n&7it will be published soon.\n\n&eStay tuned!");
        item(holder, "terms", "&aTerms and Conditions", "&7Accept our terms and conditions to continue");

        item(holder, "cosmetics", "&bCosmetics <lang.lobby.items.right_click>", "&7Open cosmetics menu and change your current cosmetics.");

        holder.save();
    }

    public static String getLangList(String name) {
        return "<lang.lobby." + name + ">";
    }

    public static ItemBuilder getItem(Material material, String name) {
        String namePath = "<lang.lobby.items." + name + ".name>";
        String lorePath = "<lang.lobby.items." + name + ".description>";
        return ItemBuilder.of(material)
                .name(namePath)
                .lore(lorePath);
    }

    static void item(LanguageMessagesHolder holder, String name, String displayName, String description) {
        String mainPath = "items." + name + ".";
        holder.registerDefault(mainPath + "name", displayName);
        holder.registerDefault(mainPath + "description", description);
    }

}
