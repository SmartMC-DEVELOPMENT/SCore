package us.smartmc.bmotd.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import us.smartmc.bmotd.BMotdVelocity;
import us.smartmc.bmotd.manager.MOTDManager;
import us.smartmc.bmotd.util.ChatUtil;

public class bwhitelistCMD implements SimpleCommand {

    private final BMotdVelocity plugin = BMotdVelocity.getPlugin();

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();

        if(sender.hasPermission("*")){
            MOTDManager motdManager = plugin.getMotdManager();
            if(args.length >= 1){
                if(args[0].equalsIgnoreCase("add")){
                    if(args.length >= 2){
                        String name = args[1];
                        motdManager.addWhitelistName(name);
                        sender.sendMessage(ChatUtil.parseToComponent("&aNombre añadido a la lista blanca!"));
                    } else {
                        sender.sendMessage(ChatUtil.parseToComponent("&cUso correcto: /bwhitelist add <nombre>"));
                    }
                }

                if(args[0].equalsIgnoreCase("remove")){
                    if(args.length >= 2){
                        String name = args[1];
                        motdManager.removeWhitelistName(name);
                        sender.sendMessage(ChatUtil.parseToComponent("&cNombre eliminado de la lista blanca!"));

                        // KICK IF IS CONNECTED (TRY TO AVOID ERRORS)
                        try {
                            if(motdManager.isWhitelistActive()) {
                                Player player = plugin.getProxy().getPlayer(name).get();
                                if (player.isActive()) {
                                    String message = plugin.getMotdManager().getConfig().getString("messages.not-whitelist");
                                    player.disconnect(ChatUtil.parseToComponent(message));
                                }
                            }
                        } catch (Exception e){}

                    } else {
                        sender.sendMessage(ChatUtil.parseToComponent("&cUso correcto: /bwhitelist remove <nombre>"));
                    }
                }

                if(args[0].equalsIgnoreCase("list")){
                    sender.sendMessage(ChatUtil.parseToComponent("&aLista blanca: &7"+motdManager.getWhitelistList()));
                }
            }
        } else {
            sender.sendMessage(ChatUtil.parseToComponent("&cPlugin BMotd vPRIVATE created by ImSergioh"));
        }
    }
}
