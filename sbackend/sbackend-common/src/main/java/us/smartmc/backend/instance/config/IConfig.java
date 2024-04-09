package us.smartmc.backend.instance.config;

public interface IConfig {

    void load();
    void save();

    void registerDefaultValue(String path, Object value);
    void set(String path, Object value);
    Object get(String path);

}
