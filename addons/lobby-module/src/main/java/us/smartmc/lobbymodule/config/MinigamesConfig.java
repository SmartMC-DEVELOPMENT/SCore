package us.smartmc.lobbymodule.config;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.instance.MongoDBPluginConfig;
import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.pluginsapi.util.LineLimiter;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.core.SmartCore;
import us.smartmc.core.variables.CountVariables;
import us.smartmc.lobbymodule.LobbyModule;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MinigamesConfig extends MongoDBPluginConfig {

    public static final String INV_SIZE_PATH = "inv_size";

    public MinigamesConfig() {
        super("server_data", "menus", new Document().append("_id", SmartCore.RELEASE_ID));
        load();
        registerDefault(INV_SIZE_PATH, 27);
        registerMiniGame("snowmatch");
        registerMiniGame("survival");
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
        Language language = PlayerLanguages.get(player.getUniqueId());
        Document configDoc = LobbyModule.getMinigamesConfig().get(name, Document.class);
        Document document = LanguagesHandler.get(language).get("lobby_miniGames").get(name, Document.class);
        String itemName = document.getString("name");

        Material material = Material.BEDROCK;
        int amount = 1;
        String serverPrefixId = null;
        String titleCustom = null;

        if (configDoc.containsKey("serverPrefixId")) serverPrefixId = configDoc.getString("serverPrefixId");
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

        String count = CountVariables.getCountOf("online." + serverPrefixId);
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
