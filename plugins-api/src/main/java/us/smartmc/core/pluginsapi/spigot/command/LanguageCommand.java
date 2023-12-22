package us.smartmc.core.pluginsapi.spigot.command;

import us.smartmc.core.pluginsapi.language.Language;
import us.smartmc.core.pluginsapi.menu.SetLanguageMenu;
import us.smartmc.core.pluginsapi.spigot.player.CorePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LanguageCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = ((Player) sender).getPlayer();
        if (args.length >= 1) {
            try {
                Language language = Language.valueOf(args[0].toUpperCase());
                CorePlayer.get(((Player) sender)).setLanguage(language);
                return true;
            } catch (Exception ignore) {}
        }
        new SetLanguageMenu(player).open(player);
        return false;
    }
}

