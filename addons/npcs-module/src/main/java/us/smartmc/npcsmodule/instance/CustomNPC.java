package us.smartmc.npcsmodule.instance;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.mojang.authlib.GameProfile;
import lombok.Getter;
import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.pluginsapi.util.SyncUtil;
import net.kyori.adventure.text.Component;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.*;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.ScoreboardTeamBase;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import us.smartmc.npcsmodule.NPCSModule;

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

    private Location bukkitLocation;

    private final Document configData;
    private final String skinValue, skinSignature;

    public CustomNPC(ServerLevel world, String name, String skinValue, String skinSignature, Document configData) {
        this.npcPlayer = new ServerPlayer(server, world, new GameProfile(UUID.randomUUID(), name), ClientInformation.createDefault());
        this.skinValue = skinValue;
        this.skinSignature = skinSignature;
        this.configData = configData;
        // SET SKIN VALUE & SIGNATURE IF NOT NULL BOTH STRINGS
        hologramManager = new NPCHologramManager(this);
        this.world = world;
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

        parseEntity(player);

        npcPlayer.setCustomNameVisible(configData.getBoolean("nameVisible"));

        SynchedEntityData synchedEntityData = npcPlayer.getEntityData();
        synchedEntityData.set(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), (byte) 127);

        setValue(npcPlayer, "c", ((CraftPlayer) player).getHandle().connection);
        if (bukkitLocation != null)
            npcPlayer.forceSetPositionRotation(bukkitLocation.getX(), bukkitLocation.getY(), bukkitLocation.getZ(), bukkitLocation.getYaw(), bukkitLocation.getPitch());
        else System.out.println("DETECTED NULL BUKKITLOC -> " + npcPlayer.getName().getString());

        ClientboundPlayerInfoUpdatePacket infoUpdatePacket = new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npcPlayer);
        ClientboundAddEntityPacket addEntityPacket = new ClientboundAddEntityPacket(npcPlayer);
        ClientboundSetEntityDataPacket dataPacket = new ClientboundSetEntityDataPacket(npcPlayer.getId(), synchedEntityData.getNonDefaultValues());

        ((CraftPlayer) player).getHandle().connection.send(infoUpdatePacket);
        ((CraftPlayer) player).getHandle().connection.send(addEntityPacket);
        ((CraftPlayer) player).getHandle().connection.send(dataPacket);

        if (!npcPlayer.isCustomNameVisible()) {
            Scoreboard scoreboard = player.getScoreboard();
            Team team = scoreboard.getTeam("hideTag");
            if (team == null) team = scoreboard.registerNewTeam("hideTag");
            team.setNameTagVisibility(NameTagVisibility.NEVER);
            team.addEntry(npcPlayer.getName().getString());
        }

        hologramManager.createHologram(player);
    }

    public void showToAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(this::showTo);
    }

    private void updateNMSLocation(Location loc) {
        System.out.println("Location=" + loc);
        npcPlayer.setPos(loc.getX(), loc.getY(), loc.getZ());
        npcPlayer.teleportTo(((CraftWorld) loc.getWorld()).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
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

    public void setBukkitLocation(Location bukkitLocation) {
        this.bukkitLocation = bukkitLocation;
        System.out.println("BukkitLocation set to = " + bukkitLocation);
        updateNMSLocation(bukkitLocation);
    }
}
