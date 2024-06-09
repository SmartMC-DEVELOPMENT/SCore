package us.smartmc.game.luckytowers.util;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import us.smartmc.core.SmartCore;
import us.smartmc.core.handler.ScoreboardHandler;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.instance.game.GameSession;
import us.smartmc.game.luckytowers.instance.game.GameSessionStatus;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;
import us.smartmc.game.luckytowers.instance.player.PlayerScoreboardType;
import us.smartmc.game.luckytowers.instance.player.PlayerStatus;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GameUtil {

    public static void cancel(Cancellable e) {
        e.setCancelled(true);
    }

    public static void updateVisibility(Player player) {
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        if (gamePlayer == null) return;
        GameSession session = gamePlayer.getGameSession();

        if (session != null) {
            // In game & is alive
            if (session.getAlivePlayers().contains(gamePlayer)) {
                session.getPlayers().stream().filter(gp -> gp.getStatus().equals(PlayerStatus.SPECTATING)).forEach(spectator -> {
                    Player spectatorPlayer = spectator.getBukkitPlayer();
                    if (spectatorPlayer == null) return;
                    player.hidePlayer(LuckyTowers.getPlugin(), spectatorPlayer);
                });
            } else {
                // In game & Player is not alive
                session.getPlayers().forEach(sessionPlayer -> {
                    Player sessionBukkitPlayer = sessionPlayer.getBukkitPlayer();
                    if (sessionBukkitPlayer == null) return;
                    if (sessionPlayer.getStatus().equals(PlayerStatus.SPECTATING)) {
                        sessionPlayer.getBukkitPlayer().showPlayer(LuckyTowers.getPlugin(), player);
                    } else {
                        sessionPlayer.getBukkitPlayer().hidePlayer(LuckyTowers.getPlugin(), player);
                    }
                });
            }
        } else {
            // Not in game
            for (Player p : Bukkit.getOnlinePlayers()) {
                player.showPlayer(LuckyTowers.getPlugin(), p);
            }
        }
    }

    public static String getFormattedTimeFromSeconds(int initialSeconds) {
        if (initialSeconds <= 0) {
            return String.valueOf(0);
        }

        int seconds = initialSeconds % 60;
        int totalMinutes = initialSeconds / 60;
        int minutes = totalMinutes % 60;
        int totalHours = totalMinutes / 60;
        int hours = totalHours % 24;
        int days = totalHours / 24;

        StringBuilder formattedTime = new StringBuilder();

        if (days > 0) {
            formattedTime.append(days).append("d ");
        }
        if (hours > 0) {
            formattedTime.append(hours).append("h ");
        }
        if (minutes > 0) {
            formattedTime.append(minutes).append("min ");
        }
        if (seconds > 0 || formattedTime.length() == 0) {
            formattedTime.append(seconds).append("s");
        }

        // Trim the final string to remove any extra space at the end
        return formattedTime.toString().trim();
    }


    public static void removeAllEntitiesInRegion(World world, GameSession session) {
        Location location1 = session.getMap().getPos1(world, session.getXAddition());
        Location location2 = session.getMap().getPos2(world, session.getXAddition());

        forEachChunk(location1, location2).forEach(chunk -> {
            for (Entity entity : chunk.getEntities()) {
                if (entity instanceof Player) continue;
                entity.remove();
            }
        });
    }

    public static Collection<Chunk> forEachChunk(Location location1, Location location2) {
        Set<Chunk> chunks = new HashSet<>();

        if (location1 == null || location2 == null) {
            return chunks; // Si alguna localización es nula, retorna una colección vacía
        }

        if (!location1.getWorld().equals(location2.getWorld())) {
            throw new IllegalArgumentException("Las localizaciones deben estar en el mismo mundo");
        }

        World world = location1.getWorld();

        int x1 = location1.getBlockX() >> 4; // Coordenada de chunk X de location1
        int z1 = location1.getBlockZ() >> 4; // Coordenada de chunk Z de location1
        int x2 = location2.getBlockX() >> 4; // Coordenada de chunk X de location2
        int z2 = location2.getBlockZ() >> 4; // Coordenada de chunk Z de location2

        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                chunks.add(world.getChunkAt(x, z));
            }
        }
        return chunks;
    }

    public static Material getRandomMaterial() {
        int lenght = Material.values().length - 1;
        int randomIndex = new Random().nextInt(lenght);
        return Material.values()[randomIndex];
    }

    public static void updateScoreboard(GamePlayer gamePlayer) {
        ScoreboardHandler handler = SmartCore.getPlugin().getScoreboardHandler();
        handler.register(gamePlayer.getBukkitPlayer(), getScoreboardTypeByPlayer(gamePlayer).getId());
    }

    public static PlayerScoreboardType getScoreboardTypeByPlayer(GamePlayer gamePlayer) {
        GameSession session = gamePlayer.getGameSession();
        if (session == null) return PlayerScoreboardType.DEFAULT;
        switch (gamePlayer.getStatus()) {
            case INGAME -> {
                if (session.getStatus().equals(GameSessionStatus.STARTING)) {
                    return PlayerScoreboardType.STARTING;
                }
                if (session.getStatus().equals(GameSessionStatus.WAITING)) {
                    return PlayerScoreboardType.WAITING;
                }

                return PlayerScoreboardType.PLAYING;
            }
            case SPECTATING -> {
                return PlayerScoreboardType.SPECTATING;
            }
            default -> {
                return PlayerScoreboardType.DEFAULT;
            }
        }
    }

}
