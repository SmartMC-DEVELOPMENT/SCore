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
        holder.registerDefault("items_right_click", "&8(Right-Click)");
        holder.registerDefault("items_lobby_name", "&aLobby");
        holder.registerDefault("items_lobby_description", "&7Connect to lobby ");
        holder.registerDefault("connecting_to", "&7Connecting to ");
        holder.registerDefault("title_lobbies_menu", "Lobbies");
        holder.registerDefault("title_settings_menu", "Settings");

        holder.registerDefault("no_store_benefit", "&aAcquire a package from the store that contains this feature.");

        holder.registerDefault("fly_enabled", "&aFlight mode has been enabled.");
        holder.registerDefault("fly_disabled", "&cFlight mode has been disabled.");

        holder.registerDefault("not_vip", "&cYou are not vip do this!");

        holder.registerDefault("join_title", "&b&lWelcome!");
        holder.registerDefault("join_subtitle", "&rThank you for coming, enjoy your stay! :D");
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
        holder.registerDefault("link_socials_invalid_input", "You introduced an invalid input!");
        holder.registerDefault("link_socials_linked_correctly", "You have successfully linked the social network!");
        holder.registerDefault("link_socials_introduce_url", "&8➤ &bPlease introduce your social network link in the chat");
        holder.registerDefault("link_socials.socials_cmd.usage", "&cCorrect usage: /socials <name>");
        holder.registerDefault("link_socials_description_show",  "&7Here you have the link of {0}:\n\n&fLink:&e {1}\n\n&a▶ Click to open");
        holder.registerDefault("link_socials_no_linked",  "&cThis user does not have an account already linked on that social network!");
        holder.registerDefault("link_socials_show_message",  "&bClick here to open {0}'s {1}");
        holder.registerDefault("link_socials_target_not_found",  "&cPlayer not found!");
        holder.registerDefault("link_socials_unlinked_correctly", "&cYou have unlinked the social media correctly!");

        item(holder, "link_social_network", "{0}",
                "&7Here you have the link of {0}&7:\n\n&fUsername:&e {1}\n&fExample:&e {2}\n\n&a▶ Click to set\n&c▶ Right-Click to unlink");

        item(holder, "link_socials", "&aLink your social networks", "&7You can link your social networks to SmartMC\nto promote your personal brand");

        for (PlayerVisibility visibility : PlayerVisibility.values()) {
            holder.registerDefault("visibility_" + visibility.name() + "_name", "visibility");
        }

        item(holder, "language",
                "&aChange language",
                "&7Change your language at the entire network");

        item(holder, "minigames", "&aMinigames <lang.lobby.items_right_click>", "&7Open minigames menu to play");
        item(holder, "settings", "&aSettings <lang.lobby.items_right_click>", "&7Adjust your custom settings");
        item(holder, "lobbies", "&aLobbies <lang.lobby.items_right_click>", "&7Connect to another lobbies instances");
        item(holder, "visibility", "&bChange visibility", "");
        item(holder, "flying", "&bToggle flying", "");
        item(holder, "not_available", "&c&lNot available", "&7This game is in development and\n&7it will be published soon.\n\n&eStay tuned!");
        item(holder, "terms", "&aTerms and Conditions", "&7Accept our terms and conditions to continue");

        item(holder, "cosmetics", "&bCosmetics <lang.lobby.items_right_click>", "&7Open cosmetics menu and change your current cosmetics.");

        holder.save();
    }

    public static String getLangList(String name) {
        return "<lang.lobby." + name + ">";
    }

    public static ItemBuilder getItem(Material material, String name) {
        String namePath = "<lang.lobby.items_" + name + "_name>";
        String lorePath = "<lang.lobby.items_" + name + "_description>";
        return ItemBuilder.of(material)
                .name(namePath)
                .lore(Arrays.asList(lorePath));
    }

    static void item(LanguageMessagesHolder holder, String name, String displayName, String description) {
        String mainPath = "items_" + name + "_";
        holder.registerDefault(mainPath + "name", displayName);
        holder.registerDefault(mainPath + "description", description);
    }

}
