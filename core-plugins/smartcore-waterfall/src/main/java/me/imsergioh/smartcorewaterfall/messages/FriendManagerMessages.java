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

        holder.registerDefault("request_sent", "&aFriend request sent to the player {0}.");
        holder.registerDefault("join_pending", "&aYou have {0} notification pending.");

        holder.registerDefault("already_friend_error", "&cThis player is already your friend.");
        holder.registerDefault("already_requested_error", "&cYou have already requested this player as a friend.");
        holder.registerDefault("self_request_error", "&cWe understand that you may have few friends, and we understand your pain, but you cannot alleviate it by adding yourself as a friend. Go and play with other people, talk and chat so you can fill this list as soon as possible.");
    }

    public static String get(Language language, String path) {
        return LanguagesHandler.get(language).get(NAME).getString(path);
    }
}
