package us.smartmc.lobbymodule.config;

import us.smartmc.core.variables.CountVariables;
import us.smartmc.core.pluginsapi.handler.LanguagesHandler;
import us.smartmc.core.pluginsapi.instance.MongoDBPluginConfig;
import us.smartmc.core.pluginsapi.language.Language;
import us.smartmc.core.pluginsapi.spigot.item.ClickHandler;
import us.smartmc.core.pluginsapi.spigot.item.ItemBuilder;
import us.smartmc.core.pluginsapi.spigot.player.CorePlayer;
import us.smartmc.core.pluginsapi.util.ChatUtil;
import us.smartmc.core.pluginsapi.util.LineLimiter;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.lobbymodule.LobbyModule;

import java.text.MessageFormat;
import java.util.*;
import java.util.function.Consumer;

public class MinigamesConfig extends MongoDBPluginConfig {

    public static final String INV_SIZE_PATH = "inv_size";

    public MinigamesConfig() {
        super("server_data", "menus", new Document().append("name", "minigames"));
        load();
        registerDefault(INV_SIZE_PATH, 27);
        registerMiniGame("snowmatch");
        save();
    }

    public HashMap<String, Document> getMiniGames() {
        HashMap<String, Document> map = new HashMap<>();
        for (String key : keySet()) {
            Object value = get(key);
            if (value instanceof Document) map.put(key, (Document) value);
        }
        return map;
    }

    private void registerMiniGame(String name) {
        registerDefault(name, new Document()
                .append("slot", 0)
                .append("material", Material.BEDROCK.name())
                .append("data", (byte) 1));
    }

    public static ItemStack getItemOf(Player player, String name) {
        CorePlayer corePlayer = CorePlayer.get(player.getUniqueId());
        Language language = corePlayer.getLanguage();
        Document configDoc = LobbyModule.getMinigamesConfig().get(name, Document.class);
        Document document = LanguagesHandler.get(language).get("lobby_miniGames").get(name, Document.class);
        String itemName = document.getString("name");

        Material material = Material.BEDROCK;
        int amount = 1;
        String serverID = null;
        String titleCustom = null;

        if (configDoc.containsKey("serverID")) serverID = configDoc.getString("serverID");
        if (configDoc.containsKey("material")) material = Material.valueOf(configDoc.getString("material"));
        if (configDoc.containsKey("amount")) amount = configDoc.getInteger("amount");
        if (configDoc.containsKey("title_custom")) titleCustom = configDoc.getString("title_custom");

        ArrayList<String> description = LineLimiter.createListFromNewLines(document.getString("description"));
        String clickToConnect = LanguagesHandler.get(language).get("lobby_miniGames").getString("click_to_connect");
        String connected = String.valueOf(LanguagesHandler.get(language).get("lobby_miniGames").getString("current_playing"));
        description = (ArrayList<String>) LineLimiter.limitLines(description, 48);

        ArrayList<String> list = new ArrayList<>();


        if (titleCustom != null) {
            list.add(ChatUtil.parse(player, titleCustom));
        }

        if (configDoc.getBoolean("prototype", false)) {
            // IF PROTOTYPE BOOLEAN IS TRUE:
            list.add(ChatUtil.parse(player, "<lang.lobby_miniGames.prototype_title>"));
        }

        list.add("&r");
        list.addAll(description);
        description = list;

        String count = CountVariables.getCountOf("online." + serverID);
        description.addAll(Arrays.asList("", clickToConnect));
        if (Long.parseLong(count) >= 1) {
            connected = MessageFormat.format(connected, count);
            description.add(connected);
        }
        description.replaceAll(l -> "&7" + l);

        return ItemBuilder.of(material)
                .name(itemName)
                .lore(description)
                .amount(amount)
                .get(player);
    }

    public static String getLabelCommand(String name) {
        String labelCommand = null;
        Document configDoc = LobbyModule.getMinigamesConfig().get(name, Document.class);
        if (configDoc.containsKey("command")) labelCommand = configDoc.getString("command");
        return labelCommand;
    }

    private static String getCountOf(String idName) {
        return CountVariables.getCountOf(idName);
    }

    public int getInventorySize() {
        return getInteger(INV_SIZE_PATH);
    }

}
