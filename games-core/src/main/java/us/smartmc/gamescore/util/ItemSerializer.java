package us.smartmc.gamescore.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ItemSerializer {

    public static String serializeItemStack(ItemStack item) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);

        // Escribimos el ItemStack
        bukkitObjectOutputStream.writeObject(item);
        bukkitObjectOutputStream.close();

        // Convertimos a Base64 y retornamos como String
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    public static ItemStack deserializeItemStack(String base64) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64));
        BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(byteArrayInputStream);

        // Leemos el ItemStack desde el stream
        ItemStack item = (ItemStack) bukkitObjectInputStream.readObject();
        bukkitObjectInputStream.close();

        return item;
    }

}
