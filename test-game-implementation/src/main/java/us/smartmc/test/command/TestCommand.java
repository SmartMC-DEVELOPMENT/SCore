package us.smartmc.test.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.gamescore.util.InventorySerializer;
import us.smartmc.gamescore.util.ItemSerializer;

import java.io.IOException;

public class TestCommand implements CommandExecutor {

    private static String itemString;
    private static String invString;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Invalid args! saveItem, loadItem, saveInv, loadInv");
            return true;
        }

        if (sender instanceof Player player) {

            if (args[0].contains("saveItem")) {
                ItemStack item = player.getItemInHand();
                try {
                    itemString = ItemSerializer.serializeItemStack(item);
                    player.sendMessage("SAVE! " + itemString);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (args[0].contains("loadItem")) {
                try {
                    ItemStack item = ItemSerializer.deserializeItemStack(itemString);
                    player.setItemInHand(item);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            if (args[0].contains("saveInv")) {
                invString = InventorySerializer.serializeContents(player.getInventory().getContents());
                player.sendMessage("SAVE! " + invString);
            }

            if (args[0].contains("loadInv")) {
                try {
                    ItemStack[] content = InventorySerializer.deserializeContents(invString);
                    player.getInventory().setContents(content);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }




        return false;
    }
}
