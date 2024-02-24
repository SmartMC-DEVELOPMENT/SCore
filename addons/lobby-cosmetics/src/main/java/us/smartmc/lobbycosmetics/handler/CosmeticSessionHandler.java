package us.smartmc.lobbycosmetics.handler;

import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import us.smartmc.lobbycosmetics.instance.player.CosmeticPlayerSession;

import java.util.UUID;

public class CosmeticSessionHandler extends AddonHandler<UUID, CosmeticPlayerSession> {

    @Override
    public CosmeticPlayerSession getDefaultValue(UUID id) {
        CosmeticPlayerSession session = new CosmeticPlayerSession(CorePlayer.get(id));
        session.load();
        return session;
    }
}
