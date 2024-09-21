package us.smartmc.gamescore.event;

import org.bukkit.entity.Player;
import us.smartmc.gamescore.instance.event.GameCorePlayerEvent;
import us.smartmc.gamescore.instance.player.GameCorePlayer;

public class GamePlayerQuitEvent extends GameCorePlayerEvent {

    public GamePlayerQuitEvent(Player player, GameCorePlayer corePlayer) {
        super(player, corePlayer);
    }
}
