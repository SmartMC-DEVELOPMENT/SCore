package us.smartmc.lobbymodule.messages;

import me.imsergioh.pluginsapi.language.LangMessagesInfo;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;
import org.bson.Document;

import static us.smartmc.lobbymodule.messages.LobbyMessages.item;

@LangMessagesInfo(name = "minigames")
public class MinigamesMessages extends MultiLanguageRegistry {

    @Override
    public void load(LanguageMessagesHolder holder) {
        holder.load();
        //item(holder, "store", "&e&lServer Store", "&7Take a look at our store and if you feel like it, buy something :)");
        //registerMinigame(holder, "snowmatch");
        holder.save();
    }

    public static void registerMinigame(LanguageMessagesHolder holder, String name) {
        Document document  = new Document()
                .append("name", "&b" + name)
                .append("description", "Descripción predeterminada sin nada que determinar.");
        holder.registerDefault(name, document);
    }
}
