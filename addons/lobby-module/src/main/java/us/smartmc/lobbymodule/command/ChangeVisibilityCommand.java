package us.smartmc.lobbymodule.command;

import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.lobbymodule.handler.VisibilityManager;
import us.smartmc.lobbymodule.instance.PlayerVisibility;
import us.smartmc.smartaddons.plugin.AddonPluginCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChangeVisibilityCommand extends AddonPluginCommand {

    private static final List<PlayerVisibility> list = new ArrayList<>(Arrays.asList(PlayerVisibility.values()));

    public ChangeVisibilityCommand() {
        super("changeVisibility");
    }

    @Override
    public void executeAll(CommandSender commandSender, String[] strings) {
    }

    @Override
    public void executeConsole(CommandSender commandSender, String[] strings) {
    }

    @Override
    public void executePlayer(Player player, String[] args) {
        String visibilityName = CorePlayer.get(player).getPlayerData().getDocument()
                .getString("visibility");
        if (visibilityName == null) visibilityName = PlayerVisibility.DEFAULT.toString();
        PlayerVisibility visibility = PlayerVisibility.valueOf(visibilityName);

        PlayerVisibility nextVisibility = getNext(visibility);
        CorePlayer corePlayer = CorePlayer.get(player);
        corePlayer.getPlayerData().getDocument().put("visibility", nextVisibility.name());
        VisibilityManager.update(player);
        player.playSound(player.getLocation(), Sound.CLICK, 0.1F, 2.5F);
    }

    @Override
    public void executeAdminPlayer(Player player, String[] strings) {
    }

    public PlayerVisibility getNext(PlayerVisibility of) {
        int index = 0;
        for (PlayerVisibility visibility : PlayerVisibility.values()) {
            if (visibility.equals(of)) break;
            index++;
        }
        index++;
        if (index >= list.size()) index = 0;
        return list.get(index);
    }
}
