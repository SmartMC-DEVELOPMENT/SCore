package us.smartmc.serverhandler.executor;

public interface IConsoleCommand {

    void execute(String label, String[] args);

    String getName();
    String getDescription();

}
