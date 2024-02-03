package us.smartmc.smartbot.util;


import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import us.smartmc.smartbot.SmartBotMain;

public class ConfigUtil {

    private static final Role DEFAULT_USER_ROLE = SmartBotMain.getMainGuild().getRoleById("1109545274285772991");
    private static final Role DEFAULT_LANGUAGE_ROLE = SmartBotMain.getMainGuild().getRoleById("1179802284222324738");

    public static void setDefaultUserRole(Member member) {
        if (DEFAULT_USER_ROLE != null) {
            member.getGuild().addRoleToMember(member, DEFAULT_USER_ROLE).queue();
        }

        if (DEFAULT_LANGUAGE_ROLE != null) {
            member.getGuild().addRoleToMember(member, DEFAULT_LANGUAGE_ROLE).queue();
        }
    }

}
