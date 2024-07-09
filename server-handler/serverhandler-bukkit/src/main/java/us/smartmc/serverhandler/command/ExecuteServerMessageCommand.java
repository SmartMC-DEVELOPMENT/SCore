package us.smartmc.serverhandler.command;

import org.bukkit.Bukkit;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;

public class ExecuteServerMessageCommand extends BackendCommandExecutor {

  public ExecuteServerMessageCommand() {
    super("execute_msg");
  }

  @Override
  public void onCommand(ConnectionHandler connectionHandler, String label, String[] args) {
    if (args.length == 0) {
      System.out.println("Usage: /execute_msg <message>");
      return;
    }
    Bukkit.getConsoleSender().sendMessage(String.join(" ", args));
  }



}
