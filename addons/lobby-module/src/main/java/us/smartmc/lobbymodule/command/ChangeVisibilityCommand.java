package us.smartmc.lobbymodule.command;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.lobbymodule.handler.VisibilityManager;
import us.smartmc.lobbymodule.instance.PlayerVisibility;
import us.smartmc.smartaddons.plugin.AddonPluginCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        if (item == null) return;
        if (!item.getType().equals(Material.INK_SACK)) return;
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
