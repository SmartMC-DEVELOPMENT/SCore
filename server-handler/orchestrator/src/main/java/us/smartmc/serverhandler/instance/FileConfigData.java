package us.smartmc.serverhandler.instance;

import com.google.gson.internal.LinkedTreeMap;
import lombok.Getter;
import org.bson.Document;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class FileConfigData implements Serializable {

    @Getter
    private LinkedTreeMap<String, Object> data;
}
