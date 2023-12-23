package us.smartmc.lobbymodule.messages;

import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;
import org.bson.Document;

import static us.smartmc.lobbymodule.messages.LobbyMessages.item;

public class MinigamesMessages extends MultiLanguageRegistry {

    public MinigamesMessages() {
        super("lobby_miniGames", holder -> {
            holder.load();
            holder.registerDefault("inventory_title", "Minigames");
            holder.registerDefault("prototype_title", "&5&lPROTOTYPE");

            holder.registerDefault("click_to_connect", "&aClick to connect!");
            holder.registerDefault("current_playing", "{0} currently playing!");
            holder.registerDefault("in_maintenance", "&c&lIN MAINTENANCE!");

            item(holder, "discord", "&9&lDiscord Server", "&7Check our Discord Server and connect with us!");
            item(holder, "twitter", "&b&lOur Twitter", "&7Follow us on Twitter and stay tuned for all the news!");
            item(holder, "store", "&e&lServer Store", "&7Take a look at our store and if you feel like it, buy something :)");

            registerMinigame(holder, "snowmatch");
            holder.save();
        });
    }

    public static void registerMinigame(LanguageMessagesHolder holder, String name) {
        Document document  = new Document()
                .append("name", "&b" + name)
                .append("description", "Descripción predeterminada sin nada que determinar.");
        holder.registerDefault(name, document);
    }

}
