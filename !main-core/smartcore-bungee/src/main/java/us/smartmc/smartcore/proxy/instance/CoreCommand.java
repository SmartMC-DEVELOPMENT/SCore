package us.smartmc.smartcore.proxy.instance;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;

public abstract class CoreCommand extends Command implements ICoreCommand {

    private final String name;

    private String permission;
    private String[] aliases;

    public CoreCommand(String name) {
        super(name);
        this.name = name;
    }

    public CoreCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
        this.name = name;
        this.permission = permission;
        this.aliases = aliases;
    }

    public void sendStringMessage(String name, CommandSender sender, String path, Object... args) {
        Language language = Language.getDefault();
        if (sender instanceof ProxiedPlayer player)
            language = PlayerLanguages.getLanguage(player.getUniqueId());

        String message = LanguagesHandler.get(language).get(name).getString(path);
        sender.sendMessage(ChatUtil.parse(message, args));
    }

    public void sendFormattedList(String name, CommandSender sender, String path, Object... args) {
        sender.sendMessage(ChatUtil.parse(getFormattedList(name, sender, path), args));
    }

    private String getFormattedList(String name, CommandSender sender, String path) {
        StringBuilder stringBuilder = new StringBuilder();
        ProxiedPlayer player = null;
        if (sender instanceof ProxiedPlayer) player = (ProxiedPlayer) sender;
        Language language = Language.getDefault();
        if (player != null) language = PlayerLanguages.getLanguage(player.getUniqueId());
        List<String> list = LanguagesHandler.get(language).get(name).getList(path, String.class);

        for (String line : list) {
            if (player != null)
                stringBuilder.append(ChatUtil.parse(player, line) + "\n");
            else stringBuilder.append(ChatUtil.parse(line) + "\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        execute(sender, args);
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public String[] getAliases() {
        if (aliases == null) return new String[0];
        return aliases;
    }

    @Override
    public String getName() {
        return name;
    }
}
