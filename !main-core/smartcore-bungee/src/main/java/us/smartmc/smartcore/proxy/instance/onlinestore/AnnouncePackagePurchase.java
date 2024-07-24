package us.smartmc.smartcore.proxy.instance.onlinestore;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.manager.BungeeCordPluginsAPI;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.smartmc.smartcore.proxy.instance.CoreCommand;
import us.smartmc.smartcore.proxy.instance.PlayerLanguages;

import java.util.HashMap;
import java.util.Map;

public class AnnouncePackagePurchase extends CoreCommand {

    public AnnouncePackagePurchase() {
        super("announcePackagePurchase");
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

        Map<Language, BaseComponent> messages = new HashMap<>();

        for (Language language : Language.values()) {
            String textMessage = getPurchasePackageAnnounce(language);
            BaseComponent message = ChatUtil.parse(textMessage, username, packageName);
            messages.put(language, message);
        }

        for (ProxiedPlayer player : BungeeCordPluginsAPI.proxy.getPlayers()) {
            Language language = PlayerLanguages.getLanguage(player.getUniqueId());
            BaseComponent message = messages.get(language);
            player.sendMessage(message);
        }
        BungeeCordPluginsAPI.proxy.getConsole().sendMessage(messages.get(Language.getDefault()));
    }

    private String getPurchasePackageAnnounce(Language language) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : LanguagesHandler.get(language).get("proxy_main").getList("purchasePackageAnnounce", String.class)) {
            stringBuilder.append(line);
            if (!line.contains("\n")) stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

}
