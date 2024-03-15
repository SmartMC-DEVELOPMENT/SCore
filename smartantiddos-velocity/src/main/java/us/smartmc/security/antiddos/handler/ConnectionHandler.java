package us.smartmc.security.antiddos.handler;

import com.velocitypowered.api.proxy.InboundConnection;
import com.velocitypowered.api.proxy.Player;
import us.smartmc.security.antiddos.SmartAntiDDoS;
import us.smartmc.security.antiddos.instance.PlayerConnection;
import net.kyori.adventure.text.Component;

import java.util.*;

public class ConnectionHandler {

    private static int trafficAmount = 0;
    private static int limitTrafficAmount = 72;

    private static boolean underAttack = false;

    private static Timer timer;

    private static HashMap<UUID, PlayerConnection> playerConnections = new HashMap<>();
    private static Set<String> allowed = new HashSet<>();

    private static Set<UUID> notifications = new HashSet<>();

    public static void toggleNotifications(Player player) {
        if (!hasActiveNotifications(player)) {
            notifications.add(player.getUniqueId());
            player.sendMessage(Component.text("¡Notificaciones ON!"));
        } else {
            notifications.remove(player.getUniqueId());
            player.sendMessage(Component.text("¡Notificaciones OFF!"));
        }
    }

    public static void sendNotification(String notificationMessage) {
        notificationMessage = notificationMessage.replace("&", "§");
        for (Player player : SmartAntiDDoS.getProxy().getAllPlayers()) {
            if (hasActiveNotifications(player)) {
                player.sendMessage(Component.text(notificationMessage));
            }
        }
        System.out.println(notificationMessage);
    }

    public static boolean hasActiveNotifications(Player player) {
        return notifications.contains(player.getUniqueId());
    }

    public static boolean isAllowed(InboundConnection connection) {
        return allowed.contains(connection.getRemoteAddress().getAddress().toString());
    }

    public static void allowConnection(String key) {
        allowed.add(key);
    }

    public static void registerPing(String stringKey) {
        System.out.println("Registering " + stringKey);
    }

    public static void setup() {
        if (timer != null) {
            return;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (trafficAmount >= limitTrafficAmount) {
                    if (!underAttack) {
                        underAttack = true;
                        broadcastAntiDDoSStatus("&c[FinalAntiDDoS]&f Nuevo ataque detectado! (Recibiendo actúalmente " + trafficAmount + " paquetes de tráfico, se he activado modo el protección)");
                    } else {
                        // ALREADY ON ATTACK
                        sendNotification("&c[FinalAntiDDoS]&f Ataque en curso... Con &4" + trafficAmount + "&f paquetes en los últimos 10 segundos");
                    }
                } else {
                    if (underAttack) {
                        underAttack = false;
                        broadcastAntiDDoSStatus("&c[FinalAntiDDoS]&f El ataque anterior se ha relajado/detenido (Modo protección desactivado)");
                    }

                }
                resetTrafficAmount();
            }
        }, 0, 1000 * 10);
    }

    public static boolean isUnderAttack() {
        return underAttack;
    }

    public static void detectTrafficPacket() {
        trafficAmount++;
    }

    public static void broadcastAntiDDoSStatus(String message) {
        message = message.replace("&", "§");
        for (Player player : SmartAntiDDoS.getProxy().getAllPlayers()) {
            if (player.hasPermission("*")) {
                player.sendMessage(Component.text(message));
            }
        }
        System.out.println(message);
    }

    public static void resetTrafficAmount() {
        trafficAmount = 0;
    }

    public static void registerPlayerConnection(Player player) {
        playerConnections.putIfAbsent(player.getUniqueId(), new PlayerConnection(player));
    }

    public static void unregisterPlayerConnection(Player player) {
        playerConnections.remove(player.getUniqueId());
    }

    public static PlayerConnection get(Player player) {
        return playerConnections.get(player.getUniqueId());
    }

}
