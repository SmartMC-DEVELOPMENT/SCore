package us.smartmc.backend.connection.listener;

import us.smartmc.backend.connection.BackendClient;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.protocol.LoginCompleted;

import java.lang.reflect.Method;

public class LoginCompleteListener extends BackendObjectListener<LoginCompleted> {

    private final ConnectionHandler connectionHandler;

    public LoginCompleteListener(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    @Override
    public void onReceive(ConnectionHandler connection, LoginCompleted o) {
        try {
            Method method = BackendClient.class.getDeclaredMethod("registerSuccessLogin");
            method.setAccessible(true);
            method.invoke(connectionHandler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
