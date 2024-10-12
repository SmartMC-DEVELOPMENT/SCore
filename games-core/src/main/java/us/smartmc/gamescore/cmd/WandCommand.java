package us.smartmc.gamescore.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.instance.cmd.GamesCoreCommand;
import us.smartmc.gamescore.manager.player.PlayerSelectionsManager;

public class WandCommand extends GamesCoreCommand {

    public WandCommand() {
        super("/wand");
    }

    @Override
    public void performPlayer(Player player, String label, String[] args) {
       player.getInventory().addItem(PlayerSelectionsManager.wandItem);
    }

    @Override
    public boolean perform(CommandSender sender, String label, String[] args) {
        return false;
    }

    @Override
    public String getPermission() {
        return "*";
    }
}
