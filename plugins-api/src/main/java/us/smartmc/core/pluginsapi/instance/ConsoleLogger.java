package us.smartmc.core.pluginsapi.instance;

public interface ConsoleLogger {

    void log(String message);
    void info(String message);
    void warning(String message);
    void error(String message);

}
