package us.smartmc.snowgames.manager;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.Language;
import us.smartmc.snowgames.messages.PluginMessages;

import java.util.List;
import java.util.Random;

public class KillMessagesManager {

    private static final Random random = new Random();

    public static int getRandomMessageIndex() {
        return random.nextInt(getMessages(Language.getDefault()).size());
    }

    public static String getRandomMessageFromList(int index, Language language) {
        List<String> list = getMessages(language);
        return list.get(index);
    }

    public static List<String> getMessages(Language language) {
        return LanguagesHandler.get(language).get(PluginMessages.NAME).getList("kill_messages_list", String.class);
    }

}
