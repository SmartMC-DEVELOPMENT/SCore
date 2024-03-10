package us.smartmc.smartcore.smartcorevelocity.messages;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.LangMessagesInfo;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;

@LangMessagesInfo(name = "proxy_main")
public class ProxyMainMessages extends MultiLanguageRegistry {

    public static final String NAME = "proxy_main";

    @Override
    public void load(LanguageMessagesHolder holder) {
        holder.load();
        holder.registerDefault("no_staff", "&cYou are not a staff!");

        holder.registerDefault("no_permission", "&cYou don't have permission to do that!");
        holder.registerDefault("few_arguments_command_error", "&cYou've introduced too few arguments.");
        holder.registerDefault("too_much_arguments_command_error", "&cYou've introduced too much arguments.");
        holder.registerDefault("no_player_error", "&cThe player you're looking for doesn't exist.");
        holder.registerDefault("invalid_subcommand", "&cYou've introduced an invalid subcommand");
        holder.registerDefault("servers_not_found", "&cWe have not found servers to connect to!");

        holder.registerDefault("purchase_package_announce",
                "&r\n" +
                        "&9&kii&b &l{0} &9&kii&a ha adquirido un nuevo paquete!\n" +
                        "       {1}\n" +
                        "       &2¿Deseas un paquete como este? &aCómpralo en\n" +
                        "                &e&nhttps://tienda.smartmc.us\n" +
                        "                &r\n" +
                        "                           &d&lGG in the chat\n" +
                        "                           &r");

        holder.registerDefault("renew_package_announce",
                "&r\n" +
                        "&9&kii&b &l{0} &9&kii&a ha renovado un paquete!\n" +
                        "       {1}\n" +
                        "       &2¿Deseas un paquete como este? &aCómpralo en\n" +
                        "                &e&nhttps://tienda.smartmc.us\n" +
                        "                &r\n" +
                        "                           &d&lGG in the chat\n" +
                        "                           &r");

        holder.save();
    }

    public static String get(Language language, String path) {
        return LanguagesHandler.get(language).get(NAME).getString(path);
    }

}
