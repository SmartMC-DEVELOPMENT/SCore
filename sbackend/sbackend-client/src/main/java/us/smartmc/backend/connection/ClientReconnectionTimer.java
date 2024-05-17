package us.smartmc.backend.connection;

import java.util.Timer;
import java.util.TimerTask;

public class ClientReconnectionTimer extends Timer {

    private final TimerTask task;

    public ClientReconnectionTimer(BackendClient client) {
        long lastLogin = client.getLastSuccesLogin();
        task = new TimerTask() {
            @Override
            public void run() {
                long current = client.getLastSuccesLogin();
                if (current != lastLogin) finish();
                client.relogin();
            }
        };
        this.schedule(task, 250, 1000);
    }

    private void finish() {
        purge();
    }

}
