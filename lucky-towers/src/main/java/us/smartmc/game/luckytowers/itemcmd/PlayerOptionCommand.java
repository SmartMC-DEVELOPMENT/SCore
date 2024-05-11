package us.smartmc.game.luckytowers.itemcmd;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import org.bukkit.entity.Player;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.instance.game.GameSession;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;
import us.smartmc.game.luckytowers.manager.GameMapManager;
import us.smartmc.game.luckytowers.manager.GameSessionsManager;
import us.smartmc.game.luckytowers.menu.VoteMenu;

import java.util.UUID;

public class PlayerOptionCommand implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler clickHandler, String label, String[] args) {
        Player player = clickHandler.player();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());

        if (args[0].equals("playMap")) {
            GameSessionsManager sessionsManager = LuckyTowers.getManager(GameSessionsManager.class);
            String name = args[1];
            GameSession session = sessionsManager.createOrGetByName(name);
            session.joinPlayer(gamePlayer);
        }

        if (args[0].equals("vote")) {
            new VoteMenu(player).open(player);
        }
    }
}
