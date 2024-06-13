package us.smartmc.smartcore.smartcorevelocity.listener;

import com.google.common.collect.Iterables;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.command.PlayerAvailableCommandsEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import us.smartmc.smartcore.smartcorevelocity.manager.AllowedCommandsManager;
import us.smartmc.smartcore.smartcorevelocity.manager.CustomCommandsManager;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class CustomCommandsListeners {

    @Subscribe(order = PostOrder.LAST)
    public void handleCommandAnnounces(PlayerAvailableCommandsEvent event) {
        Player player = event.getPlayer();

        event.getRootNode().getChildren().removeIf((commandNode) -> true);

        Optional<ServerConnection> currentServer = player.getCurrentServer();
        if (currentServer.isEmpty()) return;
        ServerConnection serverConnection = currentServer.get();
        Collection<AllowedCommandsManager> managers = AllowedCommandsManager.get(serverConnection.getServerInfo().getName());
        for (AllowedCommandsManager manager : managers) {
            if (manager == null) return;

            // Force commands to complete
            manager.getAllowedCommands().forEach(cmdName -> {
                if (cmdName.contains(" ")) return;
                LiteralArgumentBuilder<CommandSource> builder = BrigadierCommand.literalArgumentBuilder(cmdName);

                // Autocomplete usernames (TEMPORAL) TODO: REMOVE THIS AND IMPLEMENT BETTER COSAS NO SE
                for (Player connectedPlayer : serverConnection.getServer().getPlayersConnected()) {
                    builder.then(BrigadierCommand.literalArgumentBuilder(connectedPlayer.getUsername()));
                }

                LiteralCommandNode<CommandSource> literalCommandNode = builder.build();

                // Set of reflection
                try {
                    Field field = CommandNode.class.getDeclaredField("children");
                    field.setAccessible(true);
                    Map<String, CommandNode<?>> map = (Map<String, CommandNode<?>>) field.get(event.getRootNode());
                    map.put(cmdName, literalCommandNode);
                    field.set(event.getRootNode(), map);
                    field.setAccessible(false);
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Subscribe(order = PostOrder.LAST)
    public void onChatPlayer(CommandExecuteEvent event) {
        if (!event.getResult().isAllowed()) return;
        String label = event.getCommand();

        for (CustomCommandsManager manager : CustomCommandsManager.getManagers()) {
            boolean executed =
                    manager.execute(event.getCommandSource(), label);
            if (executed) {
                event.setResult(CommandExecuteEvent.CommandResult.denied());
            }
        }
    }
}
