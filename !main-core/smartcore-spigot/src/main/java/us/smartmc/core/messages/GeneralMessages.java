package us.smartmc.core.messages;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.LangMessagesInfo;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;

@LangMessagesInfo(name = "general")
public class GeneralMessages extends MultiLanguageRegistry {

    public static String NAME = "general";

    public static String COMMAND_NO_PERMISSION_PATH = "noPermission";
    public static String ERROR_OCCURRED_PATH = "error_ocurred";

    public static String getString(Language language, String path) {
        return LanguagesHandler.get(language).get(NAME).getString(path);
    }

    @Override
    public void load(LanguageMessagesHolder holder) {
        holder.load();
        holder.save();
    }
}
