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
