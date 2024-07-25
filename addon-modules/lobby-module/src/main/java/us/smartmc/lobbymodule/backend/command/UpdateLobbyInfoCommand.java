package us.smartmc.lobbymodule.backend.command;

import org.bson.Document;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;
import us.smartmc.lobbymodule.handler.LobbiesInfoManager;

public class UpdateLobbyInfoCommand extends BackendCommandExecutor {

    public UpdateLobbyInfoCommand() {
        super("updateLobbyInfo");
    }

    @Override
    public void onCommand(ConnectionHandler connection, String label, String[] args) {
        String json = label.replace(getName() + " ", "");
        Document document = Document.parse(json);
        LobbiesInfoManager.updateInfo(document);
    }
}
