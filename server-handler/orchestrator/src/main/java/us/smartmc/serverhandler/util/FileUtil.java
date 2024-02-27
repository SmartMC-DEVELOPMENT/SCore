package us.smartmc.serverhandler.util;

import us.smartmc.serverhandler.config.ServerConfiguration;
import us.smartmc.serverhandler.instance.StartupCreation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class FileUtil {

    private static final Map<String, StartupCreation> startupCreations = new HashMap<>();

    public static StartupCreation getCreation(String dir) {
        return startupCreations.get(dir);
    }

    public static void removeAndComplete(String dir) {
        startupCreations.remove(dir).completeAndCopy();
    }

    public static void copyTemplates(File serverDestination, ServerConfiguration<?> configuration, int port, String name, String prefixId) {
        if (!serverDestination.exists()) serverDestination.mkdirs();
        if (serverDestination.exists() && configuration.getData().isPermanent()) return;
        if (serverDestination.exists() && !configuration.getData().isPermanent()) {
            serverDestination.delete();
        }
        for (File templateDir : configuration.getData().getTemplateDirectories()) {
            copyDirToDir(templateDir, serverDestination);
        }
        removeAndComplete(serverDestination.getAbsolutePath());
    }

    private static void parseStartupTemplates(ServerConfiguration<?> configuration, StartupCreation creation) {
        for (File templateDir : configuration.getData().getTemplateDirectories()) {
            for (File file : Objects.requireNonNull(templateDir.listFiles())) {
                if (!file.getName().equals("server.properties")) continue;
                creation.readFileAndValues(file);
            }
        }
    }

    public static void createStartup(ServerConfiguration<?> config, File destinationDir, int port, String name, String id) {
        StartupCreation creation = new StartupCreation(config.getData().getStartupDirectory(), destinationDir, port, name, id);
        startupCreations.put(config.getData().getStartupDirectory().getAbsolutePath(), creation);
        parseStartupTemplates(config, creation);
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

    public static void copyDirToDir(File source, File target) {
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
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException("Error copying directory", e);
        }
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
