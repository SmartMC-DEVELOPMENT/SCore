package us.smartmc.game.luckytowers.instance.player;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.instance.game.GameMap;
import us.smartmc.game.luckytowers.instance.game.GameTeamColor;
import us.smartmc.game.luckytowers.manager.GameMapManager;

public class EditorSession {

    private final Player player;

    private static final GameMapManager mapManager = LuckyTowers.getManager(GameMapManager.class);

    private GameTeamColor color = GameTeamColor.values()[0];

    @Setter @Getter
    private String mapId;

    public EditorSession(Player player) {
        this.player = player;
    }

    public GameTeamColor rotateColor() {
        return color.next();
    }
    public GameMap getMap() {
        return mapManager.get(mapId);
    }

}
