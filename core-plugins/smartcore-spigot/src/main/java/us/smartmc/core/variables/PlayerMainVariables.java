package us.smartmc.core.variables;

import me.imsergioh.pluginsapi.instance.VariableListener;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import us.smartmc.core.instance.player.PlayerCurrenciesHandler;
import us.smartmc.core.instance.player.PlayerCurrencyCoin;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.core.util.VariableUtil;

import java.util.Optional;

public class PlayerMainVariables extends VariableListener<Player> {

    @Override
    public String parse(String message) {
        return message;
    }

    @Override
    public String parse(Player player, String message) {
        if (message == null) return null;
        message = VariableUtil.replace(message, "<ping>", s -> String.valueOf(((CraftPlayer) player).getHandle().ping));
        message = VariableUtil.replace(message, "<name>", s -> player.getName());
        message = VariableUtil.replace(message, "<coins>", s -> getCurrency(player, PlayerCurrencyCoin.SMARTCOINS));
        message = VariableUtil.replace(message, "<enigmaboxes>", s -> getCurrency(player, PlayerCurrencyCoin.ENIGMA_BOXES));
        message = VariableUtil.replace(message, "<gems>", s -> getCurrency(player, PlayerCurrencyCoin.GEMS));
        return message;
    }

    public static String getCurrency(Player player, PlayerCurrencyCoin coin) {
        SmartCorePlayer corePlayer = SmartCorePlayer.get(player);
        PlayerCurrenciesHandler handler = corePlayer.getCurrenciesHandler();
        return String.valueOf(handler.get(coin));
    }

}
