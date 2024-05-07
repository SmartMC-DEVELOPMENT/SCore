package us.smartmc.game.luckytowers.itemcmd;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import org.bukkit.entity.Player;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.instance.game.GameSession;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;
import us.smartmc.game.luckytowers.manager.GameMapManager;
import us.smartmc.game.luckytowers.manager.GameSessionsManager;

import java.util.UUID;

public class PlayerOptionCommand implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler clickHandler, String label, String[] args) {
        Player player = clickHandler.player();

        if (args[0].equals("playMap")) {
            GameMapManager mapManager = LuckyTowers.getManager(GameMapManager.class);
            GameSessionsManager sessionsManager = LuckyTowers.getManager(GameSessionsManager.class);
            String name = args[1];
            UUID id = UUID.randomUUID();
            GameSession session = new GameSession(id, mapManager.get(name));
            sessionsManager.register(id, session);

            session.joinPlayer(GamePlayer.get(player.getUniqueId()));
        }
    }
}
