package us.smartmc.core.commands;

import lombok.Getter;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.core.instance.player.IPlayerCurrencyCoin;
import us.smartmc.core.instance.player.PlayerCurrenciesHandler;
import us.smartmc.core.instance.player.PlayerCurrencyCoin;
import us.smartmc.core.instance.player.SmartCorePlayer;

public abstract class CurrencyCommand implements CommandExecutor {

    @Getter
    private final String name;
    private final IPlayerCurrencyCoin coin;

    public CurrencyCommand(String name, IPlayerCurrencyCoin coin) {
        this.name = name;
        this.coin = coin;
    }

    private void executeAdmin(CommandSender sender, String[] args) {
        if (args.length >= 2) {
            SmartCorePlayer smartCorePlayer = SmartCorePlayer.get(args[0]);
            if (smartCorePlayer == null) {
                sender.sendMessage(ChatUtil.parse("&cNo target/player found with this name!"));
                return;
            }
            PlayerCurrenciesHandler handler = smartCorePlayer.getCurrenciesHandler();
            switch (args[1].toLowerCase()) {
                case "reset": {
                    handler.set(coin, 0);
                    sender.sendMessage(ChatUtil.parse("&aBalance reset!"));
                    return;
                }
                case "add": {
                    if (args.length == 2) {
                        sender.sendMessage(ChatUtil.parse("&cCorrect usage: /" + name + " <user> add <amount>"));
                        return;
                    }
                    try {
                        long amount = Long.parseLong(args[2]);
                        handler.add(coin, amount, null);
                        sender.sendMessage(ChatUtil.parse("&aBalance added!"));
                        return;
                    } catch (Exception e) {
                        sender.sendMessage(ChatUtil.parse("&cError ocurred while trying to execute this command!"));
                    }
                    return;
                }
                case "remove": {
                    if (args.length == 2) {
                        sender.sendMessage(ChatUtil.parse("&cCorrect usage: /" + name + " <user> remove <amount>"));
                        return;
                    }
                    try {
                        long amount = Long.parseLong(args[2]);
                        handler.remove(coin, amount, null);
                        sender.sendMessage(ChatUtil.parse("&aBalance removed!"));
                        return;
                    } catch (Exception e) {
                        sender.sendMessage(ChatUtil.parse("&cError ocurred while trying to execute this command!"));
                    }
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            SmartCorePlayer corePlayer = SmartCorePlayer.get(player);
            if (!sender.hasPermission("*")) {
                corePlayer.sendLanguageMessage("general", "noPermission");
                return true;
            }
        }
        if (!sender.hasPermission("*")) return true;
        executeAdmin(sender, args);
        return true;
    }
}
