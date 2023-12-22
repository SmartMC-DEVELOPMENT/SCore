package us.smartmc.core.pluginsapi.util;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class JavaUtil {

    public static List<ClassLoader> getClassLoaders() {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        List<ClassLoader> classLoaders = new ArrayList<>();

        while (classLoader != null) {
            classLoaders.add(classLoader);
            classLoader = classLoader.getParent();
        }

        // Agrega el ClassLoader del contexto actual (Thread context ClassLoader).
        classLoaders.add(Thread.currentThread().getContextClassLoader());

        // Ahora, classLoaders contiene una lista de todos los ClassLoaders en la jerarquía.

        for (ClassLoader cl : classLoaders) {
            System.out.println("ClassLoader: " + cl.toString());
        }
        return classLoaders;
    }

}
