package us.smartmc.core.luckywars.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import us.smartmc.core.luckywars.LuckyTowers;

public class LeaveCommand implements CommandExecutor {

    private static final LuckyTowers plugin = LuckyTowers.getPlugin();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        return false;
    }
}
