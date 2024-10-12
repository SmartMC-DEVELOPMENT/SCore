package us.smartmc.gamescore.manager.map;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.entity.Player;
import us.smartmc.backend.gamescore.BackendConnection;
import us.smartmc.backend.gamescore.CuboidSaveResponse;
import us.smartmc.gamescore.adminplayer.PlayerRegionSelectSession;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.instance.cuboid.CuboidPaster;
import us.smartmc.gamescore.instance.game.map.EditMapSession;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.manager.player.PlayerRegionSelectionsManager;

import java.util.UUID;

public class EditMapSessionsManager extends MapManager<UUID, EditMapSession> {

    public void createSessionByName(Player player, String mapName) {
        EditMapSession session = new EditMapSession(player, mapName);
        put(player.getUniqueId(), session);
    }

    @Override
    public EditMapSession remove(Object key) {
        get(key).leave();
        return super.remove(key);
    }

    public void loadMapAt(Player player, String mapId) {
        BackendConnection.getBackendConnection().ifPresent(connection -> {
            connection.getCuboid(getMapName(mapId)).thenAccept(res -> {
                String message = res.getWrapper() == null ?
                        "&aRegion map has been loaded correctly!"
                        : "&cSomething went wrong!";
                player.sendMessage(ChatUtil.color(message));
                if (res.getWrapper() != null) {
                    player.sendMessage(ChatUtil.color("&aPasting..."));
                    new CuboidPaster(res.getWrapper()).pasteAt(player.getLocation(), () -> {
                        player.sendMessage(ChatUtil.color("&aPasted region! :D"));
                    });
                }
            });
        });
    }

    public void saveMap(Player player) {
        EditMapSession session = of(player);
        if (session == null) return;
        PlayerRegionSelectionsManager regionSelectionsManager = MapManager.getManager(PlayerRegionSelectionsManager.class);
        if (regionSelectionsManager == null) return;
        PlayerRegionSelectSession selectSession = regionSelectionsManager.get(player.getUniqueId());

        if (selectSession.getPos2() == null || selectSession.getPos1() == null) {
            player.sendMessage(ChatUtil.color("&cYou need to set a map selection!"));
            return;
        }
        String mapName = session.getMap().getName();

        // Backend action -> Save selection & validate response
        BackendConnection.getBackendConnection().ifPresent(backendConnection -> {
            backendConnection.sendCuboid(getMapName(mapName), selectSession.buildCuboid()).thenAccept(res -> {
                String message = res.getResponse() == CuboidSaveResponse.RequestResponse.OK ?
                        "&aRegion map has been saved correctly!"
                        : "&cSomething went wrong!";
                player.sendMessage(ChatUtil.color(message));
            });
        });
    }

    public String getMapName(String mapId) {
        return "/" + GamesCoreAPI.getApi().getGameId() + "/" + mapId;
    }

    public EditMapSession of(Player player) {
        return get(player.getUniqueId());
    }

    @Override
    public EditMapSession createValueByKey(UUID id) {
        return null;
    }
}
