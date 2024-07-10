package us.smartmc.backend.connection;

import java.util.Timer;
import java.util.TimerTask;

public class ClientReconnectionTask {

    private Timer timer;
    private TimerTask task;
    private final BackendClient client;

    private boolean waitingReconnection;

    public ClientReconnectionTask(BackendClient client) {
        this.client = client;
    }

    private void reconnect() {
        client.relogin();
    }

    public void startReconnectionTask() {
        if (waitingReconnection) return;

        waitingReconnection = true;
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                reconnect();
            }
        };
        timer.schedule(task, 250, 1000);
    }

    public void successLogin() {
        if (!waitingReconnection) return;
        task.cancel();
        timer.cancel();
        waitingReconnection = false;
    }

}
