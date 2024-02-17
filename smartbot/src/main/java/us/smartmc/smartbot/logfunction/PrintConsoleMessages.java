package us.smartmc.smartbot.logfunction;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import us.smartmc.smartbot.instance.log.LogChannelRegistry;
import us.smartmc.smartbot.util.TextUtils;

public class PrintConsoleMessages extends LogChannelRegistry {

    public PrintConsoleMessages() {
        super("1208172253108371486", "discord-logs:simple");
        run();
    }

    @Override
    public void onLogRegistryReceive(GuildChannel channel, String message) {
        if (channel instanceof TextChannel textChannel) {
            textChannel.sendMessage("[" + TextUtils.getFormattedLogDate() + "] " + message).complete();
        }
    }
}
