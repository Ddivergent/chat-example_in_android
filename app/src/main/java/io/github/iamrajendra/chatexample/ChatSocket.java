package io.github.iamrajendra.chatexample;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by rajendra on 27/1/18.
 */

public class ChatSocket {
    public static final String INTENT_ACTION_JOIN = "action_join";
    public static final String INTENT_ACTION_STATUS = "action_status";
    public static final String INTENT_ACTION_MESSAGE = "action_message";
    private static String EVENT_CONNECTION = "connection";
    private static String EVENT_CHAT_MESSAGE = "chat_message";
    private static String EVENT_JOIN = "join";
    private static String EVENT_STATUS = "status";
    private String TAG = ChatSocket.class.getSimpleName();
    private final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            Log.i(TAG, "checkClientTrusted: " + authType + " " + chain);
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            Log.i(TAG, "checkServerTrusted: " + authType + " " + chain);
        }
    }
    };
    private Context context;
    private Socket client;
    private String url;
    private SSLContext sc;
    private boolean isNew;
    private boolean isReconecction;
    private boolean isSecure;
    private MessageHandler handler;
    private Callback callback;
    private Intent intent;


    public ChatSocket(Context context) {
        this.context = context;
    }

    public void sendMessage(String s) {
        client.emit(EVENT_CHAT_MESSAGE, s);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public ChatSocket setUrl(String url) {
        this.url = url;
        return this;
    }

    public ChatSocket forceNewConnection(boolean isNew) {
        this.isNew = isNew;
        return this;
    }

    public ChatSocket reConnection(boolean isReconecction) {
        this.isReconecction = isReconecction;
        return this;
    }

    public ChatSocket secure(boolean isSecure) {
        this.isSecure = isSecure;
        return this;
    }

    public void init() {
        intent = new Intent();
        handler = new MessageHandler();
        try {
            sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            IO.setDefaultSSLContext(sc);
            HttpsURLConnection.setDefaultHostnameVerifier(new RelaxedHostNameVerifier());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        IO.Options options = new IO.Options();
        options.forceNew = true;
        options.reconnection = false;
        options.secure = true;
        options.sslContext = sc;

        try {

            client = IO.socket(url, options);
            client.connect();
            client.on(Socket.EVENT_CONNECT, handler.onConnect);
            client.on(Socket.EVENT_CONNECTING, handler.onConnecting);
            client.on(Socket.EVENT_CONNECT_ERROR, handler.onConnectError);
            client.on(Socket.EVENT_CONNECT_TIMEOUT, handler.onConnectTimeOut);
            client.on(Socket.EVENT_ERROR, handler.onEventError);
            client.on(Socket.EVENT_RECONNECT, handler.onEventReconnect);
            client.on(Socket.EVENT_MESSAGE, handler.onEventMessage);
            client.on(ChatSocket.EVENT_CONNECTION, handler.onEventConnection);
            client.on(ChatSocket.EVENT_CHAT_MESSAGE, handler.onEventChatMessge);
            client.on(ChatSocket.EVENT_STATUS, handler.onEventStatus);
            client.on(ChatSocket.EVENT_JOIN, handler.onEventJoin);

        } catch (URISyntaxException e) {
            e.printStackTrace();

        }
    }

    public boolean isOnline() {

        if (client != null) {
            return true;
        } else {
            return false;
        }
    }

    public String getSocketId() {

        if (client != null) {
            return client.id();
        } else {
            return null;
        }
    }

    public interface Callback {
        void onMessage(String msg);
    }

    public static class RelaxedHostNameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private class MessageHandler {


        public Emitter.Listener onConnect = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i(TAG, "call: onConnect");


            }
        };
        public Emitter.Listener onConnecting = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i(TAG, "call: onConnecting");

            }
        };
        public Emitter.Listener onConnectError = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i(TAG, "call: onConnectError");

            }
        };
        public Emitter.Listener onConnectTimeOut = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i(TAG, "call: onConnectTimeOut");

            }
        };
        public Emitter.Listener onEventError = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i(TAG, "call: onEventError");

            }
        };
        public Emitter.Listener onEventReconnect = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i(TAG, "call: onConnect");

            }

        };
        public Emitter.Listener onEventMessage = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i(TAG, "call: onEventMessage");

            }


        };

        public Emitter.Listener onEventConnection = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i(TAG, "call: onEventConnection");

            }


        };
        ;
        public Emitter.Listener onEventChatMessge = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                intent.setAction(INTENT_ACTION_MESSAGE);
                intent.putExtra("message", args[0].toString());
                intent.putExtra("id", args[1].toString());
                context.sendBroadcast(intent);

            }


        };
        public Emitter.Listener onEventStatus = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i(TAG, "call: ");
                Gson gson = new Gson();
                UserModel chatModel = gson.fromJson(args[0].toString(), UserModel.class);

                intent.setAction(INTENT_ACTION_STATUS);
                intent.putExtra("user", chatModel);
                context.sendBroadcast(intent);


            }
        };


        public Emitter.Listener onEventJoin = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i(TAG, "call: ");
                Gson gson = new Gson();

                ArrayList<UserModel> userlist = (ArrayList<UserModel>) gson.fromJson(args[0].toString(), new TypeToken<List<UserModel>>(){}.getType());

                Log.i(TAG, "userlist: " + userlist.size());
                intent.setAction(INTENT_ACTION_JOIN);
                intent.putExtra("userlist", userlist);
                context.sendBroadcast(intent);

            }
        };
    }


}
