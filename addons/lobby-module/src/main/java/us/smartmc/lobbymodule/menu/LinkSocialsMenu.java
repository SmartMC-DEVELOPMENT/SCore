package us.smartmc.lobbymodule.menu;

import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.lobbymodule.instance.LinkSocialType;
import us.smartmc.lobbymodule.util.MenuUtil;

public class LinkSocialsMenu extends CoreMenu {

    private final int backSlot;

    public LinkSocialsMenu(Player player) {
        super(player, 54, "<lang.lobby.menu_link_socials_title>");
        backSlot = getInventory().getSize() - 5;
    }

    @Override
    public void load() {
        ItemBuilder relleno = ItemBuilder.of(Material.STAINED_GLASS_PANE).data(3).name(" ");
        MenuUtil.setBorder(relleno.get(), inventory);
        if (corePlayer.getPreviousOpenMenu() != null)
            set(backSlot, ItemBuilder.of(Material.BOOK).name("<lang.language.menu_previous>").get(player));

        register(12, LinkSocialType.YOUTUBE);
        register(14, LinkSocialType.TWITCH);
    }

    private void register(int slot, LinkSocialType type) {
        set(slot, ItemBuilder.of(Material.SKULL_ITEM).data(3)
                .skullTexture(type.getSkullTexture())
                .name(type.name()).get(), "linkSocial" + type.name());
    }

}
