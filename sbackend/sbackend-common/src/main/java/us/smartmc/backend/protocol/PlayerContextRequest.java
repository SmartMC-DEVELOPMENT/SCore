package us.smartmc.backend.protocol;

import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class PlayerContextRequest implements Serializable {

    private final PlayerContextRequestType type;
    private final UUID playerId;
    private final Object[] args;

    public PlayerContextRequest(PlayerContextRequestType type, UUID playerId, Object... args){
        this.type = type;
        this.playerId = playerId;
        this.args = args;
    }

}
