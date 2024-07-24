package us.smartmc.npcsmodule.instance;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.pluginsapi.util.LegacyChatUtil;
import me.imsergioh.pluginsapi.util.SyncUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;
import us.smartmc.core.SmartCore;
import us.smartmc.npcsmodule.manager.NPCManager;
import us.smartmc.npcsmodule.util.ConfigUtil;

import java.lang.reflect.Field;
import java.util.*;

public class CustomNPC {

    private static final MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
    private static final String HIDE_TAGS_TEAM_NAME = "ht-" + new Random().nextInt(9999);

    @Getter
    private List<String> commandLines = new ArrayList<>();

    private final NPCManager manager;
    @Getter
    private final String configId;
    public final World world;
    private final GameProfile gameProfile;

    @Getter
    private final EntityPlayer entityPlayer;

    @Getter
    private Location bukkitLocation;

    @Getter
    private final Document configData;
    private String skinValue, skinSignature;

    private NPCGravitySimulationTask gravityTask;

    private final Set<UUID> viewers = new HashSet<>();

    public CustomNPC(NPCManager manager, World world, String configId, String name, Document configData) {
        this.manager = manager;
        WorldServer worldServer = ((CraftWorld) world).getHandle();
        this.gameProfile = new GameProfile(UUID.randomUUID(), name);
        entityPlayer = new EntityPlayer(server, worldServer, gameProfile, new PlayerInteractManager(worldServer));
        entityPlayer.playerConnection = new PlayerConnection(worldServer.getMinecraftServer(), new NetworkManager(null), entityPlayer);
        this.configId = configId;
        this.configData = configData;
        // SET SKIN VALUE & SIGNATURE IF NOT NULL BOTH STRINGS
        this.world = world;
    }

    public boolean toggleVulnerability() {
        boolean toggled = !isVulnerable();
        configData.put("vulnerable", toggled);
        return toggled;
    }

    public void updateLocation(Location location) {
        setBukkitLocation(location);
        updateNMSLocation(location);
        configData.put("location", ConfigUtil.getLocationString(bukkitLocation, " "));
        NPCManager.getManagers().stream().filter(m -> m.get(entityPlayer.getProfile().getName()) != null).forEach(manager -> {
            manager.getConfig().put(configId, configData);
            manager.getConfig().save();
        });
    }

    public void setNameVisible(boolean active) {
        entityPlayer.setCustomNameVisible(active);
        configData.put("nameVisible", active);
        NPCManager.getManagers().stream().filter(m -> m.get(entityPlayer.getProfile().getName()) != null).forEach(manager -> {
            manager.getConfig().put(configId, configData);
            manager.getConfig().save();
        });
    }

    public boolean isNameVisible() {
        return configData.getBoolean("nameVisible");
    }

    public void setCommandLines(List<String> commandLines) {
        this.commandLines = commandLines;
    }

    public void removeViewer(Player player) {
        viewers.remove(player.getUniqueId());
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
            ScoreboardTeam teamScore = new ScoreboardTeam(((CraftPlayer) player).getHandle().getScoreboard(), player.getName());
            teamScore.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
            teamScore.getPlayerNameSet().add(entityPlayer.getName());
            connection.sendPacket(new PacketPlayOutScoreboardTeam(teamScore, 1));
            connection.sendPacket(new PacketPlayOutScoreboardTeam(teamScore, 0));

            connection.sendPacket(new PacketPlayOutPlayerInfo(
                    PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME,
                    ep));

            connection.sendPacket(new PacketPlayOutScoreboardTeam(teamScore, new ArrayList<String>() {{
                add(entityPlayer.getName());
            }}, 3));

            SyncUtil.later(() -> {
                connection.sendPacket(new PacketPlayOutPlayerInfo(
                        PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
                        ep));
            }, 1750);
        }
    }

    public void hideTagByPlayerScoreboard(Player player) {
        if (!entityPlayer.getBukkitEntity().isCustomNameVisible()) {
            Scoreboard scoreboard = player.getScoreboard();
            Team team = scoreboard.getTeam(HIDE_TAGS_TEAM_NAME);
            if (team == null) team = scoreboard.registerNewTeam(HIDE_TAGS_TEAM_NAME);
            team.addEntry(entityPlayer.getBukkitEntity().getName());
        }
    }

    public void teleportTo(Location location) {
        entityPlayer.move(location.getX(), location.getY(), location.getZ());
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (!player.canSee(entityPlayer.getBukkitEntity())) return;
            updateNPCPositionToPlayer(player, entityPlayer.getBukkitEntity().getLocation(),
                    entityPlayer.onGround);
        });
    }

    private void updateNMSLocation(Location loc) {
        entityPlayer.setPosition(loc.getX(), loc.getY(), loc.getZ());
        entityPlayer.teleportTo(loc, false);
    }

    private EntityPlayer parseEntity(Player player) {
        try {
            Field nameField = GameProfile.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(entityPlayer.getProfile(), ChatUtil.parse(player, entityPlayer.getName()));
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

    public void setSkin(String value, String signature) {
        skinValue = value;
        skinSignature = signature;
        entityPlayer.getProfile().getProperties().removeAll("textures");
        entityPlayer.getProfile().getProperties().put("textures", new Property("textures", skinValue, skinSignature));
        configData.put("skinValue", skinValue);
        configData.put("skinSignature", skinSignature);
        manager.getConfig().put(configId, configData);
        manager.getConfig().save();
    }

    public void simulateAttack() {
        World bukkitWorld = getBukkitLocation().getWorld();
        if (bukkitWorld == null) return;
        bukkitWorld.getPlayers().iterator().forEachRemaining(player -> {
            if (!player.canSee(entityPlayer.getBukkitEntity())) return;
            simulateAttackFor(player);
        });

    }

    public void resetGravitySimulationTask() {
        gravityTask = null;
    }


    // Arreglar metodo para que el daño funcione correctamente (Si hago doble hit seguido no respeta el anterior hit)
    private void simulateAttackFor(Player player) {
        // Calcular la dirección del knockback
        Vector direction = entityPlayer.getBukkitEntity().getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
        double knockbackStrength = 0.55; // Puedes ajustar la fuerza del knockback según sea necesario

        // Aplicar el daño y la animación de daño
        PacketContainer damagePacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.DAMAGE_EVENT);
        damagePacket.getIntegers().write(0, entityPlayer.getId());
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, damagePacket);

        // Aplicar el knockback
        entityPlayer.move(direction.getX() * knockbackStrength, .6, direction.getZ() * knockbackStrength);

        // Actualizar la posición del NPC para reflejar el knockback
        entityPlayer.move(entityPlayer.locX, entityPlayer.locY, entityPlayer.locZ);

        updateNPCPositionToPlayer(player, entityPlayer.getBukkitEntity().getLocation(), entityPlayer.onGround);
        //player.playSound(entityPlayer.getBukkitEntity().getLocation(), Sound., 1, 1);

        if (gravityTask == null) {
            gravityTask = new NPCGravitySimulationTask(this);
            new Thread(gravityTask).start();
        }
    }

    private void updateNPCPositionToPlayer(Player player, Location location, boolean onGround) {
        // Crear y enviar el paquete de posición del NPC
        PacketContainer positionPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_TELEPORT);
        positionPacket.getIntegers().write(0, entityPlayer.getId());
        positionPacket.getDoubles().write(0, location.getX());
        positionPacket.getDoubles().write(1, location.getY());
        positionPacket.getDoubles().write(2, location.getZ());

        positionPacket.getBytes().write(0, (byte) (location.getYaw() * 256.0F / 360.0F));
        positionPacket.getBytes().write(0, (byte) (location.getPitch() * 256.0F / 360.0F));

        positionPacket.getBooleans().write(0, onGround);

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, positionPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isVulnerable() {
        return configData.getBoolean("vulnerable", false);
    }

    public Location getLocation() {
        double x = entityPlayer.locX;
        double y = entityPlayer.locY;
        double z = entityPlayer.locZ;
        return new Location(world, x, y, z);
    }

    public String getName() {
        return entityPlayer.getProfile().getName();
    }

    public UUID getUUID() {
        return entityPlayer.getProfile().getId();
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

    public CraftPlayer getBukkitEntity() {
        return entityPlayer.getBukkitEntity();
    }

    public void setBukkitLocation(Location bukkitLocation) {
        this.bukkitLocation = bukkitLocation;
        updateNMSLocation(bukkitLocation);
    }
}
