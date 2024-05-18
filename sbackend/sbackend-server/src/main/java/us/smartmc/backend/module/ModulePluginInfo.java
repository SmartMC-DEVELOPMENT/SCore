package us.smartmc.backend.module;

public @interface ModulePluginInfo {

    String name();
    String version() default "1.0.0";

}
