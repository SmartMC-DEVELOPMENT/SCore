package us.smartmc.smartcore.smartcorevelocity.messages;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.LangMessagesInfo;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;

@LangMessagesInfo(name = "sanctions_manager")
public class SanctionsManagerMessages extends MultiLanguageRegistry {

    public static final String NAME = "sanctions_manager";

    @Override
    public void load(LanguageMessagesHolder holder) {
        holder.load();
        holder.registerDefault("cmd_correct_usage_ban", "&cCorrect usage: /ban <player> -r <reason> -t <time>");
        holder.registerDefault("cmd_correct_usage_mute", "&cCorrect usage: /mute <player> -r <reason> -t <time>");
        holder.registerDefault("cmd_correct_usage_kick", "&cCorrect usage: /kick <player> -r <reason>");
        holder.registerDefault("cmd_correct_usage_warn", "&cCorrect usage: /warn <player> -r <reason>");

        holder.registerDefault("cmd_sanction_success", "&aSanctioned correctly!");

        holder.registerDefault("no_reason", "No reason");
        holder.registerDefault("no_expiration", "&c&lPERMANENT");

        holder.registerDefault("kick_message", "&cYou have been kicked from the Network!\n&r\n&rReason:&a {0}");
        holder.registerDefault("ban_message", "&cYou are banned from the Network!\n&r\n&rReason:&a {0}\n&rDuration:&e {1}");
        holder.registerDefault("message_been_muted", "&cYou have been muted!\n&rReason:&a {0}\n&rTime left:&e {1}");
        holder.registerDefault("message_muted", "&cYou are currently muted!\n&rReason:&a {0}\n&rTime left:&e {1}");
        holder.registerDefault("message_been_warned", "&cYou have been warned!\n&rReason:&a {0}");
        holder.save();
    }

    public static String get(Language language, String path) {
        return LanguagesHandler.get(language).get(NAME).getString(path);
    }
}
