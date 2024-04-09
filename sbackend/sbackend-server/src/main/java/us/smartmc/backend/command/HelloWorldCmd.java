package us.smartmc.backend.command;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;

public class HelloWorldCmd extends BackendCommandExecutor {

    public HelloWorldCmd() {
        super("helloWorld");
    }

    @Override
    public void onCommand(ConnectionHandler connection, String label, String[] args) {
        System.out.println("Hola mundo desde conexión de " + connection);
        connection.sendMessage("Buenassss aqui la consola server les go");
    }
}
