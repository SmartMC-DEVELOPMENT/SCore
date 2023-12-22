package us.smartmc.smartbot.util;

import org.json.JSONArray;
import org.json.JSONObject;
import us.smartmc.smartbot.SmartBotMain;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class ChatGPTUtil {

    public static CompletableFuture<String> callGPT(String prompt, String content) throws IOException, InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        // Configura el cliente HTTP
        CompletableFuture.runAsync(() -> {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(120))
                    .build();

            // Construye el cuerpo de la solicitud
            JSONObject requestJSON = new JSONObject();
            requestJSON.put("model", "gpt-3.5-turbo");

            JSONArray messages = new JSONArray();

            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", "You are a chatbot. " + content);

            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);

            messages.put(systemMessage);
            messages.put(userMessage);

            requestJSON.put("messages", messages);

            String requestBody = requestJSON.toString();

            // Construye la solicitud HTTP
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .timeout(Duration.ofSeconds(120))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + SmartBotMain.getDotenv().get("CHATGPT_KEY"))
                    .header("OpenAI-Organization", "org-jvrgPAeemEcKUksBXyS2Jeue")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // Realiza la solicitud y obtén la respuesta
            HttpResponse<String> response = null;
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String jsonData = response.body();

            // Parsear el JSON para obtener el texto generado
            JSONObject jsonObject = new JSONObject(jsonData);
            String textoGenerado = jsonObject.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
            completableFuture.complete(textoGenerado);
        });
        return completableFuture;
    }

}
