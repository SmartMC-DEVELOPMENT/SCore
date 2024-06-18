package us.smartmc.smartcore.smartcorevelocity.event;

import com.velocitypowered.api.proxy.Player;

public class LoggedInProxyPlayerEvent {

    private final Player player;

    public LoggedInProxyPlayerEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
