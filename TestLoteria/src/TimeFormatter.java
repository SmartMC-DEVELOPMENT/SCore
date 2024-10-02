import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TimeFormatter {

    public static GregorianCalendar convertirADate(String fechaString) {
        // Define el formato de la fecha que estás tratando de parsear
        SimpleDateFormat formato = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);

        try {
            // Parsear la cadena a un objeto Date
            Date fecha = formato.parse(fechaString);

            // Crear un objeto GregorianCalendar a partir del objeto Date
            GregorianCalendar calendario = new GregorianCalendar();
            calendario.setTime(fecha);

            return calendario;
        } catch (ParseException e) {
            // Manejo de excepciones si la fecha no se puede parsear
            e.printStackTrace();
            return null; // o lanzar una excepción personalizada
        }
    }

    public static String formatSeedToTime(long seed) {
        // Crear un objeto Date a partir del seed (en milisegundos)
        Date date = new Date(seed);

        // Formato de la hora (HH:mm:ss.SSS)
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS yyyy/MM/dd");

        // Retornar la hora formateada
        return formatter.format(date);
    }
}