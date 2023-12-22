package us.smartmc.npcsmodule.instance;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;;
import us.smartmc.core.pluginsapi.util.ChatUtil;
import us.smartmc.core.pluginsapi.util.SyncUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.*;

public class NPC {

    private static final MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
    private static final Set<String> namesToHide = new HashSet<>();

    private final String name;
    private final WorldServer worldServer;
    private final EntityPlayer entityPlayer;
    private final GameProfile gameProfile;
    private final PlayerInteractManager manager;
    private final NPCHologramManager hologramManager;

    private List<String> lines;
    private List<String> commandLines = new ArrayList<>();

    public NPC(World world, String name, String skinValue, String skinSignature) {
        this.name = name;
        this.worldServer = ((CraftWorld) world).getHandle();
        this.gameProfile = new GameProfile(UUID.randomUUID(), name);
        this.manager = new PlayerInteractManager(worldServer);
        entityPlayer = new EntityPlayer(server, worldServer, gameProfile, manager);
        // SET SKIN VALUE & SIGNATURE IF NOT NULL BOTH STRINGS
        if (skinValue != null && skinSignature != null) setSkinValue(skinValue, skinSignature);
        hologramManager = new NPCHologramManager(this);
    }

    public NPC(World world, String name) {
        this(world, name, null, null);
    }

    public void setNameVisible(boolean active) {
        entityPlayer.setCustomNameVisible(active);
    }

    public void setCommandLines(List<String> commandLines) {
        this.commandLines = commandLines;
    }

    public List<String> getCommandLines() {
        return commandLines;
    }

    public void setLines(List<String> list) {
        this.lines = list;
        hologramManager.setupStands();
    }

    public void setSkinValue(String value, String signature) {
        gameProfile.getProperties().put("textures", new Property("textures", value, signature));
    }

    public void showTo(Player player) {
        EntityPlayer ep = parseEntity(player);
        entityPlayer.displayName = "";
        ep.displayName = entityPlayer.displayName;

        // LOCATION
        ep.setLocation(entityPlayer.locX, entityPlayer.locY, entityPlayer.locZ,
                entityPlayer.yaw, entityPlayer.pitch);
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

        // SHOW ENTITY WITH CORRECT DATA

        connection.sendPacket(new PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
                ep));

        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(ep));

        // REMOVE FROM TAB:
        connection.sendPacket(new PacketPlayOutEntityMetadata(ep.getId(), ep.getDataWatcher(), true));
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(ep, (byte) ((ep.yaw * 256.0F) / 360.0F)));

        // NAME VISIBLE:
        if (!player.isCustomNameVisible()) {
            hologramManager.createHologram(player);
            ScoreboardTeam teamScore = new ScoreboardTeam(((CraftPlayer) player).getHandle().getScoreboard(), player.getName());
            teamScore.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
            teamScore.getPlayerNameSet().add(entityPlayer.getName());
            connection.sendPacket(new PacketPlayOutScoreboardTeam(teamScore, 1));
            connection.sendPacket(new PacketPlayOutScoreboardTeam(teamScore, 0));
            namesToHide.add(entityPlayer.getName());

            connection.sendPacket(new PacketPlayOutPlayerInfo(
                    PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME,
                    ep));

            connection.sendPacket(new PacketPlayOutScoreboardTeam(teamScore, new ArrayList<String>() {{
                addAll(namesToHide);
            }}, 3));

            SyncUtil.later(() -> {
                connection.sendPacket(new PacketPlayOutPlayerInfo(
                        PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
                        ep));
            }, 1750);
        }
    }

    public void showToAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(this::showTo);
    }

    public void setLocation(Location location) {
        entityPlayer.world = ((CraftWorld) Bukkit.getWorld(location.getWorld().getName())).getHandle();
        entityPlayer.setLocation(location.getX(), location.getY(), location.getZ(),
                location.getYaw(), location.getYaw());
        entityPlayer.yaw = location.getYaw();
        entityPlayer.pitch = location.getPitch();
    }

    private EntityPlayer parseEntity(Player player) {
        try {
            Field nameField = GameProfile.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(entityPlayer.getProfile(), ChatUtil.parse(player, name));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            entityPlayer.getProfile().getProperties().put("textures", gameProfile.getProperties().get("textures").iterator().next());
        } catch (Exception ignore) {
        }
        entityPlayer.getDataWatcher().watch(10, (byte) 127);
        return entityPlayer;
    }

    public Location getLocation() {
        double x = entityPlayer.locX;
        double y = entityPlayer.locY;
        double z = entityPlayer.locZ;
        return new Location(Bukkit.getWorld(entityPlayer.world.worldData.getName()), x, y, z);
    }

    public List<String> getLines() {
        return lines;
    }

    public UUID getUUID() {
        return gameProfile.getId();
    }

    private static WorldServer worldServer(World world) {
        return ((CraftWorld) world).getHandle();
    }

    public String getName() {
        return name;
    }

    public EntityPlayer getEntityPlayer() {
        return entityPlayer;
    }
}
