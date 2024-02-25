package us.smartmc.serverhandler.instance;

import lombok.Getter;
import us.smartmc.serverhandler.OrchestratorMain;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;

public class ServerConfigData implements Serializable {

    private static final File startupParent = new File(OrchestratorMain.getParentFolder() + "/startup");
    private static final File templateParent = new File(OrchestratorMain.getParentFolder() + "/template");

    private String port_range;
    private LinkedList<String> templates;
    private String startup;
    private String storage_parent;
    @Getter
    private String id_prefix;
    @Getter
    private boolean temporal = true;

    public File getStartupDirectory() {
        return new File(startupParent + "/" + startup);
    }

    public File getStorageParent() {
        return new File(storage_parent);
    }

    public LinkedList<File> getTemplateDirectories() {
        LinkedList<File> list = new LinkedList<>();
        for (String name : templates) {
            list.add(new File(templateParent + "/" + name));
        }
        return list;
    }

    public int getMaxPort() {
        return Integer.parseInt(port_range.split("-")[1]);
    }

    public int getMinPort() {
        return Integer.parseInt(port_range.split("-")[0]);
    }
}
