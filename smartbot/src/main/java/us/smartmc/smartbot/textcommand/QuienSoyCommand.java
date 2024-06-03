package us.smartmc.smartbot.textcommand;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.smartmc.smartbot.instance.TextCommand;
import us.smartmc.smartbot.instance.minecraft.DiscordMinecraftUser;
import us.smartmc.smartbot.instance.minecraft.MinecraftCorePlayerData;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

public class QuienSoyCommand extends TextCommand {

    public QuienSoyCommand(String name) {
        super(name);
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        Member member = event.getMember();
        if (member == null) return;

        DiscordMinecraftUser minecraftUser = DiscordMinecraftUser.get(member);

        if (!minecraftUser.isLinkedWithMinecraft()) {
            event.getChannel().sendMessage("No estas vinculado con Minecraft.").complete();
            return;
        }

        UUID id = minecraftUser.getMinecraftId();
        MinecraftCorePlayerData data = new MinecraftCorePlayerData(id);

        StringBuilder message = new StringBuilder();

        if (data.hasFlyEnabled()) {
            message.append("Hemos detectado que tienes el fly activado.\n");
        }

        message.append("Tu lenguaje es: " + data.getLanguage() + "\n");

        message.append("Tienes: " + data.getSmartCoins() + " SmartCoins\n");

        message.append("Tu primer login en el server: " + formatTimestamp(data.getFirstLogin()) + " \n");
        message.append("Tu último login en el server: " + formatTimestamp(data.getLastLogin()) + " \n");

        event.getMessage().reply(message.toString()).complete();
    }

    public static String formatTimestamp(long timestamp) {
        // Convertir el timestamp a LocalDateTime
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());

        // Crear un DateTimeFormatter con el patrón deseado y configurado para español
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy 'a las' HH:mm:ss", new Locale("es", "ES"));

        // Formatear la fecha y hora
        String formattedDate = dateTime.format(formatter);

        return formattedDate;
    }

}
