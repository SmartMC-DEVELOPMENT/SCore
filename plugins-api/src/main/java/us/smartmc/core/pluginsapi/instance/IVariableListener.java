package us.smartmc.core.pluginsapi.instance;

public interface IVariableListener<Player> {

    String parse(String message);
    String parse(Player player, String message);
}
