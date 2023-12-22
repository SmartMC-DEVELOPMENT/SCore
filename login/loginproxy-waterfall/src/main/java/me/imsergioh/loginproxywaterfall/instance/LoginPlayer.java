package me.imsergioh.loginproxywaterfall.instance;

import me.imsergioh.loginproxywaterfall.LoginProxyWaterfall;
import me.imsergioh.loginproxywaterfall.listener.AuthPlayersListeners;
import us.smartmc.core.pluginsapi.connection.MongoDBConnection;
import us.smartmc.core.pluginsapi.data.storage.DocumentLoader;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class LoginPlayer extends DocumentLoader {

    private final UUID uuid;
    boolean check;
    boolean auth;

    public LoginPlayer(UUID uuid) {
        super("uuid", uuid.toString());
        this.uuid = uuid;
        Document loaded = load();
        if (loaded != null) {
            putAll(loaded);
        }
        append("uuid", uuid.toString());
    }

    public void checkSecretKey() {
        if (!containsKey("secretKey")) {
            sendToAuthServer();
            return;
        }
        if (verify(getIP(), "secretKey")) {
            auth = true;
            sendToLobbyServer();
            return;
        }
        sendToAuthServer();
    }

    public void register(String password, String confirmPassword) {
        if (password.equals(confirmPassword)) {
            put("password", hash(password));
            setSecretKey();
            save();
            getPlayer().sendMessage("§aRegistered successfully!");
            auth = true;
            sendToLobbyServer();
            return;
        }
        getPlayer().sendMessage("§cBoth passwords entered are not the same.");
    }

    public void sendToLobbyServer() {
        AuthPlayersListeners.connectTo(getPlayer(), LoginProxyWaterfall.getPlugin().getServers("lobby"));
    }

    public void sendToAuthServer() {
        AuthPlayersListeners.connectTo(getPlayer(), LoginProxyWaterfall.getPlugin().getServers("auth"));
    }

    public void tryLogin(String password) {
        if (verify(password, "password")) {
            getPlayer().sendMessage("§aLogged in!");
            auth = true;
            setSecretKey();
            save();
            sendToLobbyServer();
            return;
        }
        auth = false;
        getPlayer().disconnect("§cWrong password!");
    }

    public void sendAuthMessage() {
        if (isRegistered()) {
            getPlayer().sendMessage("§cLogin with command: /login <password>");
            return;
        }
        getPlayer().sendMessage("§cRegister with command: /register <password> <confirm same password>");
    }

    private void setSecretKey() {
        put("secretKey", hash(getIP()));
    }

    public boolean isPremium() {
        return uuid.version() == 4;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isRegistered() {
        return containsKey("password");
    }

    public boolean isAuth() {
        return auth;
    }

    public boolean isCheck() {
        return check;
    }

    public String getIP() {
        return getPlayer().getPendingConnection().getAddress().getAddress().toString();
    }

    public ProxiedPlayer getPlayer() {
        return LoginProxyWaterfall.getPlugin().getProxy().getPlayer(uuid);
    }

    @Override
    public Document load() {
        return MongoDBConnection.mainConnection.getDatabase("proxy")
                .getCollection("loginplayers")
                .find(getQueryDocument()).first();
    }

    @Override
    public Document save() {
        MongoDBConnection.mainConnection.getDatabase("proxy")
                .getCollection("loginplayers")
                .deleteMany(getQueryDocument());
        MongoDBConnection.mainConnection.getDatabase("proxy")
                .getCollection("loginplayers")
                .insertOne(this);
        return this;
    }

    public boolean verify(String password, String path) {
        return BCrypt.checkpw(password, getString(path));
    }

    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

}
