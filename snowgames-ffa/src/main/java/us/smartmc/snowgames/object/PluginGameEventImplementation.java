package us.smartmc.snowgames.object;

import org.bukkit.entity.Player;

public interface PluginGameEventImplementation {

    void onStart();
    void onEnd();

    void updatePlayer(Player player);

    EventType getType();

}
