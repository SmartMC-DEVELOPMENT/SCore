package us.smartmc.smartcore.smartcorevelocity.command.onlinestore;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.kyori.adventure.text.Component;
import us.smartmc.smartcore.smartcorevelocity.SmartCoreVelocity;
import us.smartmc.smartcore.smartcorevelocity.instance.CoreCommand;
import us.smartmc.smartcore.smartcorevelocity.instance.onlinestore.OnlineStoreCommandType;
import us.smartmc.smartcore.smartcorevelocity.manager.TebexCommandsManager;

public class OnlineStoreCommand extends CoreCommand {

    private static final SmartCoreVelocity core = SmartCoreVelocity.getPlugin();

    public OnlineStoreCommand() {
        super("onlineStore");
    }

    @Override
    public void execute(CommandSource sender, String[] args) {
        if (!sender.hasPermission("*")) {
            sender.sendMessage(Component.text(ChatUtil.parse("&bSmartCore created by ImSergioh! :D")));
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            core.getTebexPackageManager().load();
            core.getTebexCommandsManager().load();
            sender.sendMessage(Component.text("Reloaded all configs!"));
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
