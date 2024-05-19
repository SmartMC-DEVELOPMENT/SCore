package us.smartmc.core.instance.data;

import java.util.HashSet;

public class ListRegistry<O> extends HashSet<O> {

    public void register(O object) {
        if (!contains(object)) add(object);
    }

    public void unregister(O object) {
        remove(object);
    }
}
