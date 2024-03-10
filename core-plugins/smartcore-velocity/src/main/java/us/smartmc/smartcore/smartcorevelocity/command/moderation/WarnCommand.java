package us.smartmc.smartcore.smartcorevelocity.command.moderation;

import com.velocitypowered.api.command.CommandSource;
import us.smartmc.smartcore.smartcorevelocity.instance.CoreCommand;
import us.smartmc.smartcore.smartcorevelocity.instance.OfflinePlayerData;
import us.smartmc.smartcore.smartcorevelocity.instance.sanction.SanctionType;
import us.smartmc.smartcore.smartcorevelocity.manager.SanctionsManager;
import us.smartmc.smartcore.smartcorevelocity.messages.SanctionsManagerMessages;
import us.smartmc.smartcore.smartcorevelocity.util.TimeUtils;

import java.util.UUID;

public class WarnCommand extends CoreCommand {

    public WarnCommand() {
        super("warn", "smartmc.command.warn");
    }

    @Override
    public void execute(CommandSource sender, String[] args) {
        if (args.length == 0) {
            sendStringMessage(SanctionsManagerMessages.NAME, sender, "cmd_correct_usage_warn");
            return;
        }

        OfflinePlayerData offlinePlayerData = OfflinePlayerData.get(args[0]);
        UUID uuid = offlinePlayerData.getUUID();

        TimeUtils timeUtils = null;
        StringBuilder reasonBuilder = new StringBuilder();

        boolean reasonDetected = false;

        for (int i = 1; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-r") && i < args.length - 1) {
                reasonDetected = true;
                // Combina los argumentos después de -r hasta el próximo -t o el final de los argumentos
                for (int j = i + 1; j < args.length; j++) {
                    if (args[j].equalsIgnoreCase("-t")) {
                        break;
                    }
                    reasonBuilder.append(" ").append(args[j]);
                    i++;
                }
            }
        }

        if (!reasonDetected) {
            sendStringMessage(SanctionsManagerMessages.NAME, sender, "cmd_correct_usage_warn");
            return;
        }

        if (args.length > 1) {
            StringBuilder label = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                label.append(args[i]).append(" ");
            }
        }

        String reason = reasonBuilder.toString();
        if (reason.startsWith(" ")) reason = reason.replaceFirst(" ", "");
        SanctionsManager.create(uuid, SanctionType.WARN, null, reason);
        sendStringMessage(SanctionsManagerMessages.NAME, sender, "cmd_sanction_success");
    }
}
