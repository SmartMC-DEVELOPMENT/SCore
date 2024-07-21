package us.smartmc.npcsmodule.instance;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import me.imsergioh.pluginsapi.SpigotPluginsAPI;
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
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import us.smartmc.core.SmartCore;
import us.smartmc.npcsmodule.manager.NPCManager;
import us.smartmc.npcsmodule.util.ConfigUtil;

import java.awt.print.Paper;
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
    public final ServerLevel world;

    @Getter
    private final ServerPlayer npcPlayer;

    @Getter
    private Location bukkitLocation;

    @Getter
    private final Document configData;
    private String skinValue, skinSignature;

    private NPCGravitySimulationTask gravityTask;

    private final Set<UUID> viewers = new HashSet<>();

    public CustomNPC(NPCManager manager, ServerLevel world, String configId, String name, Document configData) {
        this.manager = manager;
        this.npcPlayer = new ServerPlayer(server, world, new GameProfile(UUID.randomUUID(), name), ClientInformation.createDefault());
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
        NPCManager.getManagers().stream().filter(m -> m.get(npcPlayer.getGameProfile().getName()) != null).forEach(manager -> {
            manager.getConfig().put(configId, configData);
            manager.getConfig().save();
        });
    }

    public void setNameVisible(boolean active) {
        npcPlayer.setCustomNameVisible(active);
        configData.put("nameVisible", active);
        NPCManager.getManagers().stream().filter(m -> m.get(npcPlayer.getGameProfile().getName()) != null).forEach(manager -> {
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
        if (viewers.contains(player.getUniqueId())) return;
        viewers.add(player.getUniqueId());
        player.hideEntity(SmartCore.getPlugin(), getBukkitEntity());
        parseEntity(player);
        player.sendMessage("Show to " + npcPlayer.displayName + " " + npcPlayer.getBukkitEntity().getUniqueId());
        npcPlayer.setCustomNameVisible(configData.getBoolean("nameVisible", true));
        npcPlayer.getBukkitEntity().setCustomNameVisible(configData.getBoolean("nameVisible", true));

        // Info Update Packet
        PacketContainer infoUpdatePacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
        infoUpdatePacket.getPlayerInfoActions().write(0, EnumSet.of(EnumWrappers.PlayerInfoAction.ADD_PLAYER));

        WrappedGameProfile profile = new WrappedGameProfile(npcPlayer.getUUID(), npcPlayer.getName().getString());
        ((GameProfile) profile.getHandle()).getProperties().putAll(npcPlayer.getGameProfile().getProperties());
        PlayerInfoData playerInfoData = new PlayerInfoData(profile,
                0, EnumWrappers.NativeGameMode.SURVIVAL, WrappedChatComponent.fromText(npcPlayer.getName().getString()));
        infoUpdatePacket.getPlayerInfoDataLists().write(1, Collections.singletonList(playerInfoData));

        // Add Entity Packet
        PacketContainer addEntityPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        addEntityPacket.getModifier().writeDefaults();
        addEntityPacket.getEntityTypeModifier().write(0, EntityType.PLAYER);
        addEntityPacket.getIntegers().write(0, npcPlayer.getId());
        addEntityPacket.getUUIDs().write(0, npcPlayer.getGameProfile().getId());
        addEntityPacket.getDoubles().write(0, npcPlayer.getX());
        addEntityPacket.getDoubles().write(1, npcPlayer.getY());
        addEntityPacket.getDoubles().write(2, npcPlayer.getZ());

        // Head rotate
        PacketContainer headRotationPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
        headRotationPacket.getModifier().writeDefaults();
        headRotationPacket.getIntegers().write(0, npcPlayer.getId());
        headRotationPacket.getBytes().write(0, ((byte) (int) (getBukkitLocation().getYaw() * 256.0F / 360.0F)));

        // Body rotate
        PacketContainer lookPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_LOOK);
        lookPacket.getModifier().writeDefaults();
        lookPacket.getIntegers().write(0, npcPlayer.getId());
        lookPacket.getBytes().write(0, ((byte) (int) (getBukkitLocation().getYaw() * 256.0F / 360.0F)));
        lookPacket.getBytes().write(1, ((byte) (int) (getBukkitLocation().getPitch() * 256.0F / 360.0F)));

        // Metadata Packet
        PacketContainer metadataPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
        metadataPacket.getIntegers().write(0, npcPlayer.getId());

        WrappedDataWatcher watcher = new WrappedDataWatcher(npcPlayer.getBukkitEntity());
        List<WrappedWatchableObject> watchableObjects = watcher.getWatchableObjects();
        metadataPacket.getWatchableCollectionModifier().write(0, watchableObjects);

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, infoUpdatePacket);
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, addEntityPacket);
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, metadataPacket);
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, headRotationPacket);
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, lookPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bukkit.getScheduler().runTaskLater(SpigotPluginsAPI.getPlugin(), () -> {
            hideTagByPlayerScoreboard(player);
        }, 6);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                removeViewer(player);
            }
        }, 1000);
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

    public void teleportTo(Location location) {
        npcPlayer.moveTo(location.getX(), location.getY(), location.getZ());
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (!player.canSee(npcPlayer.getBukkitEntity())) return;
            updateNPCPositionToPlayer(player, npcPlayer.getBukkitEntity().getLocation(),
                    npcPlayer.onGround);
        });
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
        if (skinValue != null && skinSignature != null) {
            setSkin(skinValue, skinSignature);
        }
    }

    public void setSkin(String value, String signature) {
        skinValue = value;
        skinSignature = signature;
        npcPlayer.getGameProfile().getProperties().removeAll("textures");
        npcPlayer.getGameProfile().getProperties().put("textures", new Property("textures", skinValue, skinSignature));
        configData.put("skinValue", skinValue);
        configData.put("skinSignature", skinSignature);
        manager.getConfig().put(configId, configData);
        manager.getConfig().save();
    }

    public void simulateAttack() {
        World bukkitWorld = getBukkitLocation().getWorld();
        if (bukkitWorld == null) return;
        bukkitWorld.getPlayers().iterator().forEachRemaining(player -> {
            if (!player.canSee(npcPlayer.getBukkitEntity())) return;
            simulateAttackFor(player);
        });

    }

    public void resetGravitySimulationTask() {
        gravityTask = null;
    }


    // Arreglar metodo para que el daño funcione correctamente (Si hago doble hit seguido no respeta el anterior hit)
    private void simulateAttackFor(Player player) {
        // Calcular la dirección del knockback
        Vector direction = npcPlayer.getBukkitEntity().getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
        double knockbackStrength = 0.55; // Puedes ajustar la fuerza del knockback según sea necesario

        // Aplicar el daño y la animación de daño
        PacketContainer damagePacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.DAMAGE_EVENT);
        damagePacket.getIntegers().write(0, npcPlayer.getId());
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, damagePacket);

        // Aplicar el knockback
        npcPlayer.setDeltaMovement(direction.getX() * knockbackStrength, .6, direction.getZ() * knockbackStrength);

        // Actualizar la posición del NPC para reflejar el knockback
        npcPlayer.move(MoverType.SELF, new Vec3(npcPlayer.getDeltaMovement().x, npcPlayer.getDeltaMovement().y, npcPlayer.getDeltaMovement().z));

        updateNPCPositionToPlayer(player, npcPlayer.getBukkitEntity().getLocation(), npcPlayer.onGround);
        player.playSound(npcPlayer.getBukkitEntity().getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 1);

        if (gravityTask == null) {
            gravityTask = new NPCGravitySimulationTask(this);
            new Thread(gravityTask).start();
        }
    }

    private void updateNPCPositionToPlayer(Player player, Location location, boolean onGround) {
        // Crear y enviar el paquete de posición del NPC
        PacketContainer positionPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_TELEPORT);
        positionPacket.getIntegers().write(0, npcPlayer.getId());
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
        double x = npcPlayer.getX();
        double y = npcPlayer.getY();
        double z = npcPlayer.getZ();
        return new Location(world.getWorld(), x, y, z);
    }

    public String getName() {
        return npcPlayer.getGameProfile().getName();
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

    public CraftPlayer getBukkitEntity() {
        return npcPlayer.getBukkitEntity();
    }

    public void setBukkitLocation(Location bukkitLocation) {
        this.bukkitLocation = bukkitLocation;
        updateNMSLocation(bukkitLocation);
    }
}
