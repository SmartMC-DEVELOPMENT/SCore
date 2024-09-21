package us.smartmc.gamescore.instance.manager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class SetManager<T> implements ISetManager<T> {

    private static final Map<Class<? extends SetManager<?>>, SetManager<?>> registry = new HashMap<>();

    private final Set<T> set = new HashSet<>();

    @Override
    public int size() {
        return set.size();
    }

    public static <T extends SetManager<?>> T getManager(final Class<T> clazz, Object... initArgs) {
        SetManager<?> manager = registry.get(clazz);
        if (manager == null) {
            try {
                manager = (SetManager<?>) createManager(clazz, initArgs);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        if (clazz.isInstance(manager)) {
            return clazz.cast(manager);
        }
        return null;
    }

    private static Object createManager(final Class<? extends SetManager<?>> clazz, Object... initArgs) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        SetManager<?> o = (SetManager<?>) constructor.newInstance(initArgs);
        constructor.setAccessible(false);
        registry.put(clazz, o);
        return o;
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return set.iterator();
    }

    @Override
    public Object[] toArray() {
        return set.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return set.toArray(a);
    }

    @Override
    public boolean add(T t) {
        return set.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return set.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return set.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return set.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return set.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return set.removeAll(c);
    }

    @Override
    public void clear() {
        set.clear();
    }
}
