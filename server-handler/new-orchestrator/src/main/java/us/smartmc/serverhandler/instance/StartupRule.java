package us.smartmc.serverhandler.instance;

import java.util.HashMap;

public class StartupRule {

    private final HashMap<String, Object> replacements = new HashMap<>();

    public void createRule(String varKey, Object value) {
        replacements.put(varKey, value);
    }

    public Object getRule(String varKey) {
        return replacements.get(varKey);
    }

    public String parse(String line) {
        for (String varKey : replacements.keySet()) {
            if (line.contains(varKey)) {
                line = line.replace(varKey, replacements.get(varKey).toString());
            }
        }
        return line;
    }
}
