package us.smartmc.migration.messages;

import lombok.Getter;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.LanguageMessagesHolder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class EditSession {

    @Getter
    private final BufferedReader reader;

    private String currentHolderName;
    private int keyIndex = 0;
    private final HashMap<String, List<String>> map = new HashMap<>();

    public EditSession() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        fillMap();
        Optional<String> optional = map.keySet().stream().findFirst();
        if (optional.isEmpty()) {
            System.out.println("EditSession not found more valid holders!");
            return;
        }
        start();
    }

    private void start() {
        currentHolderName = new ArrayList<>(map.keySet()).get(keyIndex);
        if (currentHolderName == null) {
            System.out.println("Finished EditSession -> No more handlers");
        }
        List<String> keys = map.get(currentHolderName);
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            System.out.println("Want to rename " + key + " (in " + currentHolderName + ")? ");
            try {
                String input = reader.readLine();
                if (input.length() >= 3) {
                    renameKey(key, input);
                }
                keys.remove(key);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        keyIndex++;
        start();
    }

    private void renameKey(String key, String newKey) {
        LanguagesHandler.forEach((languageHolder) -> {
            LanguageMessagesHolder holder = languageHolder.get(currentHolderName);
            Object value = holder.get(key);
            holder.put(newKey, value);
            holder.remove(key);
            holder.save();
        });
    }

    private void fillMap() {
        LanguagesHandler.forEach((languageHolder) -> {
            languageHolder.getMessagesHolders().forEach(holder -> {
                String name = holder.getName();
                List<String> keys = holder.keySet().stream().filter(k -> k.contains("_")).collect(Collectors.toList());
                if (!keys.isEmpty()) map.put(name, keys);
            });
        });
    }

}
