package us.smartmc.smartbot.util;

import org.json.JSONArray;
import org.json.JSONObject;
import us.smartmc.smartbot.SmartBotMain;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class ChatGPTUtil {

    public static CompletableFuture<String> callGPT(final String prompt, final String content) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            HttpURLConnection connection = null;

            try {
                // Configura la URL y abre la conexión
                URL url = new URL("https://api.openai.com/v1/chat/completions");
                connection = (HttpURLConnection) url.openConnection();

                // Configura el método de solicitud y los encabezados
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + SmartBotMain.getDotenv().get("CHATGPT_KEY"));
                connection.setRequestProperty("OpenAI-Organization", "org-jvrgPAeemEcKUksBXyS2Jeue");
                connection.setConnectTimeout(120000);
                connection.setReadTimeout(120000);
                connection.setDoOutput(true);

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

                // Escribe el cuerpo de la solicitud
                try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                    wr.writeBytes(requestBody);
                    wr.flush();
                }

                // Lee la respuesta
                StringBuilder response;
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    response = new StringBuilder();
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                }

                // Parsear el JSON para obtener el texto generado
                JSONObject jsonObject = new JSONObject(response.toString());
                String textoGenerado = jsonObject.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
                completableFuture.complete(textoGenerado);
            } catch (IOException e) {
                completableFuture.completeExceptionally(e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
        return completableFuture;
    }

}
