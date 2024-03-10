package me.imsergioh.smartcorewaterfall.command.onlinestore;

import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.smartcorewaterfall.SmartCoreWaterfall;
import me.imsergioh.smartcorewaterfall.instance.onlinestore.OnlineStoreCommandType;
import me.imsergioh.smartcorewaterfall.manager.TebexCommandsManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class OnlineStoreCommand extends Command {

    private static final SmartCoreWaterfall core = SmartCoreWaterfall.getPlugin();

    public OnlineStoreCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("*")) {
            sender.sendMessage(ChatUtil.parse("&bSmartCore created by ImSergioh! :D"));
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            core.getTebexPackageManager().load();
            core.getTebexCommandsManager().load();
            sender.sendMessage("Reloaded all configs!");
            return;
        }

        OnlineStoreCommandType type = null;
        switch (args[0]) {
            case "purchasePackage":
                type = OnlineStoreCommandType.PURCHASE;
                break;
            case "removePackage":
                type = OnlineStoreCommandType.REMOVE;
                break;
            case "refundPackage":
                type = OnlineStoreCommandType.REFUND;
                break;
            case "chargebackPackage":
                type = OnlineStoreCommandType.CHARGEBACK;
                break;
            case "renewSub":
                type = OnlineStoreCommandType.RENEW;
                break;
            default:
                type = OnlineStoreCommandType.UNKNOWN;
                break;
        };

        if (type.equals(OnlineStoreCommandType.UNKNOWN)) {
            System.out.println("OnlineStore unkown command type to execute! (" + args[0] + ")");
            return;
        }

        TebexCommandsManager cmdManager = core.getTebexCommandsManager();
        String username = args[1];
        String packageId = args[2];
        cmdManager.performPackageCommands(packageId, type, username, packageId);
    }
}
