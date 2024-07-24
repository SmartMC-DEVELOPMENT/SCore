package us.smartmc.smartcore.proxy.manager;

import com.mongodb.client.MongoCursor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bson.Document;
import us.smartmc.smartcore.proxy.instance.sanction.PlayerSanction;
import us.smartmc.smartcore.proxy.instance.sanction.SanctionType;
import us.smartmc.smartcore.proxy.util.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SanctionsManager {

    private static final HashMap<UUID, List<PlayerSanction>> sanctions = new HashMap<>();

    public static void create(UUID uuid, UUID creatorId, SanctionType type, TimeUtils utils, String reason) {
        PlayerSanction sanction = new PlayerSanction(uuid, creatorId, type, utils, reason);
        List<PlayerSanction> sanctionList = get(uuid);
        sanctionList.add(sanction);
        sanctions.put(uuid, sanctionList);
    }

    public static void loadSanctions(ProxiedPlayer player) {
        UUID uuid = player.getUniqueId();
        List<PlayerSanction> collection = new ArrayList<>();
        String ip = player.getPendingConnection().getSocketAddress().toString().replace("/", "");
        if (ip.contains(":")) ip = ip.split(":")[0];
        System.out.println("loadSanctions -> " + ip);
        for (MongoCursor<Document> it = PlayerSanction.sanctionsCollection().find(new Document("ip_address", ip)).cursor(); it.hasNext(); ) {
            Document document = it.next();
            PlayerSanction sanction = new PlayerSanction(player.getUniqueId(), document);
            collection.add(sanction);
        }
        sanctions.put(uuid, collection);
    }

    public static List<PlayerSanction> get(UUID uuid) {
        if (!sanctions.containsKey(uuid)) sanctions.put(uuid, new ArrayList<>());
        return sanctions.get(uuid);
    }

}
