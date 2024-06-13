package us.smartmc.game.luckytowers.itemcmd;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import org.bukkit.entity.Player;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.instance.game.GameSession;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;
import us.smartmc.game.luckytowers.listener.PlayerLogicListeners;
import us.smartmc.game.luckytowers.manager.GameSessionsManager;
import us.smartmc.game.luckytowers.menu.GamesMenu;

import java.util.List;

public class LobbyHotbarCommand implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler clickHandler, String label, String[] args) {
        Player player = clickHandler.player();

        if (args[0].equalsIgnoreCase("play")) {
            new GamesMenu(player).open(player);
        }

        if (args[0].equals("playSwift")) {
            GameSessionsManager sessionsManager = LuckyTowers.getManager(GameSessionsManager.class);
            List<GameSession> prioSessions = sessionsManager.getPrioSessions();

            if (prioSessions.isEmpty()) {
                PlayerLogicListeners.pickRandomMapToJoin(player);
                return;
            }

            GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
            prioSessions.get(0).joinPlayer(gamePlayer);
        }

    }
}
