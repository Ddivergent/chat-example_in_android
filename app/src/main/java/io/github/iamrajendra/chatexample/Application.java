package io.github.iamrajendra.chatexample;

/**
 * Created by rajendra on 28/1/18.
 */

public class Application extends android.app.Application {
    private ChatSocket socket;

    @Override
    public void onCreate() {
        super.onCreate();
        socket = new ChatSocket(getApplicationContext());
        socket.setUrl("http://192.168.1.6:3000/").forceNewConnection(false).
                reConnection(false).secure(false).init();
    }

    public String getId() {
        return socket.getSocketId();
    }

    public void sendMessage(String msg) {
        socket.sendMessage(msg);
    }
}
