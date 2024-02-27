package us.smartmc.lobbycosmetics;

import lombok.Getter;
import me.imsergioh.pluginsapi.handler.VariablesHandler;
import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;
import me.imsergioh.pluginsapi.manager.ItemActionsManager;
import us.smartmc.core.handler.ExceptionHandler;
import us.smartmc.lobbycosmetics.handler.CosmeticSectionHandler;
import us.smartmc.lobbycosmetics.handler.CosmeticSessionHandler;
import us.smartmc.lobbycosmetics.itemcommand.*;
import us.smartmc.lobbycosmetics.listener.MenuListeners;
import us.smartmc.lobbycosmetics.listener.SessionListeners;
import us.smartmc.lobbycosmetics.message.CosmeticsMainMessages;
import us.smartmc.lobbycosmetics.variable.CosmeticSectionNameVariable;
import us.smartmc.smartaddons.plugin.AddonInfo;
import us.smartmc.smartaddons.plugin.AddonPlugin;

@AddonInfo(name = "lobby-cosmetics", version = "DEVELOPMENT-0.1")
public class LobbyCosmetics extends AddonPlugin {

    @Getter
    private static LobbyCosmetics instance;

    @Getter
    private static MultiLanguageRegistry cosmeticsMainMessages;

    @Getter
    private SpigotYmlConfig config;

    @Getter
    private static final CosmeticSessionHandler playerSessionsHandler = new CosmeticSessionHandler();
    @Getter
    private static final CosmeticSectionHandler sectionsHandler = new CosmeticSectionHandler();

    @Override
    public void start() {
        instance = this;
        ItemActionsManager.registerCommand("itemCosmetics", new ItemCosmeticAction());
        ItemActionsManager.registerCommand("openCosmeticSection", new OpenCosmeticSectionAction());
        ItemActionsManager.registerCommand("toggleCosmetic", new ToggleCosmeticAction());
        ItemActionsManager.registerCommand("confirmCosmeticPurchase", new ConfirmCosmeticPurchase());
        ItemActionsManager.registerCommand("cancelCosmeticPurchase", new CancelCosmeticPurchase());
        registerListeners(new SessionListeners(), new MenuListeners());
        cosmeticsMainMessages = new CosmeticsMainMessages();

        VariablesHandler.register(new CosmeticSectionNameVariable());
    }

    @Override
    public void stop() {

    }
}
