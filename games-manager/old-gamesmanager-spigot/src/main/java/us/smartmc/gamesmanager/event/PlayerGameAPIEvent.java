package us.smartmc.gamesmanager.event;

import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class PlayerGameAPIEvent extends GameAPIEvent {

    protected final Player player;

    public PlayerGameAPIEvent(Player player) {
        this.player = player;
    }
}
