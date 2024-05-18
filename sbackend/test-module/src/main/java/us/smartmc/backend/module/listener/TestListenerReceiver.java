package us.smartmc.backend.module.listener;

import org.bukkit.event.player.AsyncPlayerChatEvent;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendObjectListener;

public class TestListenerReceiver extends BackendObjectListener<AsyncPlayerChatEvent> {

    public TestListenerReceiver() {
        super(AsyncPlayerChatEvent.class);
    }

    @Override
    public void onReceive(ConnectionHandler connection, AsyncPlayerChatEvent event) {
        System.out.println("Received bukkit event! " + event);
    }
}
