package us.smartmc.snowgames;

import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.util.PaperChatUtil;
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

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args[0].equalsIgnoreCase("setSpawn")) {
                try {
                    FFAMap map = (FFAMap) GameMapManager.get(args[1]);
                    map.setSpawn(player.getLocation());
                    player.sendMessage(PaperChatUtil.parse("&aSpawn set!"));
                } catch (Exception ignore) {
                    player.sendMessage(PaperChatUtil.parse("&cError ocurred while trying to set spawn. Be sure you are specifing the name of the map correctly"));
                }
            }

            if (args[0].equalsIgnoreCase("teleport")) {
                FFAMap map;
                if (args.length == 2) {
                    map = (FFAMap) GameMapManager.get(args[1]);
                } else {
                    map = FFAPlugin.getGame().getMap();
                }
                player.teleport(map.getSpawn());
            }

            if (args[0].equalsIgnoreCase("setSpawnY")) {
                try {
                    FFAMap map = (FFAMap) GameMapManager.get(args[1]);
                    map.setSpawnYLocation((int) player.getLocation().getY());
                    player.sendMessage(PaperChatUtil.parse("&aSpawn Y set!"));
                } catch (Exception ignore) {
                    player.sendMessage(PaperChatUtil.parse("&cError ocurred while trying to set spawn. Be sure you are specifing the name of the map correctly"));
                }
            }

        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            execute(sender, args);
            return true;
        }
        if (!sender.hasPermission("ffa.command.admin")) {
            Player player = (Player) sender;
            CorePlayer.get(player).sendLanguageMessage("general", GeneralMessages.COMMAND_NO_PERMISSION_PATH);
            return true;
        }
        execute(sender, args);
        return true;
    }
}
