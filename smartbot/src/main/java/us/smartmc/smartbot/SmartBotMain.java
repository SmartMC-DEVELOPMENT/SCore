package us.smartmc.smartbot;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import us.smartmc.smartbot.connection.MongoDBConnection;
import us.smartmc.smartbot.connection.RedisConnection;
import us.smartmc.smartbot.handler.CommandHandler;
import us.smartmc.smartbot.handler.EventSchedulerHandler;
import us.smartmc.smartbot.handler.RepliesHandler;
import us.smartmc.smartbot.listener.*;
import us.smartmc.smartbot.manager.AutoRoleManager;
import us.smartmc.smartbot.manager.LogsManager;
import us.smartmc.smartbot.slashcommand.AnuncioCommand;
import us.smartmc.smartbot.slashcommand.JoinToCommand;
import us.smartmc.smartbot.slashcommand.TiendaCommand;
import us.smartmc.smartbot.textcommand.TestCommand;

import java.util.*;

public class SmartBotMain {

    private static final Dotenv dotenv = Dotenv.load();
    private static JDA api;

    private static final Set<String>
            allowed_guilds =new HashSet<>(
            Arrays.asList(
                    "1109545191796391938",
                    "1078252707506298930"
            ));

    private static LogsManager logsManager;

    public static void main(String[] args) {
        api = JDABuilder.createDefault(dotenv.get("TOKEN"))
                .addEventListeners(
                        new DefaultUserRoleListener(),
                        new CommandHandlerListener(),
                        new RepliesHandler(),
                        new SuggestionListener(),
                        new ChatGPTListener(),
                        new AutoRoleListeners())
                .enableIntents(
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS,
                        GatewayIntent.DIRECT_MESSAGES)
                .build();
        api.getPresence().setActivity(Activity.of(Activity.ActivityType.WATCHING, "Administración administrando lo no administrable"));

        MongoDBConnection.mainConnection = new MongoDBConnection("localhost", 27017);
        RedisConnection.mainConnection = new RedisConnection("localhost", 6379);

        CommandHandler.clearCommands();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                CommandHandler.register(
                        new TiendaCommand("tienda"),
                        new TestCommand("/abrir-servidor"),
                        new AnuncioCommand("sb-anuncio"),
                        new AnuncioCommand("anuncio"),
                        new JoinToCommand("jointo"));
                AutoRoleManager.loadAutoRolesFromGuild(getMainGuildID());
            }
        }, 1000);
        EventSchedulerHandler.setup();



    }

    public static LogsManager getLogsManager() {
        return logsManager;
    }

    public static Guild getMainGuild() {
        return api.getGuildById(getMainGuildID());
    }

    public static String getMainGuildID() {
        return Objects.requireNonNull(dotenv.get("MAIN_GUILD"));
    }

    public static boolean isAllowedGuild(Guild guild) {
        return allowed_guilds.contains(guild.getId());
    }

    public static Dotenv getDotenv() {
        return dotenv;
    }

    public static JDA getJDA() {
        return api;
    }
}
