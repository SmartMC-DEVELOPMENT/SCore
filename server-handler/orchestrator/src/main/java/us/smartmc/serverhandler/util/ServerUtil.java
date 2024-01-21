package us.smartmc.serverhandler.util;

import java.io.File;
import java.net.ServerSocket;

public class ServerUtil {

    public static String COUNT_SEPARATOR = "";

    public static boolean isPortInUse(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            serverSocket.close();
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public static void startServer(File dir) {
        new Thread(() -> {
            try {
                final Process process = Runtime.getRuntime().exec("screen -Sdm " + dir.getName() + " sh start.sh", null, dir);
                process.waitFor();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

}
