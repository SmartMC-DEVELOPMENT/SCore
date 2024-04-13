package us.smartmc.snowgames.manager;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.Language;
import us.smartmc.snowgames.messages.PluginMessages;

import java.util.List;
import java.util.Random;

public class DeathMessagesManager extends ListMessagesManager {

    public static final DeathMessagesManager INSTANCE = new DeathMessagesManager();

    public DeathMessagesManager() {
        super("death_messages_list");
    }
}
