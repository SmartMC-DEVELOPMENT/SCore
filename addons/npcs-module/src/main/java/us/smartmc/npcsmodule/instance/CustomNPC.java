package us.smartmc.npcsmodule.instance;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.pluginsapi.util.SyncUtil;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.PlayerInteractManager;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.ScoreboardTeamBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.*;

public class CustomNPC {

    private static final MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();

    @Getter
    private final String name;
    private final WorldServer worldServer;
    @Getter
    private final EntityPlayer entityPlayer;
    private final GameProfile gameProfile;
    private final PlayerInteractManager manager;
    private final NPCHologramManager hologramManager;

    @Getter
    private List<String> lines;
    @Getter
    private List<String> commandLines = new ArrayList<>();

    public CustomNPC(World world, String name, String skinValue, String skinSignature) {
        this.name = name;
        this.worldServer = ((CraftWorld) world).getHandle();
        this.gameProfile = new GameProfile(UUID.randomUUID(), name);
        ClientInformation clientInformation = ClientInformation.createDefault();
        this.entityPlayer = new EntityPlayer(server, worldServer, gameProfile, clientInformation);
        this.manager = new PlayerInteractManager(entityPlayer);
        // SET SKIN VALUE & SIGNATURE IF NOT NULL BOTH STRINGS
        if (skinValue != null && skinSignature != null) setSkinValue(skinValue, skinSignature);
        hologramManager = new NPCHologramManager(this);
    }

    public CustomNPC(World world, String name) {
        this(world, name, null, null);
    }

    public void setNameVisible(boolean active) {
        entityPlayer.setCustomNameVisible(active);
    }

    public void setCommandLines(List<String> commandLines) {
        this.commandLines = commandLines;
    }

    public void setLines(List<String> list) {
        this.lines = list;
        hologramManager.setupStands();
    }

    public void setSkinValue(String value, String signature) {
        gameProfile.getProperties().put("textures", new Property("textures", value, signature));
    }

    public void removeViewer(Player player) {
        hologramManager.removeViewer(player);
    }

    public void showTo(Player player) {
        EntityPlayer ep = parseEntity(player);
        entityPlayer.setCustomName(IChatBaseComponent.empty());
        ep.setCustomName(entityPlayer.getCustomName());

        // LOCATION
        ep.teleportTo(worldServer, entityPlayer.getX(), entityPlayer.getY(), entityPlayer.getZ(),
                entityPlayer.getYHeadRot(), entityPlayer.xRotO);
        PlayerConnection connection = ((EntityPlayer) player).connection;

        // SHOW ENTITY WITH CORRECT DATA

        connection.send(new ClientboundPlayerInfoUpdatePacket(
                ClientboundPlayerInfoUpdatePacket.a.ADD_PLAYER,
                ep));

        connection.send(new PacketPlayOutSpawnEntity(ep));

        // REMOVE FROM TAB:
        sendPacketPlayOutEntityMetadata(player, entityPlayer.getId(), List.of());
        connection.send(new PacketPlayOutEntityHeadRotation(ep, (byte) ((ep.yHeadRot * 256.0F) / 360.0F)));

        // NAME VISIBLE:
        if (!player.isCustomNameVisible()) {
            hologramManager.createHologram(player);
            ScoreboardTeam teamScore = new ScoreboardTeam(((EntityPlayer) player).getScoreboard(), player.getName());
            teamScore.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
            teamScore.getPlayers().add(entityPlayer.getName().getString());
            entityPlayer.setCustomNameVisible(player.isCustomNameVisible());
            SyncUtil.later(() -> {
                connection.send(new ClientboundPlayerInfoRemovePacket(List.of(ep.getUUID())));
            }, 1750);
        }
    }

    public void sendPacketPlayOutEntityMetadata(Player player, int entityID, List<WrappedWatchableObject> metadataList) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);

        packet.getIntegers().write(0, entityID); // Establece el ID de la entidad
        packet.getWatchableCollectionModifier().write(0, metadataList); // Establece los metadatos

        try {
            protocolManager.sendServerPacket(player, packet); // Envía el paquete al jugador
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showToAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(this::showTo);
    }

    public void setLocation(Location location) {
        entityPlayer.teleportTo(server.overworld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    private EntityPlayer parseEntity(Player player) {
        try {
            Field nameField = GameProfile.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(entityPlayer.getGameProfile(), ChatUtil.parse(player, name));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            entityPlayer.getGameProfile().getProperties().put("textures", gameProfile.getProperties().get("textures").iterator().next());
        } catch (Exception ignore) {
        }
        return entityPlayer;
    }

    public Location getLocation() {
        double x = entityPlayer.getX();
        double y = entityPlayer.getY();
        double z = entityPlayer.getZ();
        net.minecraft.world.level.World mcWorld = entityPlayer.getCommandSenderWorld();
        return new Location(Bukkit.getWorlds().get(0), x, y, z);
    }

    public UUID getUUID() {
        return gameProfile.getId();
    }

}
