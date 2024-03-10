package me.imsergioh.smartcorewaterfall.instance.onlinestore;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.smartcorewaterfall.SmartCoreWaterfall;
import me.imsergioh.smartcorewaterfall.instance.PlayerLanguages;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class AnnouncePackageRenew extends Command {

    public AnnouncePackageRenew() {
        super("announcePackageRenew");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) return;
        String username = args[0];
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            stringBuilder.append(args[i]).append(" ");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        String packageName = stringBuilder.toString();

        Map<Language, String> messages = new HashMap<>();

        for (Language language : Language.values()) {
            String message = LanguagesHandler.get(language).get("proxy_main").getString("renew_package_announce");
            message = MessageFormat.format(message, username, packageName);
            message = ChatUtil.color(message);
            messages.put(language, message);
        }

        for (ProxiedPlayer player : SmartCoreWaterfall.getPlugin().getProxy().getPlayers()) {
            Language language = PlayerLanguages.getLanguage(player.getUniqueId());
            String message = messages.get(language);
            player.sendMessage(message);
        }
        System.out.println(messages.get(Language.getDefault()));
    }
}
