package us.smartmc.backend.connection.listener;

import us.smartmc.backend.connection.BackendClient;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.protocol.LoginCompleted;

import java.lang.reflect.Method;

public class LoginCompleteListener extends BackendObjectListener<LoginCompleted> {

    @Override
    public void onReceive(ConnectionHandler connection, LoginCompleted o) {
        try {
            Method method = BackendClient.class.getDeclaredMethod("registerSuccessLogin");
            method.setAccessible(true);
            method.invoke(BackendClient.mainConnection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
