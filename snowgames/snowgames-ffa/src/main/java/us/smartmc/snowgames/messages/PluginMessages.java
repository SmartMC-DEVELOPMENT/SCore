package us.smartmc.snowgames.messages;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.LangMessagesInfo;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;
import me.imsergioh.pluginsapi.util.ChatUtil;

import java.text.MessageFormat;
import java.util.Arrays;

@LangMessagesInfo(name = "snowgames/ffa/main")
public class PluginMessages extends MultiLanguageRegistry {

    public static final String NAME = "snowgames/ffa/main";

    @Override
    public void load(LanguageMessagesHolder holder) {
        holder.load();
        holder.registerDefault("error_cant_join", "&cError while trying to join to the ffa game");
        holder.registerDefault("cant_join_spawn_ingame", "&cYou can't join at spawn in-game!");
        holder.registerDefault("kill_messages_list", Arrays.asList("&bEl jugador &c{0}&b ha sido asesinado por &a{1}&b!"));
        holder.registerDefault("death_messages_list", Arrays.asList("&bEl jugador &c{0}&b ha muerto!"));

        holder.registerDefault("not_available", "This feature is currently not available.");
        holder.registerDefault("tops_your_position", "Your position: &e{0}");

        holder.remove("setting_menu_title");
        holder.registerDefault("menu_title_setting", "Configure - {0}");
        registerSettingSection(holder, "blocks");
        registerSettingSection(holder, "projectiles");

        registerMenuTitle(holder, "tops", "Tops");
        registerMenuTitle(holder, "settings", "Settings");

        holder.save();
    }

    public static void registerMenuTitle(LanguageMessagesHolder holder, String id, String name) {
        holder.registerDefault("menu_title_" + id, name);
    }

    public static String getMenuTitle(Language language, String id) {
        return LanguagesHandler.get(language).get(NAME).getString("menu_title_" + id);
    }

    public static void registerSettingSection(LanguageMessagesHolder holder, String id) {
        holder.registerDefault("setting_" + id + "_name", "Setting@" + id);
    }

    public static String getSettingName(Language language, String id) {
        return LanguagesHandler.get(language).get(NAME).getString("setting_" + id + "_name");
    }

}
