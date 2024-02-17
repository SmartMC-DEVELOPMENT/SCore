package us.smartmc.serverhandler;

import us.smartmc.serverhandler.instance.Configuration;

public interface IConfigRegistration extends IRegistration {

    <T extends Configuration<?>> void load(String filePath, Class<T> clazz);

}
