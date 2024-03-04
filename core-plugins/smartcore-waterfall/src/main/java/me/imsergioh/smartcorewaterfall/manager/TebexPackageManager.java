package me.imsergioh.smartcorewaterfall.manager;
import me.imsergioh.pluginsapi.instance.MongoDBPluginConfig;
import org.bson.Document;
import org.json.JSONArray;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
        // Cliente HTTP
        HttpClient client = HttpClient.newHttpClient();

        // Construir la solicitud
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://plugin.tebex.io/packages"))
                .header("X-Tebex-Secret", secretKey) // Asignar la clave secreta en el encabezado
                .GET() // Método GET
                .build();

        try {
            // Realizar la solicitud y obtener la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Convertir la respuesta JSON a una lista de Document de BSON
            JSONArray jsonArray = new JSONArray(response.body());
            List<Document> documents = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                documents.add(Document.parse(jsonArray.getJSONObject(i).toString()));
            }
            return documents;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

}
