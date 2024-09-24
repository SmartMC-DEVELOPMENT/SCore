package us.smartmc.gamescore.instance.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.manager.CoreCommandsManager;

public abstract class GamesCoreCommand extends BukkitCommand implements IGamesCoreCommand {

    protected GamesCoreCommand(String name) {
        super(name);
        getManager().put(name, this);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (perform(sender, label, args)) return true;

        if (getPermission() != null && !sender.hasPermission(getPermission())) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        if (sender instanceof Player player) {
            performPlayer(player, label, args);
        } else {
            performConsole(sender, label, args);
        }
        return true;
    }

    private static CoreCommandsManager getManager() {
        return CoreCommandsManager.getManager(CoreCommandsManager.class);
    }

}
