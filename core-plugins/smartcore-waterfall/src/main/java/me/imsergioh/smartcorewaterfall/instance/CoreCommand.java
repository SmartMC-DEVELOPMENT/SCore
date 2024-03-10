package me.imsergioh.smartcorewaterfall.instance;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;

public abstract class CoreCommand extends Command {

    public CoreCommand(String name) {
        super(name);
    }

    public CoreCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void sendStringMessage(String name, CommandSender sender, String path, Object... args) {
        Language language = Language.getDefault();
        if (sender instanceof ProxiedPlayer)
            language = PlayerLanguages.getLanguage(((ProxiedPlayer) sender).getUniqueId());

        String message = LanguagesHandler.get(language).get(name).getString(path);
        sender.sendMessage(ChatUtil.parse(message, args));
    }

    public void sendFormattedList(String name, CommandSender sender, String path, Object... args) {
        sender.sendMessage(ChatUtil.parse(getFormattedList(name, sender, path), args));
    }

    private String getFormattedList(String name, CommandSender sender, String path) {
        StringBuilder stringBuilder = new StringBuilder();
        ProxiedPlayer player = null;
        if (sender instanceof ProxiedPlayer) player = (ProxiedPlayer) sender;
        Language language = Language.getDefault();
        if (player != null) language = PlayerLanguages.getLanguage(player.getUniqueId());
        List<String> list = LanguagesHandler.get(language).get(name).getList(path, String.class);

        for (String line : list) {
            if (player != null)
                stringBuilder.append(ChatUtil.parse(player, line) + "\n");
            else stringBuilder.append(ChatUtil.parse(line) + "\n");
        }
        return stringBuilder.toString();
    }
}
