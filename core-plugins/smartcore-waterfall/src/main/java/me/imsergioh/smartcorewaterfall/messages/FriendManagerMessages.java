package me.imsergioh.smartcorewaterfall.messages;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;

public class FriendManagerMessages extends MultiLanguageRegistry {

    public static final String NAME = "friend_manager";

    public FriendManagerMessages() {
        super(NAME, holder -> {
            holder.load();
            FriendManagerMessages.registerDefaultMessages(holder);
            holder.save();
        });
    }

    private static void registerDefaultMessages(LanguageMessagesHolder holder) {
        holder.registerDefault("request_received", "&aYou received a friend request of {0}.\n{1} {2} {3}");
        holder.registerDefault("request_received_accept", "&aACCEPT");
        holder.registerDefault("request_received_ignore", "&7IGNORE");
        holder.registerDefault("request_received_deny", "&cDENY");

        holder.registerDefault("response_received", "&aYou received a response from {0}.");
        holder.registerDefault("join_pending", "&aYou have {0} notification pending.");

        holder.registerDefault("response_status_accepted", "&aaccepted");
        holder.registerDefault("response_status_denied", "&cdenied");
    }

    public static String get(Language language, String path) {
        return LanguagesHandler.get(language).get(NAME).getString(path);
    }
}
