package us.smartmc.smartcore.smartcorevelocity.command.moderation;

import com.velocitypowered.api.command.CommandSource;
import us.smartmc.smartcore.smartcorevelocity.instance.CoreCommand;
import us.smartmc.smartcore.smartcorevelocity.instance.OfflinePlayerData;
import us.smartmc.smartcore.smartcorevelocity.instance.sanction.SanctionType;
import us.smartmc.smartcore.smartcorevelocity.manager.SanctionsManager;
import us.smartmc.smartcore.smartcorevelocity.messages.SanctionsManagerMessages;
import us.smartmc.smartcore.smartcorevelocity.util.SanctionTimeUtils;
import us.smartmc.smartcore.smartcorevelocity.util.TimeUtils;

import java.util.UUID;

public class MuteCommand extends CoreCommand {

    public MuteCommand() {
        super("mute", "smartmc.command.ban");
    }

    @Override
    public void execute(CommandSource sender, String[] args) {
        if (args.length == 0 || args.length < 2) {
            sendStringMessage(SanctionsManagerMessages.NAME, sender, "cmd_correct_usage_mute");
            return;
        }

        OfflinePlayerData offlinePlayerData = OfflinePlayerData.get(args[0]);
        UUID uuid = offlinePlayerData.getUUID();

        TimeUtils timeUtils = null;
        StringBuilder reasonBuilder = new StringBuilder();
        boolean isPermanent = false; // Flag para controlar si el baneo es permanente

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
            } else if (args[i].equalsIgnoreCase("-t") && i < args.length - 1) {
                // Combina los argumentos restantes para formar el tiempo como una sola cadena
                StringBuilder timeBuilder = new StringBuilder();
                for (int j = i + 1; j < args.length; j++) {
                    timeBuilder.append(args[j]).append(" ");
                }
                String time = timeBuilder.toString().trim();
                timeUtils = new SanctionTimeUtils(time).getTimeUtils();
                break; // Sal del bucle una vez que hayas capturado el tiempo
            } else if (args[i].equalsIgnoreCase("-p")) {
                isPermanent = true; // Si se especifica -p, el baneo es permanente
            }
        }

        if (!reasonDetected) {
            sendStringMessage(SanctionsManagerMessages.NAME, sender, "cmd_correct_usage_ban");
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

        if (isPermanent) {
            timeUtils = new SanctionTimeUtils("0d").getTimeUtils(); // Baneo permanente
        }

        SanctionsManager.create(uuid, SanctionType.MUTE, timeUtils, reason);
        sendStringMessage(SanctionsManagerMessages.NAME, sender, "cmd_sanction_success");
    }
}
