package us.smartmc.backend.connection;

import javax.net.ssl.SSLSocket;

public class ConnectionHandler implements Runnable {

    private final SSLSocket socket;

    private ConnectionInputStream inputStream;
    private ConnectionOutputStream outputStream;

    public ConnectionHandler(SSLSocket socket) {
        this.socket = socket;

    }

    @Override
    public void run() {
        try {
            inputStream = new ConnectionInputStream(this);
            outputStream = new ConnectionOutputStream(this);

            while (inputStream.read) {

            }
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

}

}
