
import java.io.IOException;
import java.util.*;

public class EuromillonesGenerator {

    private static final int day = 19;
    private static final int month = 9;
    private static final int year = 2024;

    private static final Map<String, Integer> times = new HashMap<>();

    public static void main(String[] args) throws IOException {
        generate(new GregorianCalendar(year, month - 1, day));
        System.out.println("SAVED! " + times.size());
        List<Map.Entry<String, Integer>> top10 = obtenerTop10(times);
        System.out.println("Top 10 equipos con los tiempos más altos:");
        for (Map.Entry<String, Integer> entry : top10) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public static List<Map.Entry<String, Integer>> obtenerTop10(Map<String, Integer> times) {
        // Convertir el Map a una lista de entradas
        List<Map.Entry<String, Integer>> lista = new ArrayList<>(times.entrySet());

        // Ordenar la lista por valores en orden descendente
        lista.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Obtener los primeros 10 elementos
        return lista.subList(0, Math.min(10, lista.size()));
    }

    public static void imprimirTopInferior(Map<String, Integer> times, int limite, int limitResults) {
        System.out.println("Equipos con tiempos menores a " + limite + ":");

        int foundResults = 0;
        for (Map.Entry<String, Integer> entry : times.entrySet()) {
            if (foundResults >= limitResults) break;
            if (entry.getValue() < limite) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
                foundResults++;
            }
        }
    }

    public static String buscarEntradas(int[] entradas) {
        return String.valueOf(times.getOrDefault(Arrays.toString(entradas), 0));
    }


    private static void generate(GregorianCalendar date) {
        Calendar start = (Calendar) date.clone();
        start.set(Calendar.HOUR_OF_DAY, 19);
        start.set(Calendar.MINUTE, 0);

        Calendar end = (Calendar) date.clone();
        end.set(Calendar.HOUR_OF_DAY, 20);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);


        // Mientras la hora actual sea menor que las 21:50
        while (start.before(end)) {

            long seed = start.getTimeInMillis();
            Random random = new Random(seed);

            int[] numbers = new int[6];
            for (int i = 0; i < 6; i++) {
                numbers[i] = random.nextInt(40) + 1;
            }

            int count = times.getOrDefault(Arrays.toString(numbers), 0) + 1;
            times.put(Arrays.toString(numbers), count);

            //System.out.println(Arrays.toString(numbers));

            // Aumentar en 1 milisegundo
            start.add(Calendar.MILLISECOND, 1);
        }
    }

    // Función para comprobar si la combinación generada coincide con los números premiados
    private static boolean checkMatch(int[] numbers, int[] searchFor) {
        int[] searchNumbers = Arrays.copyOfRange(searchFor, 0, 6);

        Arrays.sort(numbers);

        Arrays.sort(searchNumbers);

        return Arrays.equals(numbers, searchNumbers);
    }

}