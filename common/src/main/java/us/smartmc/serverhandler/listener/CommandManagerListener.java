package us.smartmc.serverhandler.listener;

import me.imsergioh.jbackend.api.ConnectionHandler;
import me.imsergioh.jbackend.api.process.ObjectProcessor;
import me.imsergioh.jbackend.api.process.ProcessorType;
import us.smartmc.serverhandler.instance.BackendCommandExecuteRequest;
import us.smartmc.serverhandler.manager.BackendCommandManager;


@ProcessorType(classOf = BackendCommandExecuteRequest.class)
public class CommandManagerListener extends ObjectProcessor {

    @Override
    public void process(ConnectionHandler handler, Object o) {
        BackendCommandExecuteRequest request = (BackendCommandExecuteRequest) o;
        BackendCommandManager.perform(handler, request);
    }
}
