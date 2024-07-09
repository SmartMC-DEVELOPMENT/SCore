package us.smartmc.serverhandler.util;

import java.io.File;
import java.io.FileWriter;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class ServerUtil {

    public static String COUNT_SEPARATOR = "";

    private static final Map<String, FileWriter> logOuts = new HashMap<>();

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
            Process process = null;
            try {
                // Ejemplo de comando directo sin usar screen
                String command = "screen -Sdm " + dir.getName() + " sh start.sh";
                process = Runtime.getRuntime().exec(command, null, dir);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (process != null) {
                    process.destroy();
                }
            }
        }).start();
    }


    private static File getLogFile(String id) {
        return new File("/home/network/server-handler/logs/", id + ".log");
    }

}
