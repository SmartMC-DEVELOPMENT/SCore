package us.smartmc.serverhandler.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    public static void startServer(File dir, boolean log) {
        new Thread(() -> {
            Process process = null;
            try {
                // Ejemplo de comando directo sin usar screen
                String command = "screen -Sdm " + dir.getName() + " sh start.sh";
                process = Runtime.getRuntime().exec(command, null, dir);
                String logId = dir.getName() + "-" + UUID.randomUUID().toString();
                // Leer y mostrar la salida estándar del proceso
                if (log) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        registerLog(logId, line);
                    }
                }
                // Esperar a que el proceso termine
                process.waitFor();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (process != null) {
                    process.destroy();
                }
            }
        }).start();
    }

    private static void registerLog(String logId, String line) {
        try {
            FileWriter writer = logOuts.containsKey(logId) ? logOuts.get(logId) : new FileWriter(getLogFile(logId).getAbsolutePath());
            if (!logOuts.containsKey(logId))
                logOuts.put(logId, writer);
            writer.write(line + "\n");
            writer.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static File getLogFile(String id) {
        return new File("/home/network/server-handler/logs/", id + ".log");
    }

}
