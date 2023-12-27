package us.smartmc.lobbymodule.menu;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.ConfigurableMenu;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.core.SmartCore;
import us.smartmc.core.variables.CountVariables;
import us.smartmc.lobbymodule.LobbyModule;
import us.smartmc.lobbymodule.handler.LobbiesInfoManager;
import us.smartmc.lobbymodule.handler.MaxSlotsInfoManager;

import java.util.Arrays;


public class LobbiesMenu extends ConfigurableMenu {

    public LobbiesMenu(Player player) {
        super(player, getDynamicInventorySize(), LobbyModule.getLobbiesMenuConfig());
        LobbiesInfoManager.registerMenu(this);
    }

    @Override
    public void load() {
        inventory.clear();
        int slot = 0;
        String serverServerID = SmartCore.getServerID();
        for (String serverID : CountVariables.getKeysByPrefix(LobbiesInfoManager.getIDPrefix())) {
            boolean isSelf = serverID.equals(serverServerID);
            Material material = Material.QUARTZ_BLOCK;
            byte materialData = 0;
            String variableConnect = "<lang.lobby_miniGames.click_to_connect>";
            String labelCommand = "connectTo " + serverID;
            if (isSelf) {
                material = Material.STAINED_CLAY;
                materialData = 14;
                variableConnect = "<lang.lobby.already_connected>";
                labelCommand = "closeInv";
            }

            int number = Integer.parseInt(serverID.split("-")[serverID.split("-").length - 1]);
            String count = CountVariables.getCountOf(serverID);

            set(slot, ItemBuilder.of(material).data(materialData).name(getItemNamePrefix(isSelf) + getItemName(number))
                    .lore(Arrays.asList("&7" + count + "/" + MaxSlotsInfoManager.getMaxSlotsOf(serverID), "&r",
                            variableConnect))
                    .get(player),labelCommand);
            slot++;
        }
    }

    public String getItemNamePrefix(boolean isSelf) {
        String prefixPath = "lobby_name_prefix";
        if (isSelf) {
            prefixPath = "current_lobby_name_prefix";
        }
        return ChatUtil.parse(player, "<lang.lobby." + prefixPath + ">");
    }

    public String getItemName(int lobbyNumber) {
        return ChatUtil.parse(player, LobbyModule.getLobbiesMenuConfig().getString("item_name"), lobbyNumber);
    }

    public static int getDynamicInventorySize() {
        int size = CountVariables.getKeysByPrefix(LobbiesInfoManager.getIDPrefix()).size();

        // Limitar el tamaño a un máximo de 54
        if (size > 54) size = 54;

        // Ajustar el tamaño para que sea un múltiplo de 9
        int remainder = size % 9;
        if (remainder != 0) {
            size += 9 - remainder;
        }

        // Asegurar que el tamaño sea al menos 9 si el original es mayor que 0
        if (size == 0 && !CountVariables.getKeysByPrefix(LobbiesInfoManager.getIDPrefix()).isEmpty()) {
            size = 9;
        }
        return size;
    }

}