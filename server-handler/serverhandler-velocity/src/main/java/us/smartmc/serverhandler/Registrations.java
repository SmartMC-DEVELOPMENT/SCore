package us.smartmc.serverhandler;

public class Registrations {

    @SafeVarargs
    public static void register(Class<? extends IRegistration>... classes) {
        for (Class<? extends IRegistration> clazz : classes) {
            try {
                clazz.newInstance().register();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
