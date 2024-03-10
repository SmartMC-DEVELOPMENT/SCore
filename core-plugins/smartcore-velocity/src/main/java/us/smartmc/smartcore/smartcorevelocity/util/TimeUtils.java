package us.smartmc.smartcore.smartcorevelocity.util;

public class TimeUtils {

    private int days, hours, minutes, seconds;

    public void addHours(int amount) {
        hours += amount;
    }

    public void addDays(int amount) {
        days += amount;
    }

    public void addMinutes(int amount) {
        minutes += amount;
    }

    public void addSeconds(int amount) {
        seconds += amount;
    }

    public long addToTimestamp(long timestampMillis) {
        long timeInMillis = ((days * 24L + hours) * 3600L + minutes * 60L + seconds);
        return timestampMillis + timeInMillis;
    }

    public static String toString(long timestampMillis) {
        long restante = (timestampMillis * 1000) - System.currentTimeMillis();

        if (restante <= 0) {
            return "0s"; // Si el tiempo restante es negativo o cero, mostrar "0s".
        }

        long segundos = restante / 1000;
        long minutos = segundos / 60;
        segundos = segundos % 60;
        long horas = minutos / 60;
        minutos = minutos % 60;
        long dias = horas / 24;
        horas = horas % 24;

        StringBuilder builder = new StringBuilder();

        if (dias > 0) {
            builder.append(String.format("%02dd ", dias));
        }

        if (horas > 0) {
            builder.append(String.format("%02dh ", horas));
        }

        if (minutos > 0) {
            builder.append(String.format("%02dm ", minutos));
        }

        builder.append(String.format("%02ds", segundos));

        return builder.toString().trim();
    }

}
