package us.smartmc.backend.handler;

import lombok.Getter;

@Getter
public class ModulesHandler {

    public static ServersInfoHandler serversInfoHandler;
    public static PlayersInfoHandler playersInfoHandler;

    public static void setup() {
        serversInfoHandler = new ServersInfoHandler();
        playersInfoHandler = new PlayersInfoHandler();
    }
}
