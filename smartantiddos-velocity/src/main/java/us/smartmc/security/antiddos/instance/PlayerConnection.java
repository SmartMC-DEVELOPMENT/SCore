package us.smartmc.security.antiddos.instance;

import com.velocitypowered.api.proxy.Player;
import lombok.Getter;
import us.smartmc.security.antiddos.handler.ConnectionHandler;
import us.smartmc.security.antiddos.util.EncriptionUtil;

import java.util.ArrayList;
import java.util.List;

public class PlayerConnection {

    @Getter
    private final Player player;
    @Getter
    private String connectionName;

    private final List<String> enteredServers = new ArrayList<>();

    public PlayerConnection(Player player){
        this.player = player;
        try {
            this.connectionName = EncriptionUtil.hash(player.getRemoteAddress().getAddress().toString());
        } catch (Exception e){}


    }

    private void checkConnectionValidation(){
        if(enteredServers.size() >= 2) {
            ConnectionHandler.allowConnection(player.getRemoteAddress().getAddress().toString());
        }
    }

    public void registerEnteredServer(String serverName){
        if (enteredServers.contains(serverName)) {
            return;
        }

        enteredServers.add(serverName);
        checkConnectionValidation();
    }

}
