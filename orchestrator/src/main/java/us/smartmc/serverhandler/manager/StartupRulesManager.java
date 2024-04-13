package us.smartmc.serverhandler.manager;

import us.smartmc.serverhandler.instance.StartupRule;

import java.util.HashMap;

public class StartupRulesManager {

    private static HashMap<String, StartupRule> rules = new HashMap<>();

    public static void registerStartupRule(String filePath, StartupRule rule) {
        rules.put(filePath, rule);
    }

}
