package us.smartmc.lobbymodule.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TexturePackUtil {

    public static String calculateSHA1FromURL(String urlString) throws IOException, NoSuchAlgorithmException {
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        InputStream inputStream = connection.getInputStream();
        byte[] byteArray = new byte[1024];
        int bytesCount;

        while ((bytesCount = inputStream.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }

        inputStream.close();

        byte[] hashBytes = digest.digest();

        // Convertir bytes a representación hexadecimal
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

}
