package us.smartmc.core.pluginsapi.data.managment;

public interface IFactoryValue<O> {

    void load();
    void unload();

    O get();
}
