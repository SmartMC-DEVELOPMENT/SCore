package us.smartmc.serverhandler.command;

import me.imsergioh.jbackend.api.ConnectionHandler;
import org.bukkit.Bukkit;
import us.smartmc.serverhandler.executor.BackendCommand;

public class ExecuteServerMessageCommand extends BackendCommand {

  public ExecuteServerMessageCommand() {
    super("execute_msg");
  }

  @Override
  public void execute(ConnectionHandler connectionHandler, String label, String[] args) {
    if (args.length == 0) {
      System.out.println("Usage: /execute_msg <message>");
      return;
    }
    Bukkit.getConsoleSender().sendMessage(String.join(" ", args));
  }
}
