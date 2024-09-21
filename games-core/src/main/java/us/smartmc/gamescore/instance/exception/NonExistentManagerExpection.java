package us.smartmc.gamescore.instance.exception;

public class NonExistentManagerExpection extends GameCoreException {

    public NonExistentManagerExpection(Class<?> type) {
        super("Not existent manager found of type " + type.getSimpleName());
    }
}
