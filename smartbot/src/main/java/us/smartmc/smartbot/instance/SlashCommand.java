package us.smartmc.smartbot.instance;


import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class SlashCommand extends ListenerAdapter implements ISlashCommand {

    private final String name;
    private String description = "SmartBot command.";
    private final Set<String> aliases = new HashSet<>();

    public SlashCommand(String name) {
        this.name = name;
    }

    public SlashCommand(String name, String... aliases) {
        this.name = name;
        this.aliases.addAll(Arrays.asList(aliases));
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void execute(MessageReceivedEvent event) {

    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getName() {
        return name.toLowerCase();
    }

    @Override
    public Set<String> getAliases() {
        return aliases;
    }
}
