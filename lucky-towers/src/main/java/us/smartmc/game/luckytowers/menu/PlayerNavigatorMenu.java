package us.smartmc.game.luckytowers.menu;

import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import us.smartmc.game.luckytowers.instance.game.GameSession;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;
import us.smartmc.game.luckytowers.messages.GameMessages;

public class PlayerNavigatorMenu extends GameMenu {

    public PlayerNavigatorMenu(Player player) {
        super(player, 27, GameMessages.menu_navigator_title);
    }

    @Override
    public void load() {
        GamePlayer gamePlayer = GamePlayer.get(initPlayer.getUniqueId());
        GameSession session = gamePlayer.getGameSession();

        int slot = 0;

        if (session == null) return;

        for (GamePlayer sessionPlayer : session.getAlivePlayers()) {
            Player alivePlayer = sessionPlayer.getBukkitPlayer();
            String name = alivePlayer.getName();
            ItemBuilder builder = ItemBuilder.of(Material.PLAYER_HEAD);
            builder.name("&a" + name);
            set(slot, parseBuilderToPlayersHead(builder, alivePlayer));
            slot++;
        }
    }

    private ItemStack parseBuilderToPlayersHead(ItemBuilder builder, Player player) {
        builder.lore(GameMessages.itemDescription_teleportToPlayer.getMessageListOf(PlayerLanguages.get(initPlayer.getUniqueId())), player.getName());
        ItemStack itemStack = builder.get(initPlayer);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwningPlayer(player);
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }

}
