package us.smartmc.smartcore.smartcorevelocity.messages;

import lombok.Getter;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.LangMessagesInfo;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;

import java.util.Arrays;

@LangMessagesInfo(name = "proxy_main")
public class HelpMessages extends MultiLanguageRegistry {

    @Getter
    public static String NAME = "proxy_main";

    @Getter
    private static HelpMessages instance;

    public HelpMessages() {
        instance = this;
    }

    @Override
    public void load(LanguageMessagesHolder holder) {
        holder.load();

        holder.registerDefault("help_message", Arrays.asList(
                "",
                "&b&lSmartMC &f&lNetwork &8&m   &f Help",
                "",
                "&a1. Server Info",
                "&a2. Utility Links",
                "&a3. Basic Commands",
                "&c&m4. Discord Sync",
                "",
                "&7&oClick on any of these options to select",
                ""
        ));

        holder.registerDefault("discord", Arrays.asList("", "&9&lOur Discord Server", "", "&fJoin our Discord Server and stay connected with us!", "", "&9https://discord.smartmc.us", ""));
        holder.registerDefault("twitter", Arrays.asList("", "&b&lFollow us on Twitter", "", "&fClick here to follow us on Twitter", "", "&bhttps://twitter.com/smartmc_net", ""));
        holder.registerDefault("store", Arrays.asList("", "&a&lTake a look at our store!", "", "&fClick to go to our store and buy a rank!", "", "&ahttps://store.smartmc.us", ""));

        holder.registerDefault("broadcast_incorrect_usage", "&cIncorrect usage! Try with: /broadcast <args>");
        holder.save();
    }

    public static String get(Language language, String path) {
        return LanguagesHandler.get(language).get("proxy_main").getString(path);
    }
}
