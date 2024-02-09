package us.smartmc.lobbymodule.instance;

public @interface LinkSocialInfo {

    String linkFormat();
    boolean atDisabled() default true;
}
