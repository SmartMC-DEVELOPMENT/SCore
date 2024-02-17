package us.smartmc.snowgames;

import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.core.messages.GeneralMessages;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GameMapManager;
import us.smartmc.snowgames.game.FFAMap;

public class FFACommand implements CommandExecutor {

    private void execute(CommandSender sender, String[] args) {

        if (args.length == 0) {
            sender.sendMessage("Available commands: /ffa setSpawn <*map name>, teleport <map name>, ");
            return;
        }

        if (sender instanceof Player player) {
            if (args[0].equals("setSpawn")) {
                try {
                    FFAMap map = (FFAMap) GameMapManager.get(args[1]);
                    map.setSpawn(player.getLocation());
                    player.sendMessage(ChatUtil.parse("&aSpawn set!"));
                } catch (Exception ignore) {
                    player.sendMessage(ChatUtil.parse("&cError ocurred while trying to set spawn. Be sure you are specifing the name of the map correctly"));
                }
            }

            if (args[0].equals("teleport")) {
                FFAMap map;
                if (args.length == 2) {
                    map = (FFAMap) GameMapManager.get(args[1]);
                } else {
                    map = FFAPlugin.getGame().getMap();
                }
                player.teleport(map.getSpawn());
            }

        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            execute(sender, args);
            return true;
        }
        if (!sender.hasPermission("ffa.command.admin")) {
            CorePlayer.get(player).sendLanguageMessage("general", GeneralMessages.COMMAND_NO_PERMISSION_PATH);
            return true;
        }
        execute(sender, args);
        return true;
    }
}
