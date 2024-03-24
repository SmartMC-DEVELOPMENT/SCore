package us.smartmc.moderation.staffmodebungee.manager;

import lombok.Getter;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.LangMessagesInfo;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;
import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.smartcorewaterfall.instance.PlayerLanguages;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.smartmc.moderation.staffmodebungee.message.StaffMessages;

public class MessagesManager {

    @Getter
    private static StaffMessages messages;

    public static void load() {
        messages = new StaffMessages();
    }

    public static void send(ProxiedPlayer player, Class<? extends MultiLanguageRegistry> clazz, String msgPath, Object... args) {
        String name = clazz.getDeclaredAnnotation(LangMessagesInfo.class).name();
        String message = LanguagesHandler.get(PlayerLanguages.getLanguage(player)).get(name).getString(msgPath);
        player.sendMessage(ChatUtil.parse(player, message, args));
    }
}
