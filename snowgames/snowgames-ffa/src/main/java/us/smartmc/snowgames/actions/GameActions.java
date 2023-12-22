package us.smartmc.snowgames.actions;

import org.bukkit.entity.Player;
import us.smartmc.core.pluginsapi.spigot.instance.ItemActionExecutor;
import us.smartmc.core.pluginsapi.spigot.item.ClickHandler;
import us.smartmc.snowgames.util.GameItemUtils;

public class GameActions implements ItemActionExecutor {


    @Override
    public void execute(ClickHandler handler, String s, String[] args) {
        Player player = handler.getPlayer();
        String arg = args[0].toLowerCase();
        System.out.println("Executing " + s);

        if (arg.equals("weapon")) {
            GameItemUtils.handleWeaponAction(player);
        }

        if (arg.equals("blocks")) {
            GameItemUtils.handleWeaponAction(player);
        }

        if (arg.equals("speed")) {
            GameItemUtils.handleVelocityAction(player);
        }
    }
}
