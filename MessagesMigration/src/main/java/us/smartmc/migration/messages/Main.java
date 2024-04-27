package us.smartmc.migration.messages;

import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    private static final Set<String> holders = new HashSet<>();

    public static void main(String[] a) {
        registerLanguages();

        load("language",
                "lobby_miniGames",
                "lobby",
                "sanctions_manager",
                "proxy_main",
                "npcs-module",
                "item_utils",
                "general",
                "snowgames/lobby/main",
                "snowgames/ffa/main",
                "cosmetics_info/UNKNOWN",
                "cosmetics_info/main",
                "cosmetics_info/HATS");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String line;
        try {
            while ((((line = reader.readLine())) != null)) {
                String cmdName = line.contains(" ") ? line.split(" ")[0] : line;
                String[] args = line.contains(" ") ? line.replace(cmdName + " ", "").split(" ") : new String[]{};

                System.out.println("PERFORMING " + cmdName + " Args=" + Arrays.toString(args));

                switch (cmdName.toLowerCase()) {
                    case "changepaths" -> {
                        changePaths(ChangePathsType.valueOf(args[0]), args[1], args[2]);
                    }
                    case "stringtolists" -> {
                        fromStringToList();
                    }
                    case "save" -> {
                        saveAllLoadedHolders();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void saveAllLoadedHolders() {
        for (String name : holders) {
            LanguagesHandler.forEach((languageHolder) -> {
                languageHolder.get(name).save();
            });
        }
    }

    private static void registerLanguages() {
        for (Language language : Language.values()) {
            LanguagesHandler.register(language);
        }
    }

    private static void load(String... names) {
        for (String name : names) {
            LanguagesHandler.forEach((languageHolder) -> {
                languageHolder.get(name).load();
            });
            holders.add(name);
        }
    }

    private static void fromStringToList() {
        LanguagesHandler.forEach(languageHolder -> {
            for (LanguageMessagesHolder holder : languageHolder.getMessagesHolders()) {
                parseDocumentFromStringToList(holder);
            }
        });
    }

    private static void parseDocumentFromStringToList(Document document) {
        for (String path : new HashSet<>(document.keySet())) {
            Object value = document.get(path);
            if (value instanceof String strValue && strValue.contains("\n")) {
                value = new ArrayList<>(Arrays.asList(strValue.split("\n")));
                document.put(path, value);
                System.out.println("From String to List " + path);
            }

            if (value instanceof Document documentValue) {
                parseDocumentFromStringToList(documentValue);
            }
        }
    }

    private static void changePaths(ChangePathsType type, String oldPathPrefix, String newPathPrefix) {
        LanguagesHandler.forEach(languageHolder -> {
            languageHolder.getMessagesHolders().forEach(holder -> {
                migratePathsFrom(type, holder, oldPathPrefix, newPathPrefix);
            });
        });
    }

    private static void migratePathsFrom(ChangePathsType type, Document document, String oldPathPrefix, String newPathPrefix) {
        for (String key : new HashSet<>(document.keySet())) {
            Object value = document.get(key);
            boolean isValidOldPath = false;

            if (value instanceof Document documentValue) {
                migratePathsFrom(type, documentValue, oldPathPrefix, newPathPrefix);
                continue;
            }

            switch (type) {
                case CONTAINS -> isValidOldPath = key.contains(oldPathPrefix);
                case ENDS_WITH -> isValidOldPath = key.endsWith(oldPathPrefix);
                case STARTS_WITH -> isValidOldPath = key.startsWith(oldPathPrefix);
            }

            if (isValidOldPath) {

                // Checks the values string and string in lists and replaces the oldVersion of pathPrefix to newPathPrefix
                if (value instanceof String str && str.contains(oldPathPrefix)) {
                    value = str.replace(oldPathPrefix, newPathPrefix);
                    document.put(key, value);
                    return;
                }

                if (value instanceof ArrayList<?> list) {
                    List<String> stringList = (ArrayList<String>) list;
                    int index = 0;
                    for (String line : new ArrayList<>(stringList)) {
                        if (line.contains(oldPathPrefix)) {
                            index++;
                            continue;
                        }
                        line = line.replace(oldPathPrefix, newPathPrefix);
                        stringList.set(index, line);
                        System.out.println("Changed list = " + oldPathPrefix);
                        index++;
                    }
                }

                if (value instanceof Document documentValue) {
                    migratePathsFrom(type, documentValue, oldPathPrefix, newPathPrefix);
                    continue;
                }

                document.remove(key);
                String modernPath = key.replace(oldPathPrefix, newPathPrefix);
                document.put(modernPath, value);
                System.out.println("Migrated " + oldPathPrefix + " " + newPathPrefix + " (" + modernPath + ")");
            }
        }
    }

    public enum ChangePathsType {
        STARTS_WITH, CONTAINS, ENDS_WITH
    }

}
