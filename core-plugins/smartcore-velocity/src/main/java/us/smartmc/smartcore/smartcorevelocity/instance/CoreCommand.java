package us.smartmc.smartcore.smartcorevelocity.instance;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.VelocityChatUtil;
import net.kyori.adventure.text.Component;

import java.util.Arrays;
import java.util.List;

public abstract class CoreCommand implements SimpleCommand, ICoreCommand {

    private final String name;

    private String permission;
    private List<String> aliases;

    public CoreCommand(String name) {
        this.name = name;
    }

    public CoreCommand(String name, String permission, String... aliases) {
        this.name = name;
        this.permission = permission;
        this.aliases = Arrays.asList(aliases);
    }

    public void sendStringMessage(String name, CommandSource sender, String path, Object... args) {
        Language language = Language.getDefault();
        if (sender instanceof Player)
            language = PlayerLanguages.getLanguage(((Player) sender).getUniqueId());

        String message = LanguagesHandler.get(language).get(name).getString(path);
        sender.sendMessage(Component.text(VelocityChatUtil.parse(message, args)));
    }

    public void sendFormattedList(String name, CommandSource sender, String path, Object... args) {
        sender.sendMessage(Component.text(VelocityChatUtil.parse(getFormattedList(name, sender, path), args)));
    }

    private String getFormattedList(String name, CommandSource sender, String path) {
        StringBuilder stringBuilder = new StringBuilder();
        Player player = null;
        if (sender instanceof Player) player = (Player) sender;
        Language language = Language.getDefault();
        if (player != null) language = PlayerLanguages.getLanguage(player.getUniqueId());
        List<String> list = LanguagesHandler.get(language).get(name).getList(path, String.class);

        for (String line : list) {
            if (player != null)
                stringBuilder.append(VelocityChatUtil.parse(player, line) + "\n");
            else stringBuilder.append(VelocityChatUtil.parse(line) + "\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        execute(source, args);
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public String getName() {
        return name;
    }
}
