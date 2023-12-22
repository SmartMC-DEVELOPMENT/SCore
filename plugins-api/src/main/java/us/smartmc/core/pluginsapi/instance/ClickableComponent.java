package us.smartmc.core.pluginsapi.instance;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;
import us.smartmc.core.pluginsapi.util.ChatUtil;

public class ClickableComponent {

    private final ComponentBuilder builder = new ComponentBuilder("");

    public ClickableComponent() {
    }

    public void addURL(String text, String url) {
        builder
                .append(ChatUtil.parse(text), ComponentBuilder.FormatRetention.FORMATTING)
                .event(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
    }

    public void addCommandSuggestion(String text, String command) {
        builder
                .append(ChatUtil.parse(text), ComponentBuilder.FormatRetention.FORMATTING)
                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
    }

    public void addRunCommand(String text, String command) {
        builder
                .append(ChatUtil.parse(text), ComponentBuilder.FormatRetention.FORMATTING)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
    }

    public void addText(String text) {
        builder.append(ChatUtil.parse(text), ComponentBuilder.FormatRetention.NONE);
    }

    public void send(Player player) {
        player.spigot().sendMessage(builder.create());
    }

    public BaseComponent getBuilder() {
        return builder.create()[0];
    }
}
