package us.smartmc.smartbot;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import me.imsergioh.pluginsapi.language.EnumMessagesRegistry;
import me.imsergioh.pluginsapi.language.TestMessages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import us.smartmc.backend.connection.BackendClient;
import us.smartmc.smartbot.connection.MongoDBConnection;
import us.smartmc.smartbot.connection.RedisConnection;
import us.smartmc.smartbot.handler.CommandHandler;
import us.smartmc.smartbot.handler.EventSchedulerHandler;
import us.smartmc.smartbot.handler.GuildsHandler;
import us.smartmc.smartbot.handler.RepliesHandler;
import us.smartmc.smartbot.listener.*;
import us.smartmc.smartbot.logfunction.PrintConsoleMessages;
import us.smartmc.smartbot.logfunction.SendEmbedExceptionsMessages;
import us.smartmc.smartbot.logfunction.SendEmbedMessages;
import us.smartmc.smartbot.manager.CustomProxyCommandManager;
import us.smartmc.smartbot.manager.LogsManager;
import us.smartmc.smartbot.message.MainMessages;
import us.smartmc.smartbot.slashcommand.*;
import us.smartmc.smartbot.textcommand.SendMessageCommand;
import us.smartmc.smartbot.textcommand.TestCommand;

import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class SmartBotMain {

    @Getter
    private static final Dotenv dotenv = Dotenv.load();
    private static JDA api;

    @Getter
    private static LogsManager logsManager;

    public static void main(String[] args) throws IOException {
        // CONNECT BACKEND SERVICES >>
        MongoDBConnection.mainConnection = new MongoDBConnection("localhost", 27017);
        RedisConnection.mainConnection = newRedisConnection();
        BackendClient.mainConnection = new BackendClient("localhost", 7723);
        BackendClient.mainConnection.login("discordbot", "MRWORLDWIDEWHENISTEPINTOAROMDALEE");
        new Thread(BackendClient.mainConnection).start();

        EnumMessagesRegistry.registerLanguageHolder(MainMessages.class);

        // CREATION BOT >>
        api = JDABuilder.createDefault(dotenv.get("TOKEN"))
                .addEventListeners(
                        new DefaultUserRoleListener(),
                        new CommandHandlerListener(),
                        new RepliesHandler(),
                        new SuggestionListener(),
                        new ChatGPTListener(),
                        new TicketReactionListener(),
                        new TicketSavementListeners(),
                        new SurveysListener())
                .enableIntents(
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS,
                        GatewayIntent.DIRECT_MESSAGES)
                .build();
        api.getPresence().setActivity(Activity.of(Activity.ActivityType.PLAYING, "play.smartmc.us"));

        // ALLOWED GUILDS >>
        GuildsHandler.register("1109545191796391938",
                "1078252707506298930");

        logsManager = new LogsManager();
        logsManager.register(new PrintConsoleMessages(), new SendEmbedMessages(), new SendEmbedExceptionsMessages());

        new CustomProxyCommandManager();

        CommandHandler.clearCommands();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                CommandHandler.register(
                        new TiendaCommand("tienda"),
                        new TestCommand("/abrir-servidor"),
                        new SendMessageCommand("!sendMessage"),
                        new AnuncioCommand("sb-anuncio"),
                        new AnuncioCommand("anuncio"),
                        new JoinToCommand("jointo"),
                        new ReactToCommand(),
                        new CreateQuoteCommand(),
                        new RemoveQuoteCommand(),
                        new VerifyMinecraftLinkCommand("verifyminecraft"));
            }
        }, 1000);
        EventSchedulerHandler.setup();
    }

    public static RedisConnection newRedisConnection() {
        return new RedisConnection("localhost", 6379);
    }

    public static Guild getMainGuild() {
        return api.getGuildById(getMainGuildID());
    }

    public static String getMainGuildID() {
        return Objects.requireNonNull(dotenv.get("MAIN_GUILD"));
    }

    public static boolean isAllowedGuild(Guild guild) {
        return GuildsHandler.isAllowed(guild.getId());
    }

    public static JDA getJDA() {
        return api;
    }
}
