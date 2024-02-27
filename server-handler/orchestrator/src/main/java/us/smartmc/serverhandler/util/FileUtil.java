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
    private static final Set<String> recentCreatedPermanentServers = new HashSet<>();

    public static void removeAndComplete(String dir) {
        StartupCreation creation = startupCreations.remove(dir);
        if (creation == null) return;
        creation.completeAndCopy();
    }

    public static void copyTemplates(File serverDestination, ServerConfiguration<?> configuration) {
        if (!recentCreatedPermanentServers.contains(serverDestination.getAbsolutePath()) && configuration.getData().isPermanent()) return;
        for (File templateDir : configuration.getData().getTemplateDirectories()) {
            copyDirToDir(templateDir, serverDestination);
        }
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
        if (!destinationDir.exists()) {
            if (config.getData().isPermanent()) {
                recentCreatedPermanentServers.add(destinationDir.getAbsolutePath());
            }
            copyDirToDir(config.getData().getStartupDirectory(), destinationDir);
        }
        StartupCreation creation = new StartupCreation(config.getData().getStartupDirectory(), destinationDir, port, name, id);
        startupCreations.put(destinationDir.getAbsolutePath(), creation);
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
