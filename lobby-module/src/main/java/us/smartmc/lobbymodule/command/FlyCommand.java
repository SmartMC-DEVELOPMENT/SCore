package us.smartmc.lobbymodule.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.lobbymodule.handler.FlyManager;
import us.smartmc.smartaddons.plugin.AddonPluginCommand;

public class FlyCommand extends AddonPluginCommand {

    public FlyCommand(String name) {
        super(name);
    }

    @Override
    public void executeAll(CommandSender sender, String[] args) {
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
        sender.sendMessage("Only for players.");
    }

    @Override
    public void executePlayer(Player player, String[] args) {
        // CHECK PERMISSIONS
        if (!player.hasPermission(FlyManager.FLY_PERMISSION)) {
            SmartCorePlayer.get(player).sendLanguageMessage("lobby", "no_store_benefit");
            return;
        }

        // TOGGLE TRUE & FALSE
        SmartCorePlayer corePlayer = SmartCorePlayer.get(player);
        if (!FlyManager.isFlyingEnabled(corePlayer)) {
            FlyManager.toggle(corePlayer, true);
            SmartCorePlayer.get(player).sendLanguageMessage("lobby", "flyEnabled");
        } else {
            FlyManager.toggle(corePlayer, false);
            SmartCorePlayer.get(player).sendLanguageMessage("lobby", "flyDisabled");
        }
    }

    @Override
    public void executeAdminPlayer(Player player, String[] args) {}
}
