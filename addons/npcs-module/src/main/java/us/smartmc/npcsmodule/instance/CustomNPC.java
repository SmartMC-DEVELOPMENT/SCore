package us.smartmc.npcsmodule.instance;

import com.mojang.authlib.GameProfile;
import lombok.Getter;
import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import me.imsergioh.pluginsapi.util.ChatUtil;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.*;

public class CustomNPC {

    private static final MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();

    @Getter
    private List<String> lines;
    @Getter
    private List<String> commandLines = new ArrayList<>();

    public final ServerLevel world;

    private final NPCHologramManager hologramManager;

    @Getter
    private final ServerPlayer npcPlayer;

    public CustomNPC(ServerLevel world, String name, String skinValue, String skinSignature, ClientInformation ci) {
        this.npcPlayer = new ServerPlayer(server, world, new GameProfile(UUID.randomUUID(), name), ClientInformation.createDefault());
        // SET SKIN VALUE & SIGNATURE IF NOT NULL BOTH STRINGS
        hologramManager = new NPCHologramManager(this);
        this.world = world;
    }

    public CustomNPC(ServerLevel world, String name) {
        this(world, name, null, null, ClientInformation.createDefault());
    }

    public void setNameVisible(boolean active) {
        npcPlayer.setCustomNameVisible(active);
    }

    public void setCommandLines(List<String> commandLines) {
        this.commandLines = commandLines;
    }

    public void setLines(List<String> list) {
        this.lines = list;
        hologramManager.setupStands();
    }

    public void removeViewer(Player player) {
        hologramManager.removeViewer(player);
    }

    public void showTo(Player player) {

        Location location = getLocation();
        npcPlayer.setPos(location.getX(), location.getY(), location.getZ());

        SynchedEntityData synchedEntityData = npcPlayer.getEntityData();
        synchedEntityData.set(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), (byte) 127);

        setValue(npcPlayer, "c", ((CraftPlayer) player).getHandle().connection);

        ClientboundPlayerInfoUpdatePacket infoUpdatePacket = new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npcPlayer);
        ClientboundAddEntityPacket addEntityPacket = new ClientboundAddEntityPacket(npcPlayer);
        ClientboundSetEntityDataPacket dataPacket = new ClientboundSetEntityDataPacket(npcPlayer.getId(), synchedEntityData.getNonDefaultValues());

        ((CraftPlayer) player).getHandle().connection.send(infoUpdatePacket);
        ((CraftPlayer) player).getHandle().connection.send(addEntityPacket);
        ((CraftPlayer) player).getHandle().connection.send(dataPacket);
    }

    public void showToAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(this::showTo);
    }

    public void setLocation(Location location) {
        npcPlayer.setPos(location.getX(), location.getY(), location.getZ());
        npcPlayer.teleportTo(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    private void parseEntity(Player player) {
        try {
            Field nameField = GameProfile.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(npcPlayer.getGameProfile(), ChatUtil.parse(player, npcPlayer.getName().getString()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            npcPlayer.getGameProfile().getProperties().put("textures", npcPlayer.getGameProfile().getProperties().get("textures").iterator().next());
        } catch (Exception ignore) {
        }
    }

    public Location getLocation() {
        double x = npcPlayer.getX();
        double y = npcPlayer.getY();
        double z = npcPlayer.getZ();
        return new Location(world.getWorld(), x, y, z);
    }

    public UUID getUUID() {
        return npcPlayer.getGameProfile().getId();
    }

    public static void setValue(Object packet, String fieldName, Object value) {
        try {
            Field field = packet.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(packet, value);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
