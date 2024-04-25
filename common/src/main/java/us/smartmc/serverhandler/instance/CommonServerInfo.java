package main.java.us.smartmc.serverhandler.instance;

public class CommonServerInfo {

    protected final String name, hostname;
    protected final int port;

    private ServerStatus serverStatus = ServerStatus.IDLE;

    public CommonServerInfo(String name, String hostname, int port) {
        this.name = name;
        this.hostname = hostname;
        this.port = port;
    }

    public void setStatus(ServerStatus serverStatus) {
        this.serverStatus = serverStatus;
        if (serverStatus.equals(ServerStatus.IDLE)) {
            System.out.println("Server disconnected! " + name);
        }
    }

    public String getHostname() {
        return hostname;
    }

    public ServerStatus getStatus() {
        return serverStatus;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }
}
