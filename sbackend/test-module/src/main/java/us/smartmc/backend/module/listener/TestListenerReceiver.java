package us.smartmc.backend.module.listener;

import org.bukkit.event.Event;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendObjectListener;

public class TestListenerReceiver extends BackendObjectListener<Event> {

    public TestListenerReceiver() {
        super(Event.class);
    }

    @Override
    public void onReceive(ConnectionHandler connection, Event event) {
        System.out.println("Received bukkit event! " + event);
    }
}
