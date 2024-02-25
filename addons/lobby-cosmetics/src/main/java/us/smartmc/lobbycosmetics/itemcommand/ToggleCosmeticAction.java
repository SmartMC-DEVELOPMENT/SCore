package us.smartmc.lobbycosmetics.itemcommand;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import us.smartmc.core.SmartCore;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.lobbycosmetics.LobbyCosmetics;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticType;
import us.smartmc.lobbycosmetics.instance.cosmetic.ICosmetic;
import us.smartmc.lobbycosmetics.instance.helper.MsgHolderLanguageInfo;
import us.smartmc.lobbycosmetics.instance.player.CosmeticPlayerSession;
import us.smartmc.lobbycosmetics.menu.CosmeticBuyMenu;
import us.smartmc.lobbycosmetics.message.CosmeticsMainMessages;
import us.smartmc.lobbycosmetics.message.CosmeticsSectionMessages;

public class ToggleCosmeticAction implements ItemActionExecutor {

    @Override
    public void execute(ClickHandler handler, String label, String[] args) {
        Player player = handler.player();
        CosmeticType type = CosmeticType.valueOf(args[0]);
        String name = args[1];
        CosmeticPlayerSession playerSession = CosmeticPlayerSession.get(player);
        ICosmetic cosmetic = LobbyCosmetics.getSectionsHandler().get(type).get(name);

        if (!playerSession.getData().hasCosmetic(cosmetic)) {
            new CosmeticBuyMenu(player, playerSession, cosmetic).open(player);
            return;
        }
        cosmetic.toggleEntity(player);
        player.closeInventory();

        MsgHolderLanguageInfo info = new MsgHolderLanguageInfo("cosmetic_" + cosmetic.getId(), PlayerLanguages.get(player.getUniqueId()), CosmeticsSectionMessages.getName(cosmetic.getType()));
        SmartCorePlayer smartCorePlayer = SmartCorePlayer.get(player);
        smartCorePlayer.sendLanguageMessage(CosmeticsMainMessages.NAME, "cosmetic_set", info.getName());
        smartCorePlayer.playSound(Sound.CLICK, 1, 1);
    }
}
