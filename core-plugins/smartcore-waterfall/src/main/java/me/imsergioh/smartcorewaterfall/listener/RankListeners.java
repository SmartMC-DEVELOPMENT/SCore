package me.imsergioh.smartcorewaterfall.listener;

import me.imsergioh.smartcorewaterfall.SmartCoreWaterfall;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.log.LogPublishEvent;
import net.luckperms.api.event.user.UserLoadEvent;
import net.luckperms.api.event.user.track.UserPromoteEvent;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;

public class RankListeners implements Listener {

    private void onUserPromote(UserPromoteEvent event) {

        net.luckperms.api.LuckPerms luckPerms = net.luckperms.api.LuckPermsProvider.get();
        EventBus eventBus = luckPerms.getEventBus();

        eventBus.subscribe(SmartCoreWaterfall.getPlugin(), LogPublishEvent.class, e -> {});
        eventBus.subscribe(SmartCoreWaterfall.getPlugin(), UserLoadEvent.class, e -> {});
        eventBus.subscribe(SmartCoreWaterfall.getPlugin(), UserPromoteEvent.class, this::onUserPromote);

        String developer = "developer";
        Group dev = luckPerms.getGroupManager().getGroup(developer);
        User user = event.getUser();

        if(event.getGroupTo().equals(dev)){
            ProxiedPlayer player = (ProxiedPlayer) user;
            TextComponent textComponent = getMessage();

            player.sendMessage(textComponent);
        }
    }

    private static TextComponent getMessage() {
        TextComponent textComponent = new TextComponent("\n");
        textComponent.addExtra("§b§lSmartMC §f§lNetwork §8§m   §f Mensaje para §atí\n");
        textComponent.addExtra("\n");
        textComponent.addExtra("§f¡Bienvenido al equipo de Desarrollo de SmartMC!\n");
        textComponent.addExtra("§fSe te ha otorgado el rango correctamente.\n");
        textComponent.addExtra("\n");
        textComponent.addExtra("§7§oSi tienes tu cuenta vinculada a §d§l§oDiscord§7§o,\n");
        textComponent.addExtra("§7§orecibirás el rango en unos instantes.\n");
        textComponent.addExtra("\n");
        return textComponent;
    }

}
