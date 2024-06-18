package us.smartmc.bmotd.manager;

import net.kyori.adventure.text.Component;
import us.smartmc.bmotd.BMotdVelocity;
import us.smartmc.bmotd.instance.PluginYMLConfig;
import us.smartmc.bmotd.util.ChatUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MOTDUtil {

    private PluginYMLConfig config;

    private HashMap<String, Component> motdMap = new HashMap<>();

    public MOTDUtil(){
        registerConfigFromConfig();
    }

    public Component getMOTDFromDomain(String domain){
        if(motdMap.containsKey(domain)){
            return motdMap.get(domain);
        }
        return null;
    }

    public void registerConfigFromConfig(){
        File file = new File(BMotdVelocity.getPlugin().getDataFolder(), "config.yml");
        try {
            this.config = new PluginYMLConfig(file);
        } catch (Exception e){e.printStackTrace();}
        Map<String, Object> section = config.getSection("domain-profiles");
        if (section == null) return;;
        for(String all : section.keySet()){
            List<String> motdList = config.getStringList("domain-profiles."+all+".motd");
            if (motdList.isEmpty()) motdList = BMotdVelocity.getPlugin().getMotdManager().getProfileMotdList();
            String motdStr = motdList.get(0)+"\n"+ motdList.get(1);
            motdMap.put(all, ChatUtil.parseToComponent(motdStr));
        }
    }

}
