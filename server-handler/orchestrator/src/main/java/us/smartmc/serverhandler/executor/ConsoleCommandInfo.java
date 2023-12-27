package us.smartmc.serverhandler.executor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConsoleCommandInfo {

    String name();

    String[] aliases() default {}; // [alias1, alias2, ..., aliasN]

    String description() default "No description.";

    String usage() default "No usage found.";

    int minArgs() default 0;

    int maxArgs() default -1;
}
