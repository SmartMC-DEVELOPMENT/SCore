package me.imsergioh.smartcorewaterfall.manager;

import com.mongodb.client.MongoCursor;
import me.imsergioh.smartcorewaterfall.instance.sanction.PlayerSanction;
import me.imsergioh.smartcorewaterfall.instance.sanction.SanctionType;
import me.imsergioh.smartcorewaterfall.util.TimeUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SanctionsManager {

    private static final HashMap<UUID, List<PlayerSanction>> sanctions = new HashMap<>();

    public static void create(UUID uuid, SanctionType type, TimeUtils utils, String reason) {
        PlayerSanction sanction = new PlayerSanction(uuid, type, utils, reason);
        List<PlayerSanction> sanctionList = get(uuid);
        sanctionList.add(sanction);
        sanctions.put(uuid, sanctionList);
    }

    public static List<PlayerSanction> loadSanctions(ProxiedPlayer player) {
        UUID uuid = player.getUniqueId();
        List<PlayerSanction> collection = new ArrayList<>();
        String ip = player.getPendingConnection().getAddress().getAddress().toString();
        for (MongoCursor<Document> it = PlayerSanction.sanctionsCollection().find(new Document("ip_address", ip)).cursor(); it.hasNext(); ) {
            Document document = it.next();
            PlayerSanction sanction = new PlayerSanction(document);
            collection.add(sanction);
        }
        sanctions.put(uuid, collection);
        return collection;
    }

    public static List<PlayerSanction> get(UUID uuid) {
        if (!sanctions.containsKey(uuid)) sanctions.put(uuid, new ArrayList<>());
        return sanctions.get(uuid);
    }

}
