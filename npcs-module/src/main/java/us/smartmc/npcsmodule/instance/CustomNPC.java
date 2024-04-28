package us.smartmc.npcsmodule.instance;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.pluginsapi.util.LegacyChatUtil;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Field;
import java.util.*;

public class CustomNPC {

    private static final MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
    private static final String HIDE_TAGS_TEAM_NAME = "ht-" + new Random().nextInt(9999);

    @Getter
    private List<String> commandLines = new ArrayList<>();

    public final ServerLevel world;

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
        this.world = world;
    }

    public void setNameVisible(boolean active) {
        npcPlayer.setCustomNameVisible(active);
    }

    public void setCommandLines(List<String> commandLines) {
        this.commandLines = commandLines;
    }

    public void showTo(Player player) {

        parseEntity(player);

        npcPlayer.setCustomNameVisible(configData.getBoolean("nameVisible"));
        npcPlayer.getBukkitEntity().setCustomNameVisible(configData.getBoolean("nameVisible"));

        SynchedEntityData synchedEntityData = npcPlayer.getEntityData();
        synchedEntityData.set(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), (byte) 127);

        setValue(npcPlayer, "c", ((CraftPlayer) player).getHandle().connection);
        if (bukkitLocation != null)
            npcPlayer.forceSetPositionRotation(bukkitLocation.getX(), bukkitLocation.getY(), bukkitLocation.getZ(), bukkitLocation.getYaw(), bukkitLocation.getPitch());

        ClientboundPlayerInfoUpdatePacket infoUpdatePacket = new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npcPlayer);
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoActions().write(0, EnumSet.of(EnumWrappers.PlayerInfoAction.ADD_PLAYER));

        ClientboundAddEntityPacket addEntityPacket = new ClientboundAddEntityPacket(npcPlayer);
        ClientboundSetEntityDataPacket dataPacket = new ClientboundSetEntityDataPacket(npcPlayer.getId(), synchedEntityData.getNonDefaultValues());

        ((CraftPlayer) player).getHandle().connection.send(infoUpdatePacket);
        ((CraftPlayer) player).getHandle().connection.send(addEntityPacket);
        ((CraftPlayer) player).getHandle().connection.send(dataPacket);

        Bukkit.getScheduler().runTaskLater(SpigotPluginsAPI.getPlugin(), () -> {
            hideTagByPlayerScoreboard(player);
        }, 0);
    }

    public void hideTagByPlayerScoreboard(Player player) {
        if (!npcPlayer.isCustomNameVisible()) {
            Scoreboard scoreboard = player.getScoreboard();
            Team team = scoreboard.getTeam(HIDE_TAGS_TEAM_NAME);
            if (team == null) team = scoreboard.registerNewTeam(HIDE_TAGS_TEAM_NAME);
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
            team.addEntry(npcPlayer.getBukkitEntity().getName());
        }
    }

    private void updateNMSLocation(Location loc) {
        npcPlayer.setPos(loc.getX(), loc.getY(), loc.getZ());
        npcPlayer.teleportTo(((CraftWorld) loc.getWorld()).getHandle(), loc.getX(), loc.getY(), loc.getZ(), (byte) loc.getYaw() * 360.0F / 256.0F, (byte) loc.getPitch() * 360.F / 256.0F);
    }

    private void parseEntity(Player player) {
        try {
            Field nameField = GameProfile.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(npcPlayer.getGameProfile(), LegacyChatUtil.parse(player, npcPlayer.getName().getString()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            npcPlayer.getGameProfile().getProperties().removeAll("textures");
            npcPlayer.getGameProfile().getProperties().put("textures", new Property("textures", skinValue, skinSignature));
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
        updateNMSLocation(bukkitLocation);
    }
}
