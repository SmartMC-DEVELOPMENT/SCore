package us.smartmc.smartbot.listener;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import us.smartmc.smartbot.SmartBotMain;
import us.smartmc.smartbot.util.ConfigUtil;


public class DefaultUserRoleListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        if (!SmartBotMain.isAllowedGuild(event.getGuild())) return;


        Member member = event.getMember();

        if (guild.equals(SmartBotMain.getMainGuild())) {
            ConfigUtil.setDefaultUserRole(member);
        }
        System.out.println("Joined member! " + event.getUser().getName());
    }
}
