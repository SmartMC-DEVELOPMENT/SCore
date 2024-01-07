package us.smartmc.snowgames.manager;

public class KillMessagesManager extends ListMessagesManager {

    public static final KillMessagesManager INSTANCE = new KillMessagesManager();

    public KillMessagesManager() {
        super("kill_messages_list");
    }
}
