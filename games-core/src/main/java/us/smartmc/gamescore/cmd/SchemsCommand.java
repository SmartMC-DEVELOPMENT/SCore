package us.smartmc.gamescore.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.instance.cmd.GamesCoreCommand;
import us.smartmc.gamescore.manager.PlayerRegionSelectionsManager;

public class SchemsCommand extends GamesCoreCommand {

    public SchemsCommand() {
        super("schems");
    }

    @Override
    public void performPlayer(Player player, String label, String[] args) {
       if (args.length == 0) {

       }
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
