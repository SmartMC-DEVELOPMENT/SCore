package us.smartmc.serverhandler.util;

import us.smartmc.serverhandler.ConsoleColors;
import us.smartmc.serverhandler.OrchestratorMain;

public class CommandUtilities {

  public static void sendError(String error, Object... args) {
    OrchestratorMain.log(ConsoleColors.RED_BRIGHT + "[✖] (Error) " + String.format(error, args) + ConsoleColors.RESET);
  }

  public static void sendFeedback(String message, Object... args) {
    OrchestratorMain.log(ConsoleColors.GREEN_BRIGHT + "[✔]: " + String.format(message, args) + ConsoleColors.RESET);
  }
}
