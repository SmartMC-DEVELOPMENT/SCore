package us.smartmc.backend.connection.manager;

import us.smartmc.backend.instance.player.PlayerCache;

import java.io.FilterInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerCacheManager {

    private static byte lastType;
    private static final Map<UUID, Long> startDate = new HashMap<>();
    FilterInputStream in = null;
    private static final Map<UUID, PlayerCache> playerCaches = new HashMap<>();

    private static long redisCount, backendCount, totalRequests;

    public static void registerStartDate(int type, UUID uuid) {
        lastType = (byte) type;
        startDate.put(uuid, System.currentTimeMillis());
    }

    public static void parse(PlayerCache cache) {
        long millis = System.currentTimeMillis();
        long start = startDate.get(cache.getPlayerId());
        long retardo = (millis - start);

        if (lastType == (byte) 1) {
            String builder = "\n" +
                    "REDIS " + redisCount + " | BACKEND " + backendCount + "\n" +
                    "Comparación Simple=" + calcularRapidezInferior() + "\n" +
                    "Proporción de Rapidez=" + calcularProporcionRapidez() + "\n" +
                    "Diferencia Relativa=" + calcularDiferenciaRelativa() +
                    "\n";
            System.out.println(builder);
        }
        if (lastType == 1) redisCount += retardo; else backendCount += retardo;
    }

    public static double calcularDiferenciaRelativa() {
        double min = Math.min(redisCount, backendCount);
        double max = Math.max(redisCount, backendCount);
        return ((max - min) / max) * 100; // Devuelve el porcentaje de diferencia entre el valor máximo y el mínimo
    }

    public static double calcularProporcionRapidez() {
        double min = Math.min(redisCount, backendCount);
        double max = Math.max(redisCount, backendCount);
        return (min / max) * 100; // Devuelve el porcentaje del valor mínimo en comparación con el valor máximo
    }

    public static double calcularRapidezInferior() {
        return Math.min(redisCount, backendCount);
    }

    public static PlayerCache get(UUID uuid) {
        return playerCaches.get(uuid);
    }
}
