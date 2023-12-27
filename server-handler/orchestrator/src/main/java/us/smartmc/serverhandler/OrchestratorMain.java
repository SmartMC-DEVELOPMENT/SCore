package us.smartmc.serverhandler;

import me.imsergioh.jbackend.BackendServer;
import me.imsergioh.jbackend.api.ConnectionHandler;
import me.imsergioh.jbackend.api.manager.BackendActionManager;
import us.smartmc.serverhandler.consolecommand.ExitCommand;
import us.smartmc.serverhandler.manager.ConsoleCommandManager;
import us.smartmc.serverhandler.registration.CommandRegistration;
import us.smartmc.serverhandler.registration.CommonListenerRegistration;
import us.smartmc.serverhandler.registration.ConfigRegistration;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class OrchestratorMain {

    private static BackendServer backendServer;
    private static ConnectionHandler handler;

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        Registrations.register(
                CommandRegistration.class,
                CommonListenerRegistration.class,
                ConfigRegistration.class);
        BackendActionManager.registerConnectAction(h -> handler = h);

        // CREATE & START BACKEND SERVER
        backendServer = new BackendServer(55777);
        backendServer.start();

        Runtime.getRuntime().addShutdownHook(new Thread(ExitCommand::execute));

        startReadingConsoleInput();
    }

    public static File getParentFolder() {
        return new File(OrchestratorMain.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
    }

    private static void startReadingConsoleInput() {
        while (true) {
            String input = readConsoleInput();
            ConsoleCommandManager.perform(input);
        }
    }

    public static String readConsoleInput() {
        System.out.print(">> ");
        return scanner.nextLine();
    }

    public static void log(String message) {
        System.out.println(ConsoleColors.BLUE_BOLD + "[LOG]" +
                ConsoleColors.WHITE + " " + message + ConsoleColors.RESET);
    }

    public static BackendServer getBackendServer() {
        return OrchestratorMain.backendServer;
    }

    public static ConnectionHandler getHandler() {
        return OrchestratorMain.handler;
    }
}