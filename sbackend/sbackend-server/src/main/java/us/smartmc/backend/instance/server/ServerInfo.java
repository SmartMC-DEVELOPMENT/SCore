package us.smartmc.backend.instance.server;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ServerInfo {

    private final String name;
    private final int port;

    @Setter
    private int onlineCount, maxPlayers;

    public ServerInfo(String name, int port, int maxPlayers) {
        this.name = name;
        this.port = port;
        this.maxPlayers = maxPlayers;
    }

}
