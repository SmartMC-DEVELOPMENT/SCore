package us.smartmc.smartcore.smartcorevelocity.instance.onlinestore;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.VelocityChatUtil;
import net.kyori.adventure.text.Component;
import us.smartmc.smartcore.smartcorevelocity.instance.CoreCommand;
import us.smartmc.smartcore.smartcorevelocity.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.manager.VelocityPluginsAPI;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class AnnouncePackageRenew extends CoreCommand {

    public AnnouncePackageRenew() {
        super("announcePackageRenew");
    }

    @Override
    public void execute(CommandSource sender, String[] args) {
        if (sender instanceof Player) return;
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
            message = VelocityChatUtil.color(message);
            messages.put(language, message);
        }

        for (Player player : VelocityPluginsAPI.proxy.getAllPlayers()) {
            Language language = PlayerLanguages.getLanguage(player.getUniqueId());
            String message = messages.get(language);
            player.sendMessage(Component.text(message));
        }
        System.out.println(messages.get(Language.getDefault()));
    }
}
