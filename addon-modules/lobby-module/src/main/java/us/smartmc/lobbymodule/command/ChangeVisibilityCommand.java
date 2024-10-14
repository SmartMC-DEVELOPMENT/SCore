package us.smartmc.lobbymodule.command;

import me.imsergioh.pluginsapi.instance.menu.GUIMenu;;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.lobbymodule.handler.VisibilityManager;
import us.smartmc.lobbymodule.instance.PlayerVisibility;
import us.smartmc.smartaddons.plugin.AddonPluginCommand;

import java.util.*;

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
        player.playSound(player.getLocation(), Sound.WOOD_CLICK, 1f, 0.75f);

        // Update item in hand in case that it's a visibility item (INK_SACK in hand)
        ItemStack item = player.getItemInHand();
        if (!(item.getType().name().startsWith("INK") || item.getType().name().contains("DYE"))) return;
        GUIMenu menu = GUIMenu.getSetGUI(player);
        item = VisibilityManager.getVisibilityItem(player, nextVisibility).get(player);
        player.getInventory().setItemInHand(item);
        if (menu != null)
            menu.getActionManager().registerItemAction(player.getInventory().getHeldItemSlot(),
                    item, Arrays.asList("cmd changeVisibility"));
    }

    @Override
    public void executeAdminPlayer(Player player, String[] strings) {
    }
}
