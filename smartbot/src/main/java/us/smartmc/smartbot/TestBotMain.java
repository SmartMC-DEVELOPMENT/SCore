package us.smartmc.smartbot;

import io.github.cdimascio.dotenv.Dotenv;
import io.mokulu.discord.oauth.DiscordAPI;
import io.mokulu.discord.oauth.DiscordOAuth;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import us.smartmc.smartbot.handler.GuildsHandler;
import us.smartmc.smartbot.manager.LogsManager;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

public class TestBotMain {
    private static JDA api;

    @Getter
    private static LogsManager logsManager;

    public static void main(String[] args) throws IOException, InterruptedException {
        // CREATION BOT >>

        DiscordOAuth oauthHandler = new DiscordOAuth("1162630545746952364", "wXeT_7Xwzyv1M35YaGtKiK3i-6QnNk_C", "http://localhost:4000/auth/discord/callback", new String[]{"email", "identify"});
        String accessToken = "AH4yWNPiaNFqI9RdTZIixsSFzdqIEY";

        // Configurar el token de autenticación
        Unirest.config().setDefaultHeader("Authorization", "Bearer " + accessToken);

        // Hacer una solicitud a la API de Discord para obtener los estados de voz
        HttpResponse<JsonNode> response = Unirest.get("https://discord.com/api/v4/channels/852214710564683817/860229208181833748/")
                .asJson();

        if (response.getStatus() == 200) {
            JSONArray voiceStates = response.getBody().getArray();
            for (int i = 0; i < voiceStates.length(); i++) {
                JSONObject voiceState = voiceStates.getJSONObject(i);
                if (voiceState.getString("user_id").equals("235882085749882890")) {
                    boolean isSelfDeafened = voiceState.getBoolean("self_deaf");
                    if (isSelfDeafened) {
                        System.out.println("You are deafened. You can't hear anyone!");
                    } else {
                        System.out.println("You are not deafened. You can hear others.");
                    }
                    break;
                }
            }
        } else {
            System.out.println("Failed to get voice states: " + response.getStatusText() + " " + response.getStatus());
        }
    }

    private String extractAccessToken(String response) {
        // Simple JSON parsing to extract the access token
        String tokenPrefix = "\"access_token\":\"";
        int startIndex = response.indexOf(tokenPrefix) + tokenPrefix.length();
        int endIndex = response.indexOf("\"", startIndex);
        return response.substring(startIndex, endIndex);
    }

    public static boolean isAllowedGuild(Guild guild) {
        return GuildsHandler.isAllowed(guild.getId());
    }

    public static JDA getJDA() {
        return api;
    }
}
