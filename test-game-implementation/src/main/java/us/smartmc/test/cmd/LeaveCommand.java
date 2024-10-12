package us.smartmc.test.cmd;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.instance.cmd.GamesCoreCommand;
import us.smartmc.gamescore.instance.game.Game;
import us.smartmc.gamescore.instance.player.GameCorePlayer;

public class LeaveCommand extends GamesCoreCommand {

    public LeaveCommand() {
        super("salir");
    }

    @Override
    public void performPlayer(Player player, String label, String[] args) {
        GameCorePlayer gameCorePlayer = GameCorePlayer.of(player);
        Game game = gameCorePlayer.getCurrentGame();
        if (game == null) {
            player.sendMessage("Not in a game!");
            return;
        }
        game.leavePlayer(gameCorePlayer);
    }

    @Override
    public boolean perform(CommandSender sender, String s, String[] args) {
        return false;
    }
}
