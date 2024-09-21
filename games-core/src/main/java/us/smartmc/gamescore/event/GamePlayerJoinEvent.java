package us.smartmc.gamescore.event;

import org.bukkit.entity.Player;
import us.smartmc.gamescore.instance.event.GameCorePlayerEvent;
import us.smartmc.gamescore.instance.player.GameCorePlayer;

public class GamePlayerJoinEvent extends GameCorePlayerEvent {

    public GamePlayerJoinEvent(Player player, GameCorePlayer corePlayer) {
        super(player, corePlayer);
    }
}
