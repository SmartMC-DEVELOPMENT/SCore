package me.imsergioh.loginspigot.instance;

import lombok.Getter;
import me.imsergioh.loginspigot.LoginSpigot;
import me.imsergioh.loginspigot.manager.LoginPlayersFactory;
import me.imsergioh.pluginsapi.util.PluginUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class LoginPlayer {

    private static final LoginSpigot plugin = LoginSpigot.getPlugin();

    private final UUID uuid;
    private final Document document;
    @Getter
    boolean check;
    @Getter
    boolean auth;

    public LoginPlayer(UUID uuid) {
        this.uuid = uuid;
        document = loadData();
        document.put("uuid", uuid.toString());
    }

    private Document loadData() {
        Document query = getQueryDocument();
        Document mongoDoc = plugin.getMongoClient().getDatabase("proxy").getCollection("loginplayers").find(query).first();
        if (mongoDoc != null) return mongoDoc;
        return query;
    }

    public void checkSecretKey() {
        if (!document.containsKey("secretKey")) {
            return;
        }
        if (verify(getIP(), "secretKey")) {
            setAuth(true);
            sendToLobbyServer();
        }
    }

    public void forceLogin() {
        setAuth(true);
    }

    public void register(String password, String confirmPassword) {
        if (password.equals(confirmPassword)) {
            document.put("password", hash(password));
            setSecretKey();
            save();
            getPlayer().sendMessage("§a¡Registrado correctamente!");
            setAuth(true);
            sendToLobbyServer();
            return;
        }
        getPlayer().sendMessage("§cAmbas contraseñas introducidas no son iguales.");
    }

    public void sendToLobbyServer() {
        LoginPlayer loginPlayer = LoginPlayersFactory.get(getPlayer());
        if (loginPlayer == null) return;
       PluginUtils.redirectTo(getPlayer(), "lobby");
    }

    public void tryLogin(String password) {
        if (verify(password, "password")) {
            getPlayer().sendMessage("§a¡Has iniciado sesión correctamente!");
            setAuth(true);
            setSecretKey();
            save();
            sendToLobbyServer();
            return;
        }
        setAuth(false);
        getPlayer().kickPlayer("§cContraseña incorrecta!");
    }

    public void sendAuthMessage() {
        if (isRegistered()) {
            getPlayer().sendMessage("§cInicia sesión con: /login <password>");
            return;
        }
        getPlayer().sendMessage("§cRegistrate con: /register <password> <confirm same password>");
    }

    private void setSecretKey() {
        document.put("secretKey", hash(getIP()));
    }

    public boolean isPremium() {
        return uuid.version() == 4;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
        if (auth) {
            me.imsergioh.loginspigot.util.PluginUtils.sendLoginRequest(getPlayer());
        }
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isRegistered() {
        return document.containsKey("password");
    }

    public String getIP() {
        return getPlayer().getAddress().getAddress().toString();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void save() {
        plugin.getMongoClient().getDatabase("proxy")
                .getCollection("loginplayers")
                .deleteMany(getQueryDocument());
        plugin.getMongoClient().getDatabase("proxy")
                .getCollection("loginplayers")
                .insertOne(document);
    }

    public Document getQueryDocument() {
        return new Document("uuid", uuid.toString());
    }

    public boolean verify(String password, String path) {
        return BCrypt.checkpw(password, document.getString(path));
    }

    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

}
