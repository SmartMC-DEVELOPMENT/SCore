package us.smartmc.gamesmanager.editor;

import lombok.Getter;
import org.bukkit.entity.Player;
import us.smartmc.gamesmanager.game.map.GameMap;
import us.smartmc.gamesmanager.game.teams.TeamSelector;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class EditSession {

    private static final HashMap<UUID, EditSession> sessions = new HashMap<>();

    private final GameMap map;
    private final TeamSelector selector;

    public EditSession(Player player, GameMap map) {
        sessions.put(player.getUniqueId(), this);
        this.map = map;
        selector = new TeamSelector();
    }


    public static EditSession get(Player player) {
        return sessions.get(player.getUniqueId());
    }

}
