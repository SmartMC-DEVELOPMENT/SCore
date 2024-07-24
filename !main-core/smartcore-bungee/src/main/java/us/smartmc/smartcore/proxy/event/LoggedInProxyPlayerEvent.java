package us.smartmc.smartcore.proxy.event;

import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

@Getter
public class LoggedInProxyPlayerEvent extends Event {

    private final ProxiedPlayer player;

    public LoggedInProxyPlayerEvent(ProxiedPlayer player) {
        this.player = player;
    }

}
