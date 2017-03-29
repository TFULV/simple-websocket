package lv.tfu.servers.simplewebsocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/")
public class WebSocketClient {
    @OnOpen
    public void onOpen(Session session) {
        System.out.println(session.getId() + " has opened a connection");
        if(Main.WELCOME_MESSAGE) {
            try {
                session.getBasicRemote().sendText("Connection Established. Echo mode = " + (Main.ECHO_SERVER ? "on" : "off"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        Main.addClients(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("[" + session.getId() + "]: " + message);
        if(Main.ECHO_SERVER) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Session " +session.getId()+" has ended");
        Main.removeClients(session);
    }

    @OnError
    public void onError(Throwable exception, Session session) {
        exception.printStackTrace();
        System.err.println("Error for client: " + session.getId());
    }
}
