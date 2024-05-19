package us.smartmc.core.itemcommands;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import me.imsergioh.pluginsapi.util.PaperChatUtil;
import net.kyori.adventure.text.Component;
import us.smartmc.core.instance.player.SmartCorePlayer;

public class MessageCommand implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler clickHandler, String label, String[] args) {
        label = label.replaceFirst("message ", "");
        Component message = PaperChatUtil.parse(clickHandler.player(), label);
        clickHandler.clicker().sendMessage(message);
    }
}