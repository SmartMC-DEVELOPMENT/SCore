package us.smartmc.smartbot.handler;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import us.smartmc.smartbot.instance.ReplyListener;
import us.smartmc.smartbot.responses.IpAddressEvent;
import us.smartmc.smartbot.responses.AddressAskEventResponse;
import us.smartmc.smartbot.responses.NextEventResponse;

import java.util.*;

public class RepliesHandler extends ListenerAdapter {

    private static final List<ReplyListener> listeners = new ArrayList<>();

    public RepliesHandler() {
        register(new NextEventResponse(),
                new IpAddressEvent(),
                new AddressAskEventResponse());
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        for (ReplyListener listener : listeners) {
            listener.onMessage(event.getMessage());
        }
    }

    public static void register(ReplyListener... all) {
        listeners.addAll(Arrays.asList(all));
    }
}
