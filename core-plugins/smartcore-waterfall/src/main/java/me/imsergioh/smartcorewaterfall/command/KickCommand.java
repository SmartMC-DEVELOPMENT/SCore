package me.imsergioh.smartcorewaterfall.command;

import me.imsergioh.smartcorewaterfall.instance.CoreCommand;
import me.imsergioh.smartcorewaterfall.instance.OfflinePlayerData;
import me.imsergioh.smartcorewaterfall.instance.sanction.SanctionType;
import me.imsergioh.smartcorewaterfall.manager.SanctionsManager;
import me.imsergioh.smartcorewaterfall.messages.SanctionsManagerMessages;
import me.imsergioh.smartcorewaterfall.util.TimeUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class KickCommand extends CoreCommand {

    public KickCommand() {
        super("kick", "smartmc.command.kick");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendStringMessage(SanctionsManagerMessages.NAME, sender, "cmd_correct_usage_kick");
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
            sendStringMessage(SanctionsManagerMessages.NAME, sender, "cmd_correct_usage_kick");
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
        SanctionsManager.create(uuid, SanctionType.KICK, null, reason);
        sendStringMessage(SanctionsManagerMessages.NAME, sender, "cmd_sanction_success");
    }
}
