package us.smartmc.npcsmodule.npccommand;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.entity.Player;
import us.smartmc.npcsmodule.event.NPCUseEntityEvent;
import us.smartmc.npcsmodule.instance.NPCCommandExecutor;

public class MessageCommand implements NPCCommandExecutor {

    @Override
    public void onCommand(String name, String[] args, NPCUseEntityEvent event) {
        if (args.length == 0) return;
        StringBuilder builder = new StringBuilder();
        for (String word : args) {
            builder.append(word).append(" ");
        }
        builder.deleteCharAt(builder.length() - 1);

        Player player = event.getPlayer();
        String message = builder.toString();
        player.sendMessage(ChatUtil.parse(player, message));
    }
}
