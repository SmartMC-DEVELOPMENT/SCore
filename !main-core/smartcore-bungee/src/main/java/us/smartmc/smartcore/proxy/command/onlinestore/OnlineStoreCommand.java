package us.smartmc.smartcore.proxy.command.onlinestore;

import me.imsergioh.pluginsapi.util.ChatUtil;
import net.md_5.bungee.api.CommandSender;
import us.smartmc.smartcore.proxy.SmartCoreBungeeCord;
import us.smartmc.smartcore.proxy.instance.CoreCommand;
import us.smartmc.smartcore.proxy.instance.onlinestore.OnlineStoreCommandType;
import us.smartmc.smartcore.proxy.manager.TebexCommandsManager;

public class OnlineStoreCommand extends CoreCommand {

    private static final SmartCoreBungeeCord core = SmartCoreBungeeCord.getPlugin();

    public OnlineStoreCommand() {
        super("onlineStore");
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
            sender.sendMessage(ChatUtil.parse("Reloaded all configs!"));
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
