package us.smartmc.backend.connection;

import java.util.Timer;
import java.util.TimerTask;

public class ClientReconnectionTimer extends Timer {

    public ClientReconnectionTimer(BackendClient client) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                client.relogin();
            }
        };
        this.schedule(task, 250, 1000);
    }

    public void finish() {
        cancel();
    }

}
