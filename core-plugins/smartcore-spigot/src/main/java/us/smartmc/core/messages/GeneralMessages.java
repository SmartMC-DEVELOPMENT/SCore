package us.smartmc.core.messages;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;

public class GeneralMessages extends MultiLanguageRegistry {

    public static String NAME = "general";

    public static String COMMAND_NO_PERMISSION_PATH = "noPermission";
    public static String ERROR_OCCURRED_PATH = "error_ocurred";

    public GeneralMessages() {
        super(NAME, holder -> {
            holder.load();
            holder.registerDefault("acquireRank", "Acquire it!");
            holder.registerDefault(COMMAND_NO_PERMISSION_PATH, "&cYou don't have permissions to execute this command!");

            holder.registerDefault("lang_change", "&aLanguage changed.");

            holder.registerDefault("menu_lang_title", "Change language");

            holder.registerDefault(ERROR_OCCURRED_PATH, "&cError ocurred! {0}");

            holder.registerDefault("coin_added", "&6+{0} SmartCoin! {1}");
            holder.registerDefault("coins_added", "&6+{0} SmartCoins! {1}");

            holder.registerDefault("coin_removed", "&6-{0} SmartCoin! {1}");
            holder.registerDefault("coins_removed", "&6-{0} SmartCoins! {1}");

            holder.registerDefault("year", "year");
            holder.registerDefault("years", "years");
            holder.registerDefault("month", "month");
            holder.registerDefault("months", "months");
            holder.registerDefault("week", "week");
            holder.registerDefault("weeks", "weeks");
            holder.registerDefault("day", "day");
            holder.registerDefault("days", "days");
            holder.registerDefault("hour", "hour");
            holder.registerDefault("hours", "hours");
            holder.registerDefault("minute", "minute");
            holder.registerDefault("minutes", "minutes");
            holder.registerDefault("second", "second");
            holder.registerDefault("seconds", "seconds");

            holder.registerDefault("region_pos1", "&aSelected position 1 &e{0}");
            holder.registerDefault("region_pos2", "&aSelected position 2 &e{0}");

            holder.save();
        });
    }

    public static String getString(Language language, String path) {
        return LanguagesHandler.get(language).get(NAME).getString(path);
    }

}
