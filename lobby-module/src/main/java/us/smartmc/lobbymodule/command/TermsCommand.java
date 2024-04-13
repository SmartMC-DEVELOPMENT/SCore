package us.smartmc.lobbymodule.command;

import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.smartmc.lobbymodule.listener.TermsListeners;
import us.smartmc.smartaddons.plugin.AddonPluginCommand;

import java.util.UUID;

public class TermsCommand extends BukkitCommand {

    public TermsCommand() {
        super("terms");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        if (args.length == 0) return true;
        if (args[0].equalsIgnoreCase("accept")) {
            // ACCEPT TERMS
            UUID commandUUID = UUID.fromString(args[1]);
            UUID uuid = TermsListeners.getValidationTerms(player);
            if (commandUUID.equals(uuid)) {
                TermsListeners.acceptTerms(CorePlayer.get(player));
            }
        } else {
            // DECLINE TERMS
            TermsListeners.declineTerms(CorePlayer.get(player));
        }
        return false;
    }
}
