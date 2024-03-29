package us.smartmc.npcsmodule.instance;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.mojang.authlib.GameProfile;
import lombok.Getter;
import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.pluginsapi.util.SyncUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.*;

public class CustomNPC extends ServerPlayer {

    private static final MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();

    @Getter
    private List<String> lines;
    @Getter
    private List<String> commandLines = new ArrayList<>();

    public final ServerLevel world;

    private final NPCHologramManager hologramManager;

    public CustomNPC(ServerLevel world, String name, String skinValue, String skinSignature, ClientInformation ci) {
        super(server, world, new GameProfile(UUID.randomUUID(), name), ClientInformation.createDefault());
        // SET SKIN VALUE & SIGNATURE IF NOT NULL BOTH STRINGS
        hologramManager = new NPCHologramManager(this);
        this.world = world;
    }

    public CustomNPC(ServerLevel world, String name) {
        this(world, name, null, null, ClientInformation.createDefault());
    }

    public void setNameVisible(boolean active) {
        setCustomNameVisible(active);
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
        setCustomName(Component.empty());
        setCustomName(getCustomName());

        // SHOW ENTITY WITH CORRECT DATA

        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoActions().write(0, EnumSet.of(EnumWrappers.PlayerInfoAction.ADD_PLAYER));
        PlayerInfoData playerInfoData = new PlayerInfoData(new WrappedGameProfile(getUUID(), getName().getString()), 0, EnumWrappers.NativeGameMode.SURVIVAL, WrappedChatComponent.fromText(displayName));
        packet.getPlayerInfoDataLists().write(1, List.of(playerInfoData));
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);

        PacketContainer spawnPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        spawnPacket.getModifier().writeDefaults();
        var spawnPacketModifier = spawnPacket.getModifier();

        Location location = getLocation();

        spawnPacketModifier.write(0, getId());
        spawnPacketModifier.write(1, uuid);
        spawnPacketModifier.write(2, 122);
        spawnPacketModifier.write(3, location.getX());
        spawnPacketModifier.write(4, location.getY());
        spawnPacketModifier.write(5, location.getZ());

        ProtocolLibrary.getProtocolManager().sendServerPacket(player, spawnPacket);

        // REMOVE FROM TAB:
        //sendPacketPlayOutEntityMetadata(player);

        // NAME VISIBLE:
        /*if (!isCustomNameVisible()) {
            hologramManager.createHologram(player);
            ScoreboardTeam teamScore = new ScoreboardTeam(((CraftScoreboard) player.getScoreboard()).getHandle(), player.getName());
            teamScore.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
            teamScore.getPlayers().add(getName().getString());
            setCustomNameVisible(player.isCustomNameVisible());
            SyncUtil.later(() -> {
                PacketContainer packetContainer = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO_REMOVE);
                packetContainer.getUUIDLists().write(0, Collections.singletonList(uuid));
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packetContainer);
            }, 1750);
        }*/
    }

    public void sendPacketPlayOutEntityMetadata(Player player) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getModifier().writeDefaults();
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
    }

    public void showToAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(this::showTo);
    }

    public void setLocation(Location location) {
        teleportTo((ServerLevel) getCamera().level(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    private void parseEntity(Player player) {
        try {
            Field nameField = GameProfile.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(getGameProfile(), ChatUtil.parse(player, getName().getString()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            getGameProfile().getProperties().put("textures", getGameProfile().getProperties().get("textures").iterator().next());
        } catch (Exception ignore) {
        }
    }

    public Location getLocation() {
        double x = getX();
        double y = getY();
        double z = getZ();
        return new Location(world.getWorld(), x, y, z);
    }

    public UUID getUUID() {
        return getGameProfile().getId();
    }

}
