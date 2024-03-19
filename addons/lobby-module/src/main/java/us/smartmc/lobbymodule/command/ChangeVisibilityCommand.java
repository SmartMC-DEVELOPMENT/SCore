package us.smartmc.lobbymodule.command;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.core.SmartCore;
import us.smartmc.lobbymodule.handler.VisibilityManager;
import us.smartmc.lobbymodule.instance.PlayerVisibility;
import us.smartmc.smartaddons.plugin.AddonPluginCommand;

import java.util.Arrays;

public class ChangeVisibilityCommand extends AddonPluginCommand {

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
        PlayerVisibility nextVisibility = VisibilityManager.getNext(VisibilityManager.getVisibility(player));
        CorePlayer corePlayer = CorePlayer.get(player);
        VisibilityManager.set(corePlayer, nextVisibility);

        // Update item in hand in case that it's a visibility item (INK_SACK in hand)
        ItemStack item = player.getItemInHand();
        if (!(item.getType().name().startsWith("INK") || item.getType().name().contains("DYE"))) return;
        CoreMenu menu = corePlayer.getCurrentMenuSet();
        item = VisibilityManager.getVisibilityItem(nextVisibility).get(player);
        player.getInventory().setItemInHand(item);
        menu.getActionManager().registerItemAction(player.getInventory().getHeldItemSlot(),
                item, Arrays.asList("cmd changeVisibility"));
    }

    @Override
    public void executeAdminPlayer(Player player, String[] strings) {
    }
}
