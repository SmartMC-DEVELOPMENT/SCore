package us.smartmc.serverhandler;

import us.smartmc.backend.connection.BackendServer;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.serverhandler.backendcommand.IAmAProxyCommand;
import us.smartmc.serverhandler.backendcommand.KeepAliveCommand;
import us.smartmc.serverhandler.backendcommand.RegisterServerCommand;
import us.smartmc.serverhandler.backendcommand.ServerStatusCommand;
import us.smartmc.serverhandler.consolecommand.*;
import us.smartmc.serverhandler.listener.ACommandManagerListener;
import us.smartmc.serverhandler.manager.ConsoleCommandManager;
import us.smartmc.serverhandler.manager.ServerManager;
import us.smartmc.serverhandler.registration.ConfigRegistration;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class OrchestratorMain {

    private static BackendServer backendServer;

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        load();

        registerInputs();

        // CREATE & START BACKEND SERVER
        backendServer = new BackendServer(55777);
        backendServer.start();


        ServerManager.startKeepAliveRemoveTask();

        // ESTA LÍNEA SIEMPRE AL FINAL!!!!
        startReadingConsoleInput();
    }

    public static void load() {
        Registrations.register(ConfigRegistration.class);
        ConnectionInputManager.registerListeners(new ACommandManagerListener());
    }

    private static void registerInputs() {
        ConsoleCommandManager.register(
                new HelpCommand(),
                new TestCommand(),
                new ExitCommand(),
                new CreateCommand(),
                new DeleteCommand(),
                new ExecuteCommand(),
                new RestartCommand(),
                new StopCommand(),
                new ReloadCommand());

        ConnectionInputManager.registerCommands(
                new ServerStatusCommand(),
                new RegisterServerCommand(),
                new IAmAProxyCommand(),
                new KeepAliveCommand());
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
}