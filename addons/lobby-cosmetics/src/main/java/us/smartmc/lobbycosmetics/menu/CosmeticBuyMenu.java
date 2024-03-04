package us.smartmc.lobbycosmetics.menu;

import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.core.exception.CorePluginException;
import us.smartmc.core.instance.player.PlayerCurrenciesHandler;
import us.smartmc.core.instance.player.PlayerCurrencyCoin;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.lobbycosmetics.LobbyCosmetics;
import us.smartmc.lobbycosmetics.instance.cosmetic.ICosmetic;
import us.smartmc.lobbycosmetics.instance.cosmetic.ICosmeticSection;
import us.smartmc.lobbycosmetics.instance.helper.MsgHolderLanguageInfo;
import us.smartmc.lobbycosmetics.instance.player.CosmeticPlayerSession;
import us.smartmc.lobbycosmetics.instance.player.ICosmeticPlayerSession;
import us.smartmc.lobbycosmetics.message.CosmeticsMainMessages;
import us.smartmc.lobbycosmetics.message.CosmeticsSectionMessages;
import us.smartmc.lobbycosmetics.util.ItemsUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CosmeticBuyMenu extends CoreMenu {

    private static final Map<UUID, ICosmetic> pendingRequests = new HashMap<>();

    private final ICosmeticPlayerSession playerSession;
    private final ICosmetic cosmetic;
    private final ICosmeticSection<?> section;

    public CosmeticBuyMenu(Player player, ICosmeticPlayerSession session, ICosmetic cosmetic) {
        super(player, 27, getTitle(player));
        this.playerSession = session;
        this.cosmetic = cosmetic;
        this.section = LobbyCosmetics.getSectionsHandler().get(cosmetic.getType());
        pendingRequests.put(player.getUniqueId(), cosmetic);
    }

    @Override
    public void load() {
        ItemBuilder buyBuilder = cosmetic.getPreviewItemBuilder(playerSession, section, PlayerLanguages.get(playerSession.getId()));
        MsgHolderLanguageInfo info = new MsgHolderLanguageInfo("cosmetic_" + cosmetic.getId(), PlayerLanguages.get(playerSession.getId()), CosmeticsSectionMessages.getName(section.getId()));
        List<String> lore = ItemsUtil.getBuilderCosmeticItemDescription(playerSession, info, cosmetic);
        String rarityLine = lore.get(0);
        lore.clear();
        lore.addAll(List.of(" ", rarityLine, " "));
        buyBuilder.lore(lore);
        set(4, buyBuilder.get(initPlayer));

        set(12, getBuyItem().get(initPlayer), "confirmCosmeticPurchase");
        set(14, getCancelBuyItem().get(initPlayer), "cancelCosmeticPurchase");
    }

    public ItemBuilder getBuyItem() {
        return ItemBuilder.of(Material.STAINED_CLAY).data(5)
                .name("<lang." + CosmeticsMainMessages.NAME + ".item_buy_name");
    }


    public ItemBuilder getCancelBuyItem() {
        return ItemBuilder.of(Material.STAINED_CLAY).data(14)
                .name("<lang." + CosmeticsMainMessages.NAME + ".item_cancel_buy_name");
    }

    public static void acceptBuyRequest(Player player) {
        ICosmetic cosmetic = pendingRequests.get(player.getUniqueId());
        pendingRequests.remove(player.getUniqueId());
        SmartCorePlayer smartCorePlayer = SmartCorePlayer.get(player);
        long cost = cosmetic.getCost();
        long currentBalance = SmartCorePlayer.get(player).getCoins();
        if (cost <= currentBalance) {
            try {
                String reason = LobbyCosmetics.getCosmeticsMainMessages().get(player, PlayerLanguages.get(player.getUniqueId()), "cosmetic_buyed_coins_reason");
                PlayerCurrenciesHandler handler = smartCorePlayer.getCurrenciesHandler();
                handler.remove(PlayerCurrencyCoin.SMARTCOINS, cost, reason);
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 0.5F, 1.0F);
                CosmeticPlayerSession session = CosmeticPlayerSession.get(player);
                session.getData().activateCosmetic(cosmetic);
            } catch (CorePluginException e) {
                throw new RuntimeException(e);
            }
        } else {
            smartCorePlayer.sendLanguageMessage(CosmeticsMainMessages.NAME, "cosmetic_no_balance");
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1);
        }
        player.closeInventory();
    }

    public static void clearCache(UUID uuid) {
        pendingRequests.remove(uuid);
    }

    public static void cancelBuyRequest(Player player) {
        pendingRequests.remove(player.getUniqueId());
        SmartCorePlayer.get(player).openPreviousMenu();
    }

    public static String getTitle(Player player) {
        return LobbyCosmetics.getCosmeticsMainMessages().get(PlayerLanguages.get(player.getUniqueId()), "menu_confirm_buy_title");
    }
}
