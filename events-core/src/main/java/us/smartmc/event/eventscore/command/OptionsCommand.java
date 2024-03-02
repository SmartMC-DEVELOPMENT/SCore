package us.smartmc.event.eventscore.command;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.event.eventscore.handler.MainEventHandler;
import us.smartmc.event.eventscore.menu.MainOptionsMenu;

public class OptionsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatUtil.parse("&cCommand only for players"));
            return true;
        }
        Player player = (Player) sender;
        if (!MainEventHandler.isHoster(player)) {
            player.sendMessage(ChatUtil.parse("&cDebes ser admin/hoster del evento para poder hacer eso."));
        }
        MainOptionsMenu.getMenu().open(player);
        return false;
    }
}
