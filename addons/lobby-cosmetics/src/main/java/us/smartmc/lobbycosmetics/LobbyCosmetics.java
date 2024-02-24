package us.smartmc.lobbycosmetics;

import lombok.Getter;
import me.imsergioh.pluginsapi.handler.VariablesHandler;
import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;
import me.imsergioh.pluginsapi.manager.ItemActionsManager;
import us.smartmc.lobbycosmetics.handler.CosmeticSectionHandler;
import us.smartmc.lobbycosmetics.handler.CosmeticSessionHandler;
import us.smartmc.lobbycosmetics.instance.cosmetic.CosmeticType;
import us.smartmc.lobbycosmetics.itemcommand.ItemCosmeticAction;
import us.smartmc.lobbycosmetics.itemcommand.OpenCosmeticSectionAction;
import us.smartmc.lobbycosmetics.itemcommand.ToggleCosmeticAction;
import us.smartmc.lobbycosmetics.listener.SessionListeners;
import us.smartmc.lobbycosmetics.message.CosmeticsMainMessages;
import us.smartmc.lobbycosmetics.message.CosmeticsSectionMessages;
import us.smartmc.lobbycosmetics.variable.CosmeticSectionNameVariable;
import us.smartmc.smartaddons.plugin.AddonInfo;
import us.smartmc.smartaddons.plugin.AddonPlugin;

import java.util.HashSet;
import java.util.Set;

@AddonInfo(name = "lobby-cosmetics", version = "DEVELOPMENT-0.1")
public class LobbyCosmetics extends AddonPlugin {

    @Getter
    private static LobbyCosmetics instance;

    @Getter
    private static MultiLanguageRegistry cosmeticsInfoMessages;

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
        registerListeners(new SessionListeners());
        cosmeticsInfoMessages = new CosmeticsMainMessages();

        VariablesHandler.register(new CosmeticSectionNameVariable());

    }

    @Override
    public void stop() {

    }
}
