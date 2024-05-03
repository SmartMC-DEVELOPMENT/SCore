package us.smartmc.core.randomwar.command;

import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.smartmc.core.randomwar.RandomWar;
import us.smartmc.core.randomwar.messages.GameMessages;

public class LeaveCommand implements CommandExecutor {

    private static final RandomWar plugin = RandomWar.getPlugin();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        return false;
    }
}
