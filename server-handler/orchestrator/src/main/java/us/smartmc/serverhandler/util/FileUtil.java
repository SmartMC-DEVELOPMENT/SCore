package us.smartmc.serverhandler.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class FileUtil {

    public static void copyTemplates(File serverDestination, LinkedList<File> templates, int port, String name) {
        if (serverDestination.exists()) return;
        List<File> list = new ArrayList<>();
        for (File templateFile : templates) {
            list.addAll(copyDirToDir(templateFile, serverDestination));
        }

        list.forEach(file -> {
            if (!file.getName().equals("server.properties")) return;
            parseServerProperties(file, port, name);
        });
    }

    public static void createStartup(File startupDir, File destinationDir, int port, String name) {
        List<File> list = copyDirToDir(startupDir, destinationDir);
        for (File file : list) {
            if (!file.getName().equals("server.properties")) continue;
            parseServerProperties(file, port, name);
        }
    }

    public static void parseServerProperties(File file, int port, String name) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder newContent = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("server-port=")) {
                    line = "server-port=" + port;
                }
                if (line.startsWith("server-id=")) {
                    line = "server-id=" + name;
                }
                newContent.append(line).append(System.lineSeparator());
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(newContent.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    public static List<File> copyDirToDir(File source, File target) {
        if (!source.isDirectory()) {
            throw new IllegalArgumentException("Source must be a directory");
        }

        if (!target.exists()) {
            target.mkdirs();
        } else if (!target.isDirectory()) {
            throw new IllegalArgumentException("Target must be a directory");
        }

        try {
            Files.walk(source.toPath())
                    .forEach(sourcePath -> {
                        Path targetPath = target.toPath().resolve(source.toPath().relativize(sourcePath));
                        try {
                            if (!Files.isDirectory(sourcePath)) {
                                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                            } else if (!Files.exists(targetPath)) {
                                Files.createDirectory(targetPath);
                            }
                            // Los directorios existentes se manejan por sus contenidos, no se intenta sobrescribir el directorio en sí.
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException("Error copying directory", e);
        }

        return Arrays.asList(target.listFiles());
    }

    public static void deleteDirectoryContents(File dir) throws IOException {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Not a directory: " + dir);
        }

        Files.walk(dir.toPath())
                .sorted(Comparator.reverseOrder()) // Ordena los elementos de manera que los subdirectorios vengan antes que sus directorios padre
                .map(Path::toFile)
                .forEach(File::delete); // Elimina cada archivo y directorio
    }

}
