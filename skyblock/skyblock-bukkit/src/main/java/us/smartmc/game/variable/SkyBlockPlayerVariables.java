package us.smartmc.game.variable;

import me.imsergioh.pluginsapi.instance.VariableListener;
import org.bukkit.entity.Player;
import us.smartmc.game.instance.SkyBlockPlayer;

public class SkyBlockPlayerVariables extends VariableListener<Player> {


    @Override
    public String parse(String s) {
        return s;
    }

    @Override
    public String parse(Player player, String s) {
        if (s.contains("<sbcoins>")) {
            long coins = 0;

            SkyBlockPlayer skyBlockPlayer = SkyBlockPlayer.get(player);
            if (skyBlockPlayer != null) {
                coins = skyBlockPlayer.getPlayerData().getCoins();
            }
            s = s.replace("<sbcoins>", String.valueOf(coins));
        }

        return s;
    }
}
