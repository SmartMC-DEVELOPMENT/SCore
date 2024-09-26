package us.smartmc.backend.gamescore;

import us.smartmc.backend.connection.BackendClient;
import us.smartmc.backend.gamescore.listener.CuboidGetResponseListener;
import us.smartmc.backend.gamescore.listener.CuboidSaveResponseListener;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.gamescore.instance.cuboid.Cuboid;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class BackendConnection extends BackendClient {

    private static BackendConnection connection;

    private static final Map<String, CompletableFuture<CuboidSaveResponse>> cuboidSendRequests = new HashMap<>();
    private static final Map<String, CompletableFuture<CuboidGetResponse>> cuboidGetRequests = new HashMap<>();

    public BackendConnection(String hostname, int port, String username, String password) throws IOException {
        super(hostname, port);
        connection = this;
        login(username, password);
        ConnectionInputManager.registerListeners(new CuboidSaveResponseListener(), new CuboidGetResponseListener());
    }

    public void handleCuboidGetResponse(CuboidGetResponse response) {
        CompletableFuture<CuboidGetResponse> future = cuboidGetRequests.get(response.getName());
        System.out.println("handleCuboidGetResponse " + future);
        System.out.println("handleCuboidGetResponse " + cuboidGetRequests.keySet());
        if (future == null) return;
        future.complete(response);
    }

    public void handleCuboidSaveResponse(CuboidSaveResponse response) {
        CompletableFuture<CuboidSaveResponse> future = cuboidSendRequests.get(response.getName());
        if (future == null) return;
        future.complete(response);
    }

    public CompletableFuture<CuboidGetResponse> getCuboid(String name) {
        CompletableFuture<CuboidGetResponse> future = new CompletableFuture<>();
        cuboidGetRequests.put(name, future);
        sendObject(new CuboidGetRequest(name));
        return future;
    }

    public CompletableFuture<CuboidSaveResponse> sendCuboid(String name, Cuboid cuboid) {
        CompletableFuture<CuboidSaveResponse> future = new CompletableFuture<>();
        cuboidSendRequests.put(name, future);
        sendObject(new CuboidSaveRequest(name, cuboid));
        return future;
    }

    public static Optional<BackendConnection> getBackendConnection() {
        if (connection == null) return Optional.empty();
        return Optional.of(connection);
    }
}
