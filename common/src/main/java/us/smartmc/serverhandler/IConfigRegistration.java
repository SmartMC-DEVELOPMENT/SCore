package main.java.us.smartmc.serverhandler;

import main.java.us.smartmc.serverhandler.instance.Configuration;

public interface IConfigRegistration extends IRegistration {

    <T extends Configuration<?>> void load(String filePath, Class<T> clazz);

}
