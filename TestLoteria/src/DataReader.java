import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataReader {

    public static List<LotteryResult> getResults(String file) {
        List<LotteryResult> results = new ArrayList<>();
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(file)) {
            // Definir el tipo de la lista de objetos
            Type lotteryListType = new TypeToken<List<LotteryResult>>(){}.getType();

            // Leer el archivo y convertir el JSON en una lista de objetos LotteryResult
            return gson.fromJson(reader, lotteryListType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        } finally {
            reader.close();
        }
    }

}
