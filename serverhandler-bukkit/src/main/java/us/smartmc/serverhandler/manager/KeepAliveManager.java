package us.smartmc.serverhandler.manager;

import us.smartmc.serverhandler.ServerHandlerMain;
import us.smartmc.serverhandler.util.ConnectionUtil;

import java.util.Timer;
import java.util.TimerTask;

public class KeepAliveManager {

    private static final TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            try {
                ConnectionUtil.sendCommand(ServerHandlerMain.getConnection().getConnectionHandler(), "keepAlive " + ServerHandlerMain.getServerName());
            } catch (Exception e) {
                System.out.println("Error while sending keep alive without backend connection working");
            }
        }
    };

    private static boolean alreadyStarted = false;
    private static final Timer timer = new Timer();

    public static void startKeepAliveTask() {
        if (alreadyStarted) return;
        timer.schedule(timerTask, 1000, 2000);
        alreadyStarted = true;
    }

}
