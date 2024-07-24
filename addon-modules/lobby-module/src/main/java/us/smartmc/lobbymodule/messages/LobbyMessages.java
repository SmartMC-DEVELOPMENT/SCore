package us.smartmc.lobbymodule.messages;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.language.LangMessagesInfo;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;
import org.bukkit.Material;

import java.util.List;

@LangMessagesInfo(name = LobbyMessages.NAME)
public class LobbyMessages extends MultiLanguageRegistry {

    public static final String NAME = "lobby";

    @Override
    public void load(LanguageMessagesHolder holder) {
        holder.load();
        holder.registerDefault("your_discordLink_code_is", "Your discord link code is: &9{0}");

        holder.registerDefault("join_embedmessage", List.of(
                "&r",
                "&f¡Welcome to <bold><egradient:#00BFFF>SmartMC<reset>&f <name>&f!",
                " &a\uD83D\uDED2 tienda.smartmc.us",
                " &9\uD83D\uDCAC discord.smartmc.us",
                " &f\uD835\uDD4F twitter.smartmc.us",
                " &b\uD83C\uDF10 www.smartmc.us"
                ));

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
