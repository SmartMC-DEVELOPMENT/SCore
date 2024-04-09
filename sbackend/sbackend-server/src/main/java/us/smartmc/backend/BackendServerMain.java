package us.smartmc.backend;

import lombok.Getter;
import us.smartmc.backend.command.HelloWorldCmd;
import us.smartmc.backend.connection.BackendServer;
import us.smartmc.backend.handler.AuthHandler;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.instance.config.JsonConfig;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.File;
import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.security.KeyStore;

public class BackendServerMain {

    @Getter
    private static File parentDirectory;

    @Getter
    private static BackendServer backendServer;

    private static JsonConfig mainConfig;

    public static void main(String[] args) throws Exception {
        parentDirectory = getJarParentDirectory();

        mainConfig = new JsonConfig(new File(parentDirectory, "config.json"));
        mainConfig.load();
        mainConfig.registerDefaultValue("keystorePath", "/home/network/certificados");
        mainConfig.registerDefaultValue("keystorePass", "P4ssw0rdS3cvre2024YTSMARTMCÑ");
        mainConfig.registerDefaultValue("logins-directory", "/home/network/sbackend/logins");
        mainConfig.registerDefaultValue("port", 7723);

        AuthHandler.loadCache();

        String keystorePath = ((String) mainConfig.get("keystorePath"));
        char[] keystorePass = ((String) mainConfig.get("keystorePass")).toCharArray();

        SSLContext sslContext = SSLContext.getInstance("TLS");
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(keystorePath), keystorePass);
        keyManagerFactory.init(keyStore, keystorePass);
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

        // Crear el SSLServerSocketFactory
        SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
        backendServer = new BackendServer(sslServerSocketFactory, (int) ((Number) mainConfig.get("port")));

        ConnectionInputManager.registerCommands(new HelloWorldCmd());
    }

    public static File getLoginsDirectory() {
        return new File((String) mainConfig.get("logins-directory"));
    }

    private static File getJarParentDirectory() throws URISyntaxException {
        String jarPath = BackendServerMain.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        File jarFile = new File(jarPath);
        return new File(jarFile.getParent());
    }

}
