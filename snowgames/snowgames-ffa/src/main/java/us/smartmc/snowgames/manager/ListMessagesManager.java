package us.smartmc.snowgames.manager;

import lombok.Getter;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.Language;
import us.smartmc.snowgames.messages.PluginMessages;

import java.util.List;
import java.util.Random;

public class ListMessagesManager {

    @Getter
    private final String name;
    private final Random random = new Random();

    public ListMessagesManager(String name) {
        this.name = name;
    }

    public int getRandomMessageIndex() {
        return random.nextInt(getMessages(Language.getDefault()).size());
    }

    public String getRandomMessageFromList(int index, Language language) {
        List<String> list = getMessages(language);
        return list.get(index);
    }

    public List<String> getMessages(Language language) {
        return LanguagesHandler.get(language).get(PluginMessages.NAME).getList(name, String.class);
    }

}
