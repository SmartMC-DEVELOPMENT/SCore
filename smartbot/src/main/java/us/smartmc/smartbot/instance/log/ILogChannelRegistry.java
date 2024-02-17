package us.smartmc.smartbot.instance.log;

import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;

public interface ILogChannelRegistry {

    void onLogRegistryReceive(GuildChannel channel, String message);

}
