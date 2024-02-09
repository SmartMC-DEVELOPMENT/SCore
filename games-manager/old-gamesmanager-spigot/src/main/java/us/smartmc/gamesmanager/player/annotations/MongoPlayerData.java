package us.smartmc.gamesmanager.player.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MongoPlayerData {
  String name() default "[null]";
}
