package us.smartmc.smartbot.textcommand;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import us.smartmc.smartbot.instance.TextCommand;
import us.smartmc.smartbot.instance.minecraft.DiscordMinecraftUser;

public class TestCommand extends TextCommand {

    public TestCommand(String name) {
        super(name);
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        Member member = event.getMember();
        if (member == null) return;

        DiscordMinecraftUser minecraftUser = DiscordMinecraftUser.get(member);

        if (!minecraftUser.isLinkedWithMinecraft()) {
            event.getChannel().sendMessage("No estas vinculado hijo mio").complete();
            return;
        }
        minecraftUser.sendMessage("Hola buenas tardes {0}", member.getUser().getName());
    }
}
