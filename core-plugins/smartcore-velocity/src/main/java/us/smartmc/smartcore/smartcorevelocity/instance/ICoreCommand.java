package us.smartmc.smartcore.smartcorevelocity.instance;

import com.velocitypowered.api.command.CommandSource;

import java.util.List;

public interface ICoreCommand {

    void execute(CommandSource sender, String[] args);

    List<String> getAliases();

    String getName();

}
