package us.smartmc.smartbot.instance;

public abstract class TextCommand implements BotCommand {

    private String name;

    public TextCommand(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
