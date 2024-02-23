package us.smartmc.lobbycosmetics;

import lombok.Getter;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;
import me.imsergioh.pluginsapi.manager.ItemActionsManager;
import us.smartmc.lobbycosmetics.itemcommand.ItemCosmeticAction;
import us.smartmc.lobbycosmetics.listener.SessionListeners;
import us.smartmc.lobbycosmetics.menu.CosmeticsMainMenu;
import us.smartmc.lobbycosmetics.message.CosmeticsInfoMessages;
import us.smartmc.smartaddons.plugin.AddonInfo;
import us.smartmc.smartaddons.plugin.AddonPlugin;

@AddonInfo(name = "lobby-cosmetics", version = "DEVELOPMENT-0.1")
public class LobbyCosmetics extends AddonPlugin {

    @Getter
    private static LobbyCosmetics instance;

    @Getter
    private static MultiLanguageRegistry cosmeticsInfoMessages;

    @Override
    public void start() {
        instance = this;
        ItemActionsManager.registerCommand("itemCosmetics", new ItemCosmeticAction());
        registerListeners(new SessionListeners());
        cosmeticsInfoMessages = new CosmeticsInfoMessages();
    }

    @Override
    public void stop() {

    }
}
