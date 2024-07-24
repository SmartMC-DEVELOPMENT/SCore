package us.smartmc.smartcore.proxy.manager;
import me.imsergioh.pluginsapi.instance.MongoDBPluginConfig;
import org.bson.Document;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class TebexPackageManager {

    private final MongoDBPluginConfig config = new MongoDBPluginConfig("tebex", "packages_info", new Document("_id", "main"));

    private final Map<String, Document> packages = new HashMap();

    private final String secretKey;

    public TebexPackageManager(String secretKey) {
        this.secretKey = secretKey;
        load();
    }

    public void load() {
        config.load();
        registerNewValues();
    }

    public void registerNewValues() {
        for (Document packageDocument : fetchInfo()) {
            register(packageDocument);
        }
        config.save();
    }

    public Collection<String> getPackageIds() {
        return packages.keySet();
    }

    public Document getPackageDocument(String id) {
        return packages.get(id);
    }

    public void register(Document document) {
        String id = String.valueOf(document.get("id", Object.class));
        String name = document.getString("name");
        String type = document.getString("type");
        int expiryLenght = document.get("expiry_length", Number.class).intValue();
        String expiryPeriod = document.getString("expiry_period");
        Document configDoc = new Document()
                .append("name", name)
                .append("type", type)
                .append("expiry_length", expiryLenght)
                .append("expiry_period", expiryPeriod);
        config.put(id, configDoc);
        packages.put(id, configDoc);
    }

    public List<Document> fetchInfo() {
        String urlString = "https://plugin.tebex.io/packages";
        HttpURLConnection connection = null;
        try {
            // Crear la URL y abrir la conexión
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();

            // Configurar la solicitud HTTP
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Tebex-Secret", secretKey); // Asignar la clave secreta en el encabezado

            // Conectar y obtener la respuesta
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // Verificar si la respuesta es 200 OK
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Convertir la respuesta JSON a una lista de Document de BSON
                JSONArray jsonArray = new JSONArray(response.toString());
                List<Document> documents = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    documents.add(Document.parse(jsonArray.getJSONObject(i).toString()));
                }
                return documents;
            } else {
                System.out.println("Failed to fetch data: HTTP error code : " + responseCode);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
