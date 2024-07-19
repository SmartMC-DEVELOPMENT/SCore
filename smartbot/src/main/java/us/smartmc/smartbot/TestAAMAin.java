package us.smartmc.smartbot;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import us.smartmc.backend.connection.BackendClient;
import us.smartmc.smartbot.manager.LogsManager;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TestAAMAin extends ListenerAdapter {

    private static BackendClient backendClient;

    @Getter
    private static LogsManager logsManager;

    public static void main(String[] args) throws IOException {
        // CREATION BOT >>
        JDABuilder builder = JDABuilder.createDefault("MTI2MzU1MTIxMDM5NzM3MjUwNw.GdZygC._vFi5Fv4nipnGqCQF5nf8VdhPYKVIQg419VIU8")
                .setActivity(Activity.playing("Ready to serve!"))
                .enableIntents(GatewayIntent.DIRECT_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new TestAAMAin());
        JDA jda = builder.build();

        System.out.println("Ready! Logged in as " + jda.getSelfUser().getAsTag());

        // Register slash commands

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                registerSlashCommands(jda);
            }
        }, 1000);

    }

    private static void registerSlashCommands(JDA jda) {

        SlashCommandData commandData = Commands.slash("test", "Test app command");
        commandData.addOption(OptionType.STRING, "animal", "The type of animal", true);
        commandData.addOption(OptionType.BOOLEAN, "only_smol", "Whether to show only baby animals", false);
        jda.upsertCommand(commandData).complete();
        System.out.println("Slash command registered successfully.");
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "ping":
                event.reply("Pong!").queue();
                break;
            case "blep":
                event.reply("Blep command received!").queue();
                break;
            default:
                event.reply("Unknown command").queue();
                break;
        }
    }
}
